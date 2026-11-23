
package com.lifeos.dao;

import com.lifeos.database.DBConnection;
import com.lifeos.model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    // 1. Create a new task
    public boolean createTask(Task task) {

        String sql = "INSERT INTO tasks "
                + "(user_id, title, description, priority, status, due_date) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, task.getUserId());
            ps.setString(2, task.getTitle());
            ps.setString(3, task.getDescription());
            ps.setString(4, task.getPriority());
            ps.setString(5, task.getStatus());
            ps.setDate(6, task.getDueDate());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Get all tasks of a user
    public List<Task> getTasksByUser(int userId) {

        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapTask(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    // 3. Update a task
    public boolean updateTask(Task task) {

        String sql = "UPDATE tasks SET title = ?, description = ?, "
                + "priority = ?, status = ?, due_date = ? "
                + "WHERE task_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getPriority());
            ps.setString(4, task.getStatus());
            ps.setDate(5, task.getDueDate());
            ps.setInt(6, task.getTaskId());
            ps.setInt(7, task.getUserId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Delete a task
    public boolean deleteTask(int taskId, int userId) {

        String sql = "DELETE FROM tasks "
                + "WHERE task_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Search tasks
    public List<Task> searchTasks(int userId, String keyword) {

        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT * FROM tasks WHERE user_id = ? "
                + "AND (title LIKE ? OR description LIKE ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapTask(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    // 6. Filter tasks
    public List<Task> filterTasks(int userId, String column, String value) {

        List<Task> tasks = new ArrayList<>();

        if (!column.equals("status") && !column.equals("priority")) {
            return tasks;
        }

        String sql = "SELECT * FROM tasks WHERE user_id = ? AND "
                + column + " = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, value);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapTask(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    // 7. Sort tasks
    public List<Task> sortTasks(int userId, String sortBy) {

        List<Task> tasks = new ArrayList<>();

        if (!sortBy.equals("due_date") && !sortBy.equals("priority")) {
            return tasks;
        }

        String sql = "SELECT * FROM tasks WHERE user_id = ? ORDER BY "
                + sortBy;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapTask(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    // 8. Mark task as completed and save completion date
    public boolean markCompleted(int taskId, int userId) {

        String sql = "UPDATE tasks SET status = 'COMPLETED', "
                + "completed_at = CURDATE() "
                + "WHERE task_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 9. Get tasks completed this week (Monday to today)
    public int getWeeklyCompletedTasks(int userId) {

        String sql = "SELECT COUNT(*) FROM tasks "
                + "WHERE user_id = ? "
                + "AND status = 'COMPLETED' "
                + "AND completed_at >= DATE_SUB(CURDATE(), "
                + "INTERVAL WEEKDAY(CURDATE()) DAY) "
                + "AND completed_at <= CURDATE()";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // 10. Convert database record into Task object
    private Task mapTask(ResultSet rs) throws SQLException {

        return new Task(
            rs.getInt("task_id"),
            rs.getInt("user_id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getString("priority"),
            rs.getString("status"),
            rs.getDate("due_date")
        );
    }
}
