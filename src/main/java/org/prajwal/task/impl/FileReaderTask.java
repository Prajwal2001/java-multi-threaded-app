package org.prajwal.task.impl;

import org.prajwal.log.Log;
import org.prajwal.task.AbstractTask;
import org.prajwal.task.TaskException;
import org.prajwal.task.properties.FileReaderTaskProperties;
import org.prajwal.task.properties.TaskProperties;
import org.prajwal.task.registry.TaskRegistry;

import java.util.Map;

public class FileReaderTask extends AbstractTask {

    @Override
    public void runTask(TaskProperties taskProperties) throws TaskException {


        int length = ((FileReaderTaskProperties) taskProperties).getFileSize();

        for (int i = 1; i <= length; i++) {
            try {
                checkPaused();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new TaskException(e);
            }
            Log.log("Reading file... (" + i + ")");
        }
    }
}
