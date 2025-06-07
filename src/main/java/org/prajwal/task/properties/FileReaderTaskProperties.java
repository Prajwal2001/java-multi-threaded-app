package org.prajwal.task.properties;

public class FileReaderTaskProperties extends FileTaskProperties {

    public FileReaderTaskProperties(String fileName, int fileSize) {
        super(fileName, fileSize);
    }

    @Override
    public String getDisplayString() {
        return "File Reader Task: \n" + super.getDisplayString();
    }
}
