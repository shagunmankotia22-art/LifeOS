
package com.lifeos.service;

import com.lifeos.dao.TaskDAO;
import com.lifeos.model.Task;

import java.util.List;

public class TaskService {

    private final TaskDAO taskDAO = new TaskDAO();

    // 1. Create a task
    public boolean createTask(Task task) {

        if (task.getTitle() == null ||
            task.getTitle().trim().isEmpty()) {

            System.out.println("Task title cannot be empty.");
            return false;
        }

        if (!isValidPriority(task.getPriority())) {
            System.out.println("Invalid priority.");
            return false;
        }

        return taskDAO.createTask(task);
    }

    // 2. View all tasks
    public List<Task> viewTasks(int userId) {
        return taskDAO.getTasksByUser(userId);
    }

    // 3. Update a task
    public boolean updateTask(Task task) {

        if (task.getTitle() == null ||
            task.getTitle().trim().isEmpty()) {

            System.out.println("Task title cannot be empty.");
            return false;
        }

        if (!isValidPriority(task.getPriority())) {
            System.out.println("Invalid priority.");
            return false;
        }

        return taskDAO.updateTask(task);
    }

    // 4. Delete a task
    public boolean deleteTask(int taskId, int userId) {
        return taskDAO.deleteTask(taskId, userId);
    }

    // 5. Mark task completed
    public boolean markCompleted(int taskId, int userId) {
        return taskDAO.markCompleted(taskId, userId);
    }

    // 6. Search tasks
    public List<Task> searchTasks(int userId, String keyword) {
        return taskDAO.searchTasks(userId, keyword);
    }

    // 7. Filter by status
    public List<Task> filterByStatus(int userId, String status) {

        if (!status.equals("PENDING") &&
            !status.equals("IN_PROGRESS") &&
            !status.equals("COMPLETED")) {

            return List.of();
        }

        return taskDAO.filterTasks(userId, "status", status);
    }

    // 8. Filter by priority
    public List<Task> filterByPriority(int userId, String priority) {

        if (!isValidPriority(priority)) {
            return List.of();
        }

        return taskDAO.filterTasks(userId, "priority", priority);
    }

    // 9. Sort by deadline
    public List<Task> sortByDeadline(int userId) {
        return taskDAO.sortTasks(userId, "due_date");
    }

    // 10. Sort by priority
    public List<Task> sortByPriority(int userId) {
        return taskDAO.sortTasks(userId, "priority");
    }

    // 11. Get weekly completed tasks
    public int getWeeklyCompletedTasks(int userId) {
        return taskDAO.getWeeklyCompletedTasks(userId);
    }

    // 12. Validate priority
    private boolean isValidPriority(String priority) {

        return priority != null &&
            (priority.equals("HIGH") ||
             priority.equals("MEDIUM") ||
             priority.equals("LOW"));
    }
}

