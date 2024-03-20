package edu.depaul;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Register implements usercheck {

    // Checks if the username is available for registration
    @Override
    public boolean check(String username) {
        // Directly use the doesUsernameExist method to check if username exists
        return doesUsernameExist(username);
    }

    // Method to check the existence of a username in the database
    private boolean doesUsernameExist(String username) {
        final String query = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root");
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("An error occurred while checking for username existence.");
            e.printStackTrace();
        }
        return false;
    }

    // Register a new user
    public boolean registerUser(String username, String password) {
        // Use the check method to determine if the username already exists
        if (!check(username)) {
            final String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root");
                 PreparedStatement ps = conn.prepareStatement(insertQuery)) {
                ps.setString(1, username);
                ps.setString(2, password);
                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                System.err.println("An error occurred during user registration.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Username already exists. Please choose a different username.");
        }
        return false; // Registration failed
    }

    @Override
    public boolean check(String username, String password) {
        throw new UnsupportedOperationException("Checking a password is not supported during registration.");
    }
}
