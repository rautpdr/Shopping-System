package edu.depaul;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserAuthenticationclass {
	
	  
    public static int getUserId(String username) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root");
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM users WHERE username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("User not found");
                
            }
        }
    }
    
    public static void logout(Scanner sc) {
        System.out.println("You have been logged out.");
        Main.main(null);
        return;
    }

	

	
    
    

}
