package org.prajwal.task.properties;

public class FileTaskProperties implements TaskProperties {
    private String fileName;
    private int fileSize;

    public FileTaskProperties(String fileName, int fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getDisplayString() {
        return "1.\tfile name: " + fileName + "\n2.\tfile size: " + fileSize;
    }
}
