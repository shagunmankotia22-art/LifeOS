package com.lifeos.menu;

import com.lifeos.model.Task;
import com.lifeos.service.TaskService;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class TaskMenu {

    private final TaskService taskService = new TaskService();
    private final Scanner sc;

    public TaskMenu(Scanner sc) {
        this.sc = sc;
    }

    public void showMenu(int userId) {

        while (true) {
            System.out.println("\n===== TASK MANAGEMENT =====");
            System.out.println("1. Create Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Update Task");
            System.out.println("4. Delete Task");
            System.out.println("5. Mark Task Completed");
            System.out.println("6. Search Tasks");
            System.out.println("7. Filter Tasks");
            System.out.println("8. Sort Tasks");
            System.out.println("9. Back");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> createTask(userId);
                case 2 -> displayTasks(taskService.viewTasks(userId));
                case 3 -> updateTask(userId);
                case 4 -> deleteTask(userId);
                case 5 -> markCompleted(userId);
                case 6 -> searchTasks(userId);
                case 7 -> filterTasks(userId);
                case 8 -> sortTasks(userId);
                case 9 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void createTask(int userId) {
        System.out.print("Title: ");
        String title = sc.nextLine();

        System.out.print("Description: ");
        String description = sc.nextLine();

        System.out.print("Priority (HIGH/MEDIUM/LOW): ");
        String priority = sc.nextLine().toUpperCase();

        System.out.print("Due date (YYYY-MM-DD, or leave blank): ");
        String dateInput = sc.nextLine();

        Date dueDate = dateInput.isBlank() ? null : Date.valueOf(dateInput);

        Task task = new Task(
            0, userId, title, description,
            priority, "PENDING", dueDate
        );

        if (taskService.createTask(task)) {
            System.out.println("Task created successfully!");
        } else {
            System.out.println("Task creation failed.");
        }
    }

    private void updateTask(int userId) {
        System.out.print("Enter Task ID: ");
        int taskId = sc.nextInt();
        sc.nextLine();

        System.out.print("New title: ");
        String title = sc.nextLine();

        System.out.print("New description: ");
        String description = sc.nextLine();

        System.out.print("New priority (HIGH/MEDIUM/LOW): ");
        String priority = sc.nextLine().toUpperCase();

        System.out.print("New status (PENDING/IN_PROGRESS/COMPLETED): ");
        String status = sc.nextLine().toUpperCase();

        System.out.print("New due date (YYYY-MM-DD, or leave blank): ");
        String dateInput = sc.nextLine();

        Date dueDate = dateInput.isBlank() ? null : Date.valueOf(dateInput);

        Task task = new Task(
            taskId, userId, title, description,
            priority, status, dueDate
        );

        if (taskService.updateTask(task)) {
            System.out.println("Task updated successfully!");
        } else {
            System.out.println("Task update failed.");
        }
    }

    private void deleteTask(int userId) {
        System.out.print("Enter Task ID: ");
        int taskId = sc.nextInt();
        sc.nextLine();

        if (taskService.deleteTask(taskId, userId)) {
            System.out.println("Task deleted successfully!");
        } else {
            System.out.println("Task deletion failed.");
        }
    }

    private void markCompleted(int userId) {
        System.out.print("Enter Task ID: ");
        int taskId = sc.nextInt();
        sc.nextLine();

        if (taskService.markCompleted(taskId, userId)) {
            System.out.println("Task marked as completed!");
        } else {
            System.out.println("Operation failed.");
        }
    }

    private void searchTasks(int userId) {
        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine();

        displayTasks(taskService.searchTasks(userId, keyword));
    }

    private void filterTasks(int userId) {
        System.out.println("1. Filter by Status");
        System.out.println("2. Filter by Priority");
        System.out.print("Enter choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            System.out.print("Status (PENDING/IN_PROGRESS/COMPLETED): ");
            String status = sc.nextLine().toUpperCase();
            displayTasks(taskService.filterByStatus(userId, status));
        } else if (choice == 2) {
            System.out.print("Priority (HIGH/MEDIUM/LOW): ");
            String priority = sc.nextLine().toUpperCase();
            displayTasks(taskService.filterByPriority(userId, priority));
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void sortTasks(int userId) {
        System.out.println("1. Sort by Deadline");
        System.out.println("2. Sort by Priority");
        System.out.print("Enter choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            displayTasks(taskService.sortByDeadline(userId));
        } else if (choice == 2) {
            displayTasks(taskService.sortByPriority(userId));
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void displayTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        System.out.println("\nID | Title | Priority | Status | Due Date");

        for (Task task : tasks) {
            System.out.println(
                task.getTaskId() + " | " +
                task.getTitle() + " | " +
                task.getPriority() + " | " +
                task.getStatus() + " | " +
                task.getDueDate()
            );
        }
    }
}