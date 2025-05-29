package org.prajwal.task;

public interface Task extends Runnable {

    String getTaskName();

    void setTaskName(String taskName);

    void pauseTask();

    void resumeTask();
}
