
package com.lifeos.menu;

import com.lifeos.model.Habit;
import com.lifeos.service.HabitService;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class HabitMenu {

    private final HabitService habitService = new HabitService();
    private final Scanner sc;

    public HabitMenu(Scanner sc) {
        this.sc = sc;
    }

    public void showMenu(int userId) {

        int choice = -1;

        do {
            System.out.println("\n===== HABIT TRACKER =====");
            System.out.println("1. Add Habit");
            System.out.println("2. View Habits");
            System.out.println("3. Mark Habit Completed");
            System.out.println("4. View Completion History");
            System.out.println("5. Delete Habit");
            System.out.println("6. View Daily Streak");
            System.out.println("7. View Completion Percentage");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
                choice = -1;
                continue;
            }

            switch (choice) {

                case 1:
                    addHabit(userId);
                    break;

                case 2:
                    viewHabits(userId);
                    break;

                case 3:
                    markCompleted();
                    break;

                case 4:
                    viewHistory();
                    break;

                case 5:
                    deleteHabit(userId);
                    break;

                case 6:
                    viewDailyStreak();
                    break;

                case 7:
                    viewCompletionPercentage();
                    break;

                case 0:
                    System.out.println("Returning to Main Menu...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    private void addHabit(int userId) {

        System.out.print("Enter habit name: ");
        String name = sc.nextLine();

        System.out.print("Enter description: ");
        String description = sc.nextLine();

        Habit habit = new Habit(0, userId, name, description);

        if (habitService.createHabit(habit)) {
            System.out.println("Habit added successfully!");
        } else {
            System.out.println("Could not add habit.");
        }
    }

    private void viewHabits(int userId) {

        List<Habit> habits = habitService.viewHabits(userId);

        if (habits.isEmpty()) {
            System.out.println("No habits found.");
            return;
        }

        for (Habit habit : habits) {
            System.out.println(
                "ID: " + habit.getHabitId() +
                " | Name: " + habit.getHabitName() +
                " | Description: " + habit.getDescription()
            );
        }
    }

    private void markCompleted() {

        System.out.print("Enter habit ID: ");
        int habitId = Integer.parseInt(sc.nextLine());

        if (habitService.markHabitCompleted(habitId)) {
            System.out.println("Habit marked as completed!");
        } else {
            System.out.println(
                "Could not mark habit completed. It may already be completed today."
            );
        }
    }

    private void viewHistory() {

        System.out.print("Enter habit ID: ");
        int habitId = Integer.parseInt(sc.nextLine());

        List<Date> history =
            habitService.viewCompletionHistory(habitId);

        if (history.isEmpty()) {
            System.out.println("No completion history found.");
            return;
        }

        System.out.println("Completion History:");

        for (Date date : history) {
            System.out.println(date);
        }
    }

    private void deleteHabit(int userId) {

        System.out.print("Enter habit ID to delete: ");
        int habitId = Integer.parseInt(sc.nextLine());

        if (habitService.deleteHabit(habitId, userId)) {
            System.out.println("Habit deleted successfully!");
        } else {
            System.out.println("Could not delete habit.");
        }
    }

    private void viewDailyStreak() {

        System.out.print("Enter habit ID: ");
        int habitId = Integer.parseInt(sc.nextLine());

        int streak = habitService.calculateDailyStreak(habitId);

        System.out.println(
            "Your current daily streak is: " + streak +
            (streak == 1 ? " day!" : " days!")
        );
    }

    private void viewCompletionPercentage() {

        System.out.print("Enter habit ID: ");
        int habitId = Integer.parseInt(sc.nextLine());

        double percentage =
            habitService.calculateCompletionPercentage(habitId);

        System.out.printf(
            "Habit Completion Percentage: %.2f%%%n",
            percentage
        );
    }
}
