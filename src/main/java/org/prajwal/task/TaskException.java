package org.prajwal.task;

public class TaskException extends Exception {
    public TaskException(Throwable e) {
        super(e);
    }

    public TaskException(Throwable e, String msg) {
        super(msg, e);
    }

    public TaskException() {
        super();
    }
}
