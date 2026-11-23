
package com.lifeos.dao;

import com.lifeos.database.DBConnection;
import com.lifeos.model.Habit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitDAO {

    // 1. Add a new habit
    public boolean createHabit(Habit habit) {

        String sql = "INSERT INTO habits (user_id, habit_name, description) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, habit.getUserId());
            ps.setString(2, habit.getHabitName());
            ps.setString(3, habit.getDescription());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Display all habits of a user
    public List<Habit> getHabitsByUser(int userId) {

        List<Habit> habits = new ArrayList<>();

        String sql = "SELECT * FROM habits WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    habits.add(new Habit(
                        rs.getInt("habit_id"),
                        rs.getInt("user_id"),
                        rs.getString("habit_name"),
                        rs.getString("description")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return habits;
    }

    // 3. Mark a habit as completed
    public boolean markHabitCompleted(int habitId) {

        String sql = "INSERT INTO habit_records (habit_id, completion_date, completed) VALUES (?, CURDATE(), TRUE)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, habitId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Get habit completion history
    public List<Date> getCompletionHistory(int habitId) {

        List<Date> history = new ArrayList<>();

        String sql = "SELECT completion_date FROM habit_records WHERE habit_id = ? AND completed = TRUE ORDER BY completion_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, habitId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    history.add(rs.getDate("completion_date"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return history;
    }

    // 5. Delete a habit
    public boolean deleteHabit(int habitId, int userId) {

        String sql = "DELETE FROM habits WHERE habit_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, habitId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 // 6. Calculate habit completion percentage
    public double getCompletionPercentage(int habitId) {

        String sql = """
            SELECT
                COUNT(*) AS completed_days,
                DATEDIFF(CURDATE(), h.created_at) + 1 AS total_days
            FROM habits h
            LEFT JOIN habit_records hr
                ON h.habit_id = hr.habit_id
                AND hr.completed = TRUE
            WHERE h.habit_id = ?
            GROUP BY h.habit_id
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, habitId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    int completedDays = rs.getInt("completed_days");
                    int totalDays = rs.getInt("total_days");

                    if (totalDays > 0) {
                        return (completedDays * 100.0) / totalDays;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }
}
