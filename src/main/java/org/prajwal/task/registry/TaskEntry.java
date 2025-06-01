package org.prajwal.task.registry;

import org.prajwal.task.properties.TaskProperties;

import java.util.Map;

public record TaskEntry(
        String operation,
        String classPath,
        TaskProperties properties) {}
