
package com.lifeos.service;

public class TestAuth {

    public static void main(String[] args) {

        AuthService authService = new AuthService();

        try {

            // Get test password from environment variable
            String testPassword = System.getenv("LIFEOS_TEST_PASSWORD");

            if (testPassword == null || testPassword.isBlank()) {
                System.out.println("Test password is not configured.");
                return;
            }

            // Register a test user
            boolean registered = authService.register(
                    "Shagun",
                    "shagun.test@example.com",
                    testPassword
            );

            if (registered) {
                System.out.println("Registration Successful!");
            } else {
                System.out.println("Registration Failed or Email Already Exists!");
            }

            // Login test
            boolean login = authService.login(
                    "shagun.test@example.com",
                    testPassword
            );

            if (login) {
                System.out.println("Login Successful!");

                System.out.println("Welcome, " +
                        authService.getCurrentUser().getName());

            } else {
                System.out.println("Login Failed!");
            }

            // Logout test
            authService.logout();

            System.out.println("Logged out: " +
                    !authService.isLoggedIn());

        } catch (Exception e) {
            System.out.println("Authentication test failed.");
            e.printStackTrace();
        }
    }
}
