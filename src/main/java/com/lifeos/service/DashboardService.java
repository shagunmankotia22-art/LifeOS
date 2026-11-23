
package com.lifeos.service;

import com.lifeos.model.Task;
import com.lifeos.model.Habit;

import java.util.List;

public class DashboardService {

    private final TaskService taskService = new TaskService();
    private final HabitService habitService = new HabitService();

    public void showDashboard(int userId) {

        List<Task> tasks = taskService.viewTasks(userId);

        int totalTasks = tasks.size();
        int completedTasks = 0;
        int pendingTasks = 0;

        for (Task task : tasks) {
            if ("COMPLETED".equalsIgnoreCase(task.getStatus())) {
                completedTasks++;
            } else if ("PENDING".equalsIgnoreCase(task.getStatus())) {
                pendingTasks++;
            }
        }

        double productivity = 0;

        if (totalTasks > 0) {
            productivity = (completedTasks * 100.0) / totalTasks;
        }

        int weeklyCompletedTasks =
                taskService.getWeeklyCompletedTasks(userId);

        List<Habit> habits = habitService.viewHabits(userId);

        double totalHabitPercentage = 0;

        for (Habit habit : habits) {
            totalHabitPercentage +=
                    habitService.calculateCompletionPercentage(
                            habit.getHabitId()
                    );
        }

        double averageHabitPercentage = 0;

        if (!habits.isEmpty()) {
            averageHabitPercentage =
                    totalHabitPercentage / habits.size();
        }

        System.out.println("\n===== PRODUCTIVITY DASHBOARD =====");
        System.out.println("Total Tasks: " + totalTasks);
        System.out.println("Completed Tasks: " + completedTasks);
        System.out.println("Pending Tasks: " + pendingTasks);

        System.out.printf(
                "Overall Productivity: %.2f%%%n",
                productivity
        );

        System.out.println("\n===== WEEKLY PRODUCTIVITY REPORT =====");
        System.out.println(
                "Tasks Completed This Week: " + weeklyCompletedTasks
        );

        System.out.println("\n===== HABIT STATISTICS =====");
        System.out.println("Total Habits: " + habits.size());

        System.out.printf(
                "Average Habit Completion: %.2f%%%n",
                averageHabitPercentage
        );

        System.out.println("=================================");
    }
}
