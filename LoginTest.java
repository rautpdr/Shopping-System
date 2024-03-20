package edu.depaul.Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.depaul.Login;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginTest {

    private Login login;
    private Connection conn;

    @BeforeEach
    public void setUp() {
        try {
            // Setup connection to the test database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDBTest", "root", "root");

            // Initialize your Login class
            login = new Login();

            // Optionally, set up your test data here using Statement/PreparedStatement
            // For example, ensure that there's a known user in the database
            try (Statement stmt = conn.createStatement()) {
                // Ensure the table is empty or has predictable content
                stmt.execute("DELETE FROM users WHERE username='testUser'");
                // Insert test data
                stmt.execute("INSERT INTO users (username, password) VALUES ('testUser', 'testPass')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Setup failed: " + e.getMessage());
        }
    }

    @Test
    public void testValidLogin() {
        assertTrue(login.check("manthan@gmail.com", "manthan"), "Valid login credentials should return true");
    }

    @Test
    public void testInvalidLogin() {
        assertFalse(login.check("lll", "lll"), "Invalid login credentials should return false");
    }

    @AfterEach
    public void tearDown() {
        try {
            // Clean up the database
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM users WHERE username='testUser'");
            }

            // Close connection
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
