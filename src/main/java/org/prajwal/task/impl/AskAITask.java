package org.prajwal.task.impl;

import org.prajwal.task.AbstractTask;
import org.prajwal.task.TaskException;
import org.prajwal.task.properties.TaskProperties;

public class AskAITask extends AbstractTask {
    @Override
    public void runTask(TaskProperties taskProperties) throws TaskException {
        final String API_KEY = System.getenv("API_KEY");

    }
}
