package org.prajwal.task.impl;

import org.prajwal.log.Log;
import org.prajwal.task.AbstractTask;
import org.prajwal.task.TaskException;

public class FileWriterTask extends AbstractTask {

    @Override
    public void runTask() throws TaskException {
        for (int i = 1; i <= 20; i++) {
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
