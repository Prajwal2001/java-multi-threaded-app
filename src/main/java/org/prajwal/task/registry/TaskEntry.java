package org.prajwal.task.registry;

import org.prajwal.task.properties.TaskProperties;

public record TaskEntry(
                String operation,
                String classPath,
                TaskProperties properties) {
}
