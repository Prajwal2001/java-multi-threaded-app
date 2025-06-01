package org.prajwal.task;

import org.prajwal.log.Log;
import org.prajwal.task.properties.TaskProperties;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractTask implements Task {
    protected TaskProperties taskProperties;
    protected String taskName;
    private final Object pauseLock = new Object();
    private volatile boolean paused = false;
    private final AtomicReference<TaskStatus> taskStatusAtomicReference = new AtomicReference<>();

    @Override
    public String getTaskName() {
        return this.taskName;
    }

    @Override
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    protected void checkPaused() {
        synchronized (pauseLock) {
            while (paused) {
                try {
                    pauseLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    @Override
    public void pauseTask() {
        paused = true;
        Log.log("Pausing task " + this.getTaskName() + " at " + LocalDateTime.now());
        setTaskStatus(TaskStatus.PAUSED);
    }

    @Override
    public void resumeTask() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
            Log.log("Resuming task " + this.getTaskName() + " at " + LocalDateTime.now());
        }
        setTaskStatus(TaskStatus.RUNNING);
    }

    @Override
    public void setTaskStatus(TaskStatus taskStatus) {
        taskStatusAtomicReference.set(taskStatus);
    }

    @Override
    public TaskStatus getTaskStatus() {
        return taskStatusAtomicReference.get();
    }

    @Override
    public void setTaskProperties(TaskProperties taskProperties) {
        this.taskProperties = taskProperties;
    }

    @Override
    public TaskProperties getTaskProperties() {
        return this.taskProperties;
    }

    @Override
    public final void run() {
        Thread.currentThread().setName(getTaskName());
        LocalDateTime taskStartDttm = LocalDateTime.now();
        Log.log("Task started at: " + taskStartDttm);
        setTaskStatus(TaskStatus.RUNNING);
        try {
            runTask(taskProperties);
            setTaskStatus(TaskStatus.COMPLETED);
        } catch (Exception e) {
            setTaskStatus(TaskStatus.FAILED);
            throw new RuntimeException(e);
        }
        LocalDateTime taskEndDttm = LocalDateTime.now();
        Log.log("Task completed at: " + taskEndDttm + ", taking " + Duration.between(taskStartDttm, taskEndDttm).toSeconds() + " seconds");
    }

    public abstract void runTask(TaskProperties taskProperties) throws TaskException;
}
