
package com.lifeos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lifeos.database.DBConnection;
import com.lifeos.model.User;

public class UserDAO {

    // Register a new user
    public boolean registerUser(User user) throws SQLException {

        String sql = "INSERT INTO users (name, email, password_hash) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());

            return statement.executeUpdate() > 0;
        }
    }

    // Find user by email
    public User getUserByEmail(String email) throws SQLException {

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    User user = new User();

                    user.setUserId(result.getInt("user_id"));
                    user.setName(result.getString("name"));
                    user.setEmail(result.getString("email"));
                    user.setPasswordHash(result.getString("password_hash"));

                    return user;
                }
            }
        }

        return null;
    }

    // Check whether an email already exists
    public boolean emailExists(String email) throws SQLException {

        String sql = "SELECT user_id FROM users WHERE email = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }
}
