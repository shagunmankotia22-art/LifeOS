
package com.lifeos;

import java.sql.SQLException;
import java.util.Scanner;

import com.lifeos.model.User;
import com.lifeos.menu.TaskMenu;
import com.lifeos.menu.HabitMenu;
import com.lifeos.service.AuthService;
import com.lifeos.service.DashboardService;

public class App {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        AuthService authService = new AuthService();
        DashboardService dashboardService = new DashboardService();

        while (true) {

            System.out.println("\n===== LIFEOS =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            System.out.print("Enter choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
                continue;
            }

            try {

                switch (choice) {

                    case 1 -> {

                        System.out.print("Enter name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter email: ");
                        String email = sc.nextLine();

                        System.out.print("Enter password: ");
                        String password = sc.nextLine();

                        if (authService.register(name, email, password)) {
                            System.out.println("Registration successful!");
                        } else {
                            System.out.println("Registration failed.");
                        }
                    }

                    case 2 -> {

                        System.out.print("Enter email: ");
                        String email = sc.nextLine();

                        System.out.print("Enter password: ");
                        String password = sc.nextLine();

                        if (authService.login(email, password)) {

                            User user = authService.getCurrentUser();

                            System.out.println("Welcome, " + user.getName() + "!");

                            while (true) {

                                System.out.println("\n===== LIFEOS DASHBOARD =====");
                                System.out.println("1. Task Management");
                                System.out.println("2. Habit Tracker");
                                System.out.println("3. Productivity Dashboard");
                                System.out.println("4. Logout");

                                System.out.print("Enter choice: ");

                                int menuChoice;

                                try {
                                    menuChoice = Integer.parseInt(sc.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Enter a valid number.");
                                    continue;
                                }

                                if (menuChoice == 1) {

                                    TaskMenu taskMenu = new TaskMenu(sc);
                                    taskMenu.showMenu(user.getUserId());

                                } else if (menuChoice == 2) {

                                    HabitMenu habitMenu = new HabitMenu(sc);
                                    habitMenu.showMenu(user.getUserId());

                                } else if (menuChoice == 3) {

                                    dashboardService.showDashboard(
                                        user.getUserId()
                                    );

                                } else if (menuChoice == 4) {

                                    authService.logout();
                                    System.out.println("Logged out successfully.");
                                    break;

                                } else {

                                    System.out.println("Invalid choice.");
                                }
                            }

                        } else {

                            System.out.println("Invalid email or password.");
                        }
                    }

                    case 3 -> {
                        System.out.println("Thank you for using LIFEOS!");
                        sc.close();
                        return;
                    }

                    default -> System.out.println("Invalid choice.");
                }

            } catch (SQLException e) {

                System.out.println("Database error: " + e.getMessage());
            }
        }
    }
}
