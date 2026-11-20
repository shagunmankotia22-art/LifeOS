
package com.lifeos.service;

import java.sql.SQLException;

import com.lifeos.dao.UserDAO;
import com.lifeos.model.User;
import com.lifeos.utilities.PasswordUtil;

public class AuthService {

    private final UserDAO userDAO;

    private User currentUser;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    // Register a new user
    public boolean register(String name, String email, String password)
            throws SQLException {

        if (name == null || name.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {
            return false;
        }

        if (userDAO.emailExists(email)) {
            return false;
        }

        String hashedPassword = PasswordUtil.hashPassword(password);

        User user = new User(name, email, hashedPassword);

        return userDAO.registerUser(user);
    }

    // Login an existing user
    public boolean login(String email, String password)
            throws SQLException {

        if (email == null || password == null) {
            return false;
        }

        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            return false;
        }

        boolean valid = PasswordUtil.verifyPassword(
                password,
                user.getPasswordHash()
        );

        if (valid) {
            currentUser = user;
            return true;
        }

        return false;
    }

    // Logout
    public void logout() {
        currentUser = null;
    }

    // Get currently logged-in user
    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
