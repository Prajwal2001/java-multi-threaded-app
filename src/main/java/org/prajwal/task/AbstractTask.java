package org.prajwal.task;

import org.prajwal.log.Log;

import java.time.LocalDateTime;

public abstract class AbstractTask implements Task {
    protected String taskName;
    private final Object pauseLock = new Object();
    private volatile boolean paused = false;

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
    }

    @Override
    public void resumeTask() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
            Log.log("Resuming task " + this.getTaskName() + " at " + LocalDateTime.now());
        }

    }

    @Override
    public final void run() {
        Thread.currentThread().setName(getTaskName());
        Log.log("Task started at: " + LocalDateTime.now());
        runTask();
        Log.log("Task completed at: " + LocalDateTime.now());
    }

    public abstract void runTask();
}
