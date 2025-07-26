package org.prajwal.task.registry;

import org.prajwal.task.Task;
import org.prajwal.task.TaskException;
import org.prajwal.task.properties.FileReaderTaskProperties;
import org.prajwal.task.properties.FileWriterTaskProperties;
import org.prajwal.task.properties.TaskProperties;
import org.prajwal.task.properties.TaskStatisticsTaskProperties;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class TaskRegistry {
    private static final Map<String, TaskEntry> operationToClassMap = new HashMap<>();

    static {
        operationToClassMap.put("read file", new TaskEntry("read file", "org.prajwal.task.impl.FileReaderTask", new FileReaderTaskProperties("", 40)));
        operationToClassMap.put("write file", new TaskEntry("write file", "org.prajwal.task.impl.FileWriterTask", new FileWriterTaskProperties("", 40)));
        operationToClassMap.put("task statistics", new TaskEntry("task statistics", "org.prajwal.task.impl.TaskStatisticsTask", new TaskStatisticsTaskProperties()));
    }

    public static String getClassPath(String operation) {
        return operationToClassMap.get(operation).classPath();
    }

    public static boolean isExistingOperation(String operation) {
        return operationToClassMap.containsKey(operation);
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

    public static String getTaskPropertiesForDisplay(String operation) {
        return getTaskProperties(operation).getDisplayString();
    }

    public static TaskProperties getTaskProperties(String operation) {
        return operationToClassMap.get(operation).properties();
    }
}
