package org.prajwal;

import org.prajwal.task.Task;
import org.prajwal.task.factory.TaskFactory;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String... args) throws Exception {

        System.out.println("Application started execution");
        TaskFactory.trackTasks();

        Scanner scanner = new Scanner(System.in);
        Timer timer = null;

        while (true) {

            System.out.print("Enter 1 for read, 2 for write, 0 to quit and 3 to pause: ");
            int input = scanner.nextInt();

            if (input == 0) {
                TaskFactory.quitNow();
                if (timer != null) {
                    timer.cancel();
                }
                break;
            }

            if (input == 3) {
                System.out.print("Enter the task ID to pause: ");
                long taskId = scanner.nextLong();
                Task task = TaskFactory.getTaskById(taskId);

                System.out.print("Enter the pause duration in seconds: ");
                long duration = scanner.nextLong();

                System.out.println(task.getTaskName() + " will resume at: " + LocalDateTime.now().plusSeconds(duration));

                if (task == null) {
                    System.out.println("Invalid task ID...");
                    continue;
                }

                task.pauseTask();

                timer = new Timer("Pausing thread");
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        task.resumeTask();
                    }
                }, duration * 1000);
                continue;
            }

            String operation = (input == 1 ? "read" : "write") + " file";

            Task task = TaskFactory.createTask(operation);
            assert task != null;
            System.out.println("Created task " + task.getTaskName());
        }

        TaskFactory.quitNow();
        System.out.println("Application execution has ended");

    }
}