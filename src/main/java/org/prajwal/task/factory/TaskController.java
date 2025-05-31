package org.prajwal.task.factory;

import org.prajwal.task.Task;
import org.prajwal.task.TaskException;
import org.prajwal.task.TaskStatus;
import org.prajwal.task.registry.TaskRegistry;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class TaskController {
    private static final AtomicLong TASK_ID_GENERATOR = new AtomicLong(1);
    private static final Map<Long, Task> TASK_ID_TASK_MAP = new ConcurrentHashMap<>();
    private static final AtomicBoolean taskControllerRunning = new AtomicBoolean(true);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final CompletionService<Void> completionService = new ExecutorCompletionService<>(executorService);
    private static final Set<Task> pausedTasks = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static Thread trackerThread;
    private static Thread pausedTasksTracker;
    private static final int PAUSE_TASK_TRACKER_INTERVAL = 10_000;
    private static final ScheduledExecutorService resumeTaskScheduleService = Executors.newScheduledThreadPool(10);

    private TaskController() {
    }

    public static Task createTask(String operation) throws TaskException {
        Task task = null;

        if (!TaskRegistry.isExistingOperation(operation)) {
            throw new TaskException("Invalid operation");
        }

        task = TaskRegistry.getTaskForOperation(operation);

        long taskId = TASK_ID_GENERATOR.getAndIncrement();
        String taskName = "Task-" + taskId;
        task.setTaskName(taskName);
        task.setTaskStatus(TaskStatus.NEW);
        completionService.submit(task, null);
        TASK_ID_TASK_MAP.put(taskId, task);

        return task;
    }

    public static void trackTasks() {
        trackerThread = new Thread(() -> {
            while (isTaskControllerRunning()) {
                try {
                    Future<Void> completed = completionService.take();
                    completed.get(); // handle exception if needed
                } catch (InterruptedException | ExecutionException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Tracker Thread");

        trackerThread.start();

        pausedTasksTracker = new Thread(() -> {
           while (isTaskControllerRunning()) {
               try {
                   Thread.sleep(PAUSE_TASK_TRACKER_INTERVAL);
                   pausedTasks.removeIf(task -> task.getTaskStatus() != TaskStatus.PAUSED);
               } catch (InterruptedException e) {
                   Thread.currentThread().interrupt();
                   break;
               }
           }
        }, "Pause Task Tracker");

        pausedTasksTracker.start();
    }


    public static void quit() {
        shutdownExecutor(false);
    }

    public static void quitNow() {
        shutdownExecutor(true);
    }

    private static void shutdownExecutor(boolean force) {
        if (isTaskControllerRunning()) {
            taskControllerRunning.set(false);
            if (force) {
                executorService.shutdownNow();
            } else {
                executorService.shutdown();
            }

            resumeTaskScheduleService.shutdownNow();

            for (Thread t : new Thread[]{trackerThread, pausedTasksTracker}) {
                if (t != null && t.isAlive()) {
                    t.interrupt();
                }
            }
        }
    }

    public static Task getTaskById(long taskId) {
        return TASK_ID_TASK_MAP.get(taskId);
    }

    public static boolean isTaskControllerRunning() {
        return taskControllerRunning.get();
    }

    public static void pauseTaskForDuration(Task task, long duration) throws TaskException {
        pauseTask(task);
        resumeTaskScheduleService.schedule(() -> {
            try {
                resumeTask(task);
            } catch (TaskException e) {
                throw new RuntimeException(e);
            }
        }, duration, TimeUnit.SECONDS);
    }

    public static void pauseTask(Task task) throws TaskException {
        if (task.getTaskStatus() == TaskStatus.RUNNING) {
            task.pauseTask();
            pausedTasks.add(task);
        } else {
            throw new TaskException(task.getTaskName() + " is not in running status");
        }
    }

    public static void resumeTask(Task task) throws TaskException {
        if (task.getTaskStatus() == TaskStatus.PAUSED) {
            task.resumeTask();
            pausedTasks.remove(task);
        } else {
            throw new TaskException(task.getTaskName() + " is not paused");
        }
    }
}
