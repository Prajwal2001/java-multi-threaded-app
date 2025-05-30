package org.prajwal.task.registry;

import java.util.HashMap;
import java.util.Map;

public final class TaskRegistry {
    private static final Map<String, String> TASK_TO_CLASS_MAP = new HashMap<>();

    static {
        TASK_TO_CLASS_MAP.put("read file", "org.prajwal.task.impl.FileReaderTask");
        TASK_TO_CLASS_MAP.put("write file", "org.prajwal.task.impl.FileWriterTask");
    }

    public static String getClassPath(String operation) {
        return TASK_TO_CLASS_MAP.get(operation);
    }

    public static boolean existingTask(String operation) {
        return TASK_TO_CLASS_MAP.containsKey(operation);
    }
}
