package org.prajwal.task.factory;

import org.prajwal.task.Task;
import org.prajwal.task.TaskException;
import org.prajwal.task.TaskStatus;
import org.prajwal.task.impl.FileReaderTask;
import org.prajwal.task.impl.FileWriterTask;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class TaskController {
    private static final AtomicLong TASK_ID_GENERATOR = new AtomicLong(1);
    private static final Map<Long, Task> TASK_ID_TASK_MAP = new ConcurrentHashMap<>();
    private static final AtomicBoolean applicationRunning = new AtomicBoolean(true);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final CompletionService<Void> completionService = new ExecutorCompletionService<>(executorService);
    private static Thread trackerThread;

    private TaskController() {
    }

    public static Task createTask(String operation) throws TaskException {
        Task task = null;

        if (operation.equals("write file")) {
            task = new FileWriterTask();
        } else if (operation.equals("read file")) {
            task = new FileReaderTask();
        }

        if (task == null) {
            return null;
        }

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
            while (applicationRunning.get()) {
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
    }


    public static void quit() {
        shutdownExecutor(false);
    }

    public static void quitNow() {
        shutdownExecutor(true);
    }

    private static void shutdownExecutor(boolean force) {
        applicationRunning.set(false);
        if (force) {
            executorService.shutdownNow();
        } else {
            executorService.shutdown();
        }
        if (trackerThread != null && trackerThread.isAlive()) {
            trackerThread.interrupt();
        }
    }

    public static Task getTaskById(long taskId) {
        return TASK_ID_TASK_MAP.get(taskId);
    }

}
