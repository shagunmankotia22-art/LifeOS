

package com.lifeos.service;

import com.lifeos.dao.HabitDAO;
import com.lifeos.model.Habit;

import java.util.List;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class HabitService {

    private final HabitDAO habitDAO = new HabitDAO();

    // 1. Add a new habit
    public boolean createHabit(Habit habit) {

        if (habit.getHabitName() == null ||
            habit.getHabitName().trim().isEmpty()) {

            System.out.println("Habit name cannot be empty.");
            return false;
        }

        return habitDAO.createHabit(habit);
    }

    // 2. View all habits
    public List<Habit> viewHabits(int userId) {
        return habitDAO.getHabitsByUser(userId);
    }

    // 3. Mark a habit as completed
    public boolean markHabitCompleted(int habitId) {
        return habitDAO.markHabitCompleted(habitId);
    }

    // 4. View habit completion history
    public List<Date> viewCompletionHistory(int habitId) {
        return habitDAO.getCompletionHistory(habitId);
    }

    // 5. Delete a habit
    public boolean deleteHabit(int habitId, int userId) {
        return habitDAO.deleteHabit(habitId, userId);
    }

    // 6. Calculate daily habit streak
    public int calculateDailyStreak(int habitId) {

        List<Date> history =
            habitDAO.getCompletionHistory(habitId);

        Set<LocalDate> completedDates = new HashSet<>();

        for (Date date : history) {
            completedDates.add(date.toLocalDate());
        }

        LocalDate today = LocalDate.now();

        // If not completed today, check from yesterday
        if (!completedDates.contains(today)) {
            today = today.minusDays(1);
        }

        int streak = 0;

        // Count consecutive completed days
        while (completedDates.contains(today)) {
            streak++;
            today = today.minusDays(1);
        }

        return streak;
    }
 // 7. Calculate habit completion percentage
    public double calculateCompletionPercentage(int habitId) {
        return habitDAO.getCompletionPercentage(habitId);
    }
}
