package org.prajwal.log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private static final PrintStream LOG_PRINT_STREAM;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        try {
            String logFileName = "log-" + LocalDate.now() + ".log";
            LOG_PRINT_STREAM = new PrintStream(new FileOutputStream(logFileName, true)); // append mode
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to initialize log file", e);
        }
    }

    public static synchronized void log(String msg) {
        String time = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String thread = Thread.currentThread().getName();
        LOG_PRINT_STREAM.printf("%s [%s]: %s%n", time, thread, msg);
    }
}
