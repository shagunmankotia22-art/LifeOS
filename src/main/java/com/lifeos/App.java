package com.lifeos;

import java.sql.SQLException;
import java.util.Scanner;

import com.lifeos.model.User;
import com.lifeos.menu.TaskMenu;
import com.lifeos.service.AuthService;

public class App {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AuthService authService = new AuthService();

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
                            System.out.println("Registration failed. Email may already exist.");
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

                            TaskMenu taskMenu = new TaskMenu(sc);
                            taskMenu.showMenu(user.getUserId());

                            authService.logout();

                        } else {
                            System.out.println("Invalid email or password.");
                        }
                    }

                    case 3 -> {
                        System.out.println("Thank you for using LifeOS!");
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