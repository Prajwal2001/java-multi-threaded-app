package org.prajwal.task;

import org.prajwal.task.properties.TaskProperties;

public interface Task extends Runnable {

    String getTaskName();

    void setTaskName(String taskName);

    void pauseTask();

    void resumeTask();

    void setTaskStatus(TaskStatus taskStatus);

    TaskStatus getTaskStatus();

    void setTaskProperties(TaskProperties taskProperties);

    TaskProperties getTaskProperties();
}
