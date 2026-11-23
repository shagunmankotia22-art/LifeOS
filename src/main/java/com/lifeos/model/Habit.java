
package com.lifeos.model;

public class Habit {

    private int habitId;
    private int userId;
    private String habitName;
    private String description;

    // Default constructor
    public Habit() {
    }

    // Parameterized constructor
    public Habit(int habitId, int userId, String habitName, String description) {
        this.habitId = habitId;
        this.userId = userId;
        this.habitName = habitName;
        this.description = description;
    }

    // Getters and Setters
    public int getHabitId() {
        return habitId;
    }

    public void setHabitId(int habitId) {
        this.habitId = habitId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
