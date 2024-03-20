package edu.depaul;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthenticationclassTest {

    @Test
    public void testAuthenticateUser() {
        // Test with valid username and password
        boolean result = UserAuthenticationclass.authenticateUser("pratham@gmail.com", "pratham");
        assertTrue(result);

        // Test with non-existing username
        result = UserAuthenticationclass.authenticateUser("nonExistingUser", "nonExistingPassword");
        assertFalse(result);

        // Test with incorrect password
        result = UserAuthenticationclass.authenticateUser("pratham@gmail.com", "prathamz");
        assertFalse(result);
    }

    @Test
    public void testRegisterUser() {
        // Test successful registration
        boolean result = UserAuthenticationclass.registerUser("newUser", "newPassword");
        assertTrue(result);

        // Test registration with existing username
        result = UserAuthenticationclass.registerUser("pratham@gmail.com", "pratham");
        assertFalse(result);
    }

    @Test
    public void testGetUserId() throws SQLException {
        // Test with existing username
        int userId = UserAuthenticationclass.getUserId("pratham@gmail.com");
        assertEquals(1, userId);

        // Test with non-existing username
        try {
            userId = UserAuthenticationclass.getUserId("xyz@xyz");
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            // Expected
        }
    }

    

   
}