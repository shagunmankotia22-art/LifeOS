package com.lifeos.service;

import com.lifeos.dao.TaskDAO;
import com.lifeos.model.Task;

import java.util.List;

public class TaskService {

    private final TaskDAO taskDAO = new TaskDAO();

    public boolean createTask(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            System.out.println("Task title cannot be empty.");
            return false;
        }

        if (!isValidPriority(task.getPriority())) {
            System.out.println("Invalid priority.");
            return false;
        }

        return taskDAO.createTask(task);
    }

    public List<Task> viewTasks(int userId) {
        return taskDAO.getTasksByUser(userId);
    }

    public boolean updateTask(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            System.out.println("Task title cannot be empty.");
            return false;
        }

        if (!isValidPriority(task.getPriority())) {
            System.out.println("Invalid priority.");
            return false;
        }

        return taskDAO.updateTask(task);
    }

    public boolean deleteTask(int taskId, int userId) {
        return taskDAO.deleteTask(taskId, userId);
    }

    public boolean markCompleted(int taskId, int userId) {
        return taskDAO.markCompleted(taskId, userId);
    }

    public List<Task> searchTasks(int userId, String keyword) {
        return taskDAO.searchTasks(userId, keyword);
    }

    public List<Task> filterByStatus(int userId, String status) {
        if (!status.equals("PENDING") &&
            !status.equals("IN_PROGRESS") &&
            !status.equals("COMPLETED")) {
            return List.of();
        }

        return taskDAO.filterTasks(userId, "status", status);
    }

    public List<Task> filterByPriority(int userId, String priority) {
        if (!isValidPriority(priority)) {
            return List.of();
        }

        return taskDAO.filterTasks(userId, "priority", priority);
    }

    public List<Task> sortByDeadline(int userId) {
        return taskDAO.sortTasks(userId, "due_date");
    }

    public List<Task> sortByPriority(int userId) {
        return taskDAO.sortTasks(userId, "priority");
    }

    private boolean isValidPriority(String priority) {
        return priority != null &&
            (priority.equals("HIGH") ||
             priority.equals("MEDIUM") ||
             priority.equals("LOW"));
    }
}