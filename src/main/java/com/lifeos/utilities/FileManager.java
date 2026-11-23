
package com.lifeos.utilities;

import com.lifeos.model.Task;
import com.lifeos.model.Habit;
import com.lifeos.service.TaskService;
import com.lifeos.service.HabitService;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

public class FileManager {

    // 1. Export tasks to CSV
    public boolean exportTasksToCSV(List<Task> tasks, String filePath) {

        try (FileWriter writer = new FileWriter(filePath)) {

            writer.append("Task ID,User ID,Title,Description,Priority,Status,Due Date\n");

            for (Task task : tasks) {

                writer.append(String.valueOf(task.getTaskId())).append(",");
                writer.append(String.valueOf(task.getUserId())).append(",");
                writer.append(escapeCSV(task.getTitle())).append(",");
                writer.append(escapeCSV(task.getDescription())).append(",");
                writer.append(escapeCSV(task.getPriority())).append(",");
                writer.append(escapeCSV(task.getStatus())).append(",");
                writer.append(
                    task.getDueDate() == null
                        ? ""
                        : task.getDueDate().toString()
                );

                writer.append("\n");
            }

            System.out.println("Tasks exported successfully!");
            return true;

        } catch (IOException e) {

            System.out.println("Error exporting tasks: " + e.getMessage());
            return false;
        }
    }

    // 2. Import tasks from CSV
    public boolean importTasksFromCSV(String filePath, int userId) {

        TaskService taskService = new TaskService();
        int importedCount = 0;

        try (BufferedReader reader =
                 new BufferedReader(new FileReader(filePath))) {

            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] data = parseCSVLine(line);

                if (data.length != 7) {
                    System.out.println("Skipping invalid CSV row.");
                    continue;
                }

                String title = data[2];
                String description = data[3];
                String priority = data[4].toUpperCase();
                String status = data[5].toUpperCase();

                Date dueDate = data[6].isBlank()
                    ? null
                    : Date.valueOf(data[6]);

                Task task = new Task(
                    0,
                    userId,
                    title,
                    description,
                    priority,
                    status,
                    dueDate
                );

                if (taskService.createTask(task)) {
                    importedCount++;
                }
            }

            System.out.println(
                importedCount + " tasks imported successfully!"
            );

            return true;

        } catch (Exception e) {

            System.out.println("Error importing CSV: " + e.getMessage());
            return false;
        }
    }

    // 3. Export productivity report to text file
    public boolean exportProductivityReport(int userId, String filePath) {

        TaskService taskService = new TaskService();
        HabitService habitService = new HabitService();

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
            productivity = completedTasks * 100.0 / totalTasks;
        }

        int weeklyCompleted =
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

        try (FileWriter writer = new FileWriter(filePath)) {

            writer.write("===== LIFEOS PRODUCTIVITY REPORT =====\n\n");

            writer.write("Total Tasks: " + totalTasks + "\n");
            writer.write("Completed Tasks: " + completedTasks + "\n");
            writer.write("Pending Tasks: " + pendingTasks + "\n");

            writer.write(
                    String.format("Overall Productivity: %.2f%%\n",
                            productivity)
            );

            writer.write("\n===== WEEKLY PRODUCTIVITY =====\n");

            writer.write(
                    "Tasks Completed This Week: " + weeklyCompleted + "\n"
            );

            writer.write("\n===== HABIT STATISTICS =====\n");

            writer.write("Total Habits: " + habits.size() + "\n");

            writer.write(
                    String.format("Average Habit Completion: %.2f%%\n",
                            averageHabitPercentage)
            );

            writer.write("\n=================================\n");

            System.out.println("Productivity report exported successfully!");
            return true;

        } catch (IOException e) {

            System.out.println(
                    "Error exporting productivity report: " + e.getMessage()
            );

            return false;
        }
    }

    // 4. Handle commas and quotes in CSV values
    private String escapeCSV(String value) {

        if (value == null) {
            return "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    // 5. Read CSV values, including quoted commas
    private String[] parseCSVLine(String line) {

        List<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char ch = line.charAt(i);

            if (ch == '"') {

                if (insideQuotes && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {

                    value.append('"');
                    i++;

                } else {
                    insideQuotes = !insideQuotes;
                }

            } else if (ch == ',' && !insideQuotes) {

                values.add(value.toString());
                value.setLength(0);

            } else {
                value.append(ch);
            }
        }

        values.add(value.toString());

        return values.toArray(new String[0]);
    }
}
