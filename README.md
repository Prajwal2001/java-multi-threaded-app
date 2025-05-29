# Test Task Manager

A Java console application for managing and tracking asynchronous file read/write tasks, with support for pausing, resuming, and monitoring task status. All actions are logged with timestamps and thread information.

## Features

- Start file read or write tasks asynchronously.
- Pause and resume individual tasks for a specified duration.
- Check the status of any task by its ID.
- Graceful and immediate application shutdown.
- Logs all actions to a daily log file (e.g., `log-2025-05-29.log`).
- Console menu with ASCII table UI.

## Project Structure

```
src/
  main/
    java/
      org/
        prajwal/
          Main.java
          log/
            Log.java
          task/
            AbstractTask.java
            Task.java
            TaskException.java
            TaskStatus.java
            factory/
              TaskController.java
            impl/
              FileReaderTask.java
              FileWriterTask.java
```

## Getting Started

### Prerequisites

- Java 22 or higher
- Maven 3.x

### Build

To build the project and generate the deliverable JAR:

```sh
mvn clean package
```

The output JAR will be in `target/deliverables/test-1.0-SNAPSHOT.jar`.

### Run

From the project root, run:

```sh
java -jar target/deliverables/test-1.0-SNAPSHOT.jar
```

### Usage

On startup, you'll see a menu like:

```
+------+------------------------------+-----+
| Sl no| Description                  | Key |
+------+------------------------------+-----+
| 1    | Perform file read            | 1   |
+------+------------------------------+-----+
| 2    | Perform file write           | 2   |
+------+------------------------------+-----+
| 3    | Pause particular task        | 3   |
+------+------------------------------+-----+
| 4    | Check particular task status | 4   |
+------+------------------------------+-----+
| 5    | Quit                         | 0   |
+------+------------------------------+-----+
```

- **1**: Start a file read task.
- **2**: Start a file write task.
- **3**: Pause a running task by entering its ID and pause duration (in seconds).
- **4**: (Not implemented in current code, placeholder in menu.)
- **0**: Quit the application immediately.

All actions are logged in a file named `log-YYYY-MM-DD.log` in the project root.

## Code Overview

- `Main`: Entry point, handles user interaction and menu.
- `TaskController`: Manages task creation, tracking, and shutdown.
- `AbstractTask`: Base class for tasks, implements pause/resume logic.
- `FileReaderTask`, `FileWriterTask`: Example tasks simulating file operations.
- `Log`: Handles thread-safe logging to daily log files.

## Logging

All logs are written to a file named `log-YYYY-MM-DD.log` in the project root. Each log entry includes a timestamp and the thread name.

## Dependencies

- [de.vandermeer:asciitable](https://mvnrepository.com/artifact/de.vandermeer/asciitable): For ASCII table rendering in the console.
- [JUnit 5](https://junit.org/junit5/) and [Mockito](https://site.mockito.org/): For testing (test scope).

## License

This project is for educational/demo purposes.

---

**Author:** Prajwal

For questions or issues, please open an issue in your repository.
