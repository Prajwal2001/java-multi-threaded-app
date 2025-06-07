package org.prajwal;

import de.vandermeer.asciitable.*;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import org.prajwal.task.Task;
import org.prajwal.task.TaskException;
import org.prajwal.task.controller.TaskController;

import java.time.LocalDateTime;
import java.util.*;

public class Main {
    private static final List<List<String>> recordRows = new ArrayList<>() {
        {
            add(List.of("Create tasks", "1"));
            add(List.of("Pause task", "2"));
            add(List.of("Print task status", "3"));
            add(List.of("Resume task", "4"));
            add(List.of("Quit", "0"));
        }
    };
    private static final Scanner scanner = new Scanner(System.in);
    public static final int USER_INPUT_DELAY = 1000;

    public static void main(String... args) throws Exception {

        System.out.println("Application started execution");
        TaskController.trackTasks();

        while (TaskController.isTaskControllerRunning()) {
            int input = getUserInput();

            switch (input) {
                case 0:
                    TaskController.quitNow();
                    break;
                case 1:
                    createTaskBasedOnInput();
                    break;
                case 2:
                    pauseTask();
                    break;
                case 3:
                    printTaskStatus();
                    break;
                case 4:
                    resumeTask();
                    break;
                default:
                    System.out.println("Invalid key! Please enter again");
            }
            Thread.sleep(USER_INPUT_DELAY);
        }

        System.out.println("Application execution has ended");
        scanner.close();
    }

    private static int getUserInput() {
        printAsciiTable();
        System.out.print("Enter the key: ");
        return scanner.nextInt();
    }

    private static void createTaskBasedOnInput() throws TaskException {
        System.out.print("Enter 1 for file read, 2 to write to a file and 3 to get all tasks statistics: ");
        int taskType = scanner.nextInt();

        String operation = "";
        switch (taskType) {
            case 1:
                operation = "read file";
                break;
            case 2:
                operation = "write file";
                break;
            case 3:
                operation = "task statistics";
                break;
            default:
                System.out.println("Invalid task type");
        }

        Task task = TaskController.createTask(operation);
        System.out.println("Created task " + task.getTaskName());
    }

    private static void pauseTask() {
        System.out.print("Enter the task ID: ");
        long taskId = scanner.nextLong();
        final Task task = TaskController.getTaskById(taskId);

        if (task == null) {
            System.out.println("Invalid task ID");
            return;
        }

        System.out.print("Enter 1 for indefinite pause and 2 for definite duration pause: ");
        int pauseChoice = scanner.nextInt();

        if (pauseChoice == 1) {
            try {
                TaskController.pauseTask(task);
            } catch (TaskException e) {
                System.out.println(e.getMessage());
            }
        } else if (pauseChoice == 2) {
            System.out.print("Enter the pause duration in seconds: ");
            long duration = scanner.nextLong();

            System.out.println(task.getTaskName() + " will resume at: " + LocalDateTime.now().plusSeconds(duration));

            try {
                TaskController.pauseTaskForDuration(task, duration);
            } catch (TaskException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid pause choice!");
        }
    }

    private static void printTaskStatus() {
        System.out.print("Enter the task ID: ");
        long taskId = scanner.nextLong();
        Task task = TaskController.getTaskById(taskId);

        if (task == null) {
            System.out.println("Invalid task ID");
            return;
        }

        System.out.println("Status of " + task.getTaskName() + " is " + task.getTaskStatus());
    }

    private static void resumeTask() {
        System.out.print("Enter the task ID: ");
        long taskId = scanner.nextLong();
        Task task = TaskController.getTaskById(taskId);

        if (task == null) {
            System.out.println("Invalid task ID");
            return;
        }

        task.resumeTask();
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