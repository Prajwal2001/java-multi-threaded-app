package org.prajwal;

import de.vandermeer.asciitable.*;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import org.prajwal.task.Task;
import org.prajwal.task.factory.TaskController;

import java.time.LocalDateTime;
import java.util.*;

public class Main {
    private static final List<List<String>> recordRows = new ArrayList<>() {{
        add(List.of("Perform file read", "1"));
        add(List.of("Perform file write", "2"));
        add(List.of("Pause particular task", "3"));
        add(List.of("Check particular task status", "4"));
        add(List.of("Quit", "0"));
    }};

    public static void main(String... args) throws Exception {

        System.out.println("Application started execution");
        TaskController.trackTasks();

        Scanner scanner = new Scanner(System.in);
        Timer timer = null;

        while (true) {
            printAsciiTable();
            System.out.print("Enter the key: ");
            int input = scanner.nextInt();

            if (input == 0) {
                TaskController.quitNow();
                if (timer != null) {
                    timer.cancel();
                }
                break;
            }

            if (input == 3) {
                startPausingTask(scanner, timer);
                continue;
            }

            String operation = (input == 1 ? "read" : "write") + " file";

            Task task = TaskController.createTask(operation);
            assert task != null;
            System.out.println("Created task " + task.getTaskName());
        }

        TaskController.quitNow();
        System.out.println("Application execution has ended");

    }

    private static void startPausingTask(Scanner scanner, Timer timer) {
        System.out.print("Enter the task ID to pause: ");
        long taskId = scanner.nextLong();
        Task task = TaskController.getTaskById(taskId);

        if (task == null) {
            System.out.println("Invalid task ID...");
        }

        System.out.print("Enter the pause duration in seconds: ");
        long duration = scanner.nextLong();

        System.out.println(task.getTaskName() + " will resume at: " + LocalDateTime.now().plusSeconds(duration));


        task.pauseTask();

        timer = new Timer("Pausing thread");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                task.resumeTask();
            }
        }, duration * 1000);
    }

    private static void printAsciiTable() {
        AsciiTable at = new AsciiTable();
        at.addRule();
        AT_Row headerRow = at.addRow("Sl no", "Description", "Key");
        headerRow.setTextAlignment(TextAlignment.CENTER);
        at.addRule();

        for (int i = 0; i < recordRows.size(); i++) {
            List<String> row = recordRows.get(i);
            AT_Row recordRow = at.addRow(String.valueOf(i + 1), row.get(0), row.get(1));
            recordRow.setPaddingLeft(1);
            at.addRule();
        }

        System.out.println(at.render());
    }
}