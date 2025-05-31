package org.prajwal.task.registry;

import org.prajwal.task.Task;
import org.prajwal.task.TaskException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class TaskRegistry {
    private static final Map<String, String> TASK_TO_CLASS_MAP = new HashMap<>();

    static {
        TASK_TO_CLASS_MAP.put("read file", "org.prajwal.task.impl.FileReaderTask");
        TASK_TO_CLASS_MAP.put("write file", "org.prajwal.task.impl.FileWriterTask");
        TASK_TO_CLASS_MAP.put("task statistics", "org.prajwal.task.impl.TaskStatistics");
    }

    public static String getClassPath(String operation) {
        return TASK_TO_CLASS_MAP.get(operation);
    }

    public static boolean isExistingOperation(String operation) {
        return TASK_TO_CLASS_MAP.containsKey(operation);
    }

    public static Task getTaskForOperation(String operation) throws TaskException {
        try {
            Class<?> taskClass = Class.forName(TaskRegistry.getClassPath(operation));
            return (Task) taskClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new TaskException(e);
        }
    }
}
