package org.prajwal.task.properties;

public class FileWriterTaskProperties extends FileTaskProperties {
    public FileWriterTaskProperties(String fileName, int fileSize) {
        super(fileName, fileSize);
    }

    @Override
    public String getDisplayString() {
        return "File Writer Task: \n" + super.getDisplayString();
    }
}
