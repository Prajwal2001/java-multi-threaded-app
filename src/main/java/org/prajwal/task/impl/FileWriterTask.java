package org.prajwal.task.impl;

import org.prajwal.log.Log;
import org.prajwal.task.AbstractTask;
import org.prajwal.task.TaskException;
import org.prajwal.task.properties.FileWriterTaskProperties;
import org.prajwal.task.properties.TaskProperties;

public class FileWriterTask extends AbstractTask {

    @Override
    public void runTask(TaskProperties taskProperties) throws TaskException {
        int length = ((FileWriterTaskProperties) taskProperties).getFileSize();

        for (int i = 1; i <= length; i++) {
            try {
                checkPaused();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new TaskException(e);
            }
            Log.log("Writing to a file... (" + i + ")");
        }
    }
}
