package edu.depaul;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Product {
	
	public static void showCategories(Scanner sc) {
	        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root");
	             PreparedStatement ps = conn.prepareStatement("SELECT category_id, name FROM categories WHERE parent_id IS NOT NULL")) {
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                System.out.println(rs.getInt("category_id") + ". " + rs.getString("name"));
	            }
	            System.out.println("Enter the ID of the category you wish to view:");
	            int categoryId = sc.nextInt();
	            sc.nextLine(); 
	            showProductsInCategory(categoryId, sc);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	
	public static void showProductsInCategory(int categoryId, Scanner sc) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root");
             PreparedStatement ps = conn.prepareStatement("SELECT product_id, name, description, price FROM products WHERE category_id = ?")) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            boolean hasProducts = false;
            while (rs.next()) {
                if (!hasProducts) {
                    System.out.println("Products in selected category:");
                    hasProducts = true;
                }
                System.out.println("Product ID: " + rs.getInt("product_id") + ", Name: " + rs.getString("name") + ", Description: " + rs.getString("description") + ", Price: $" + rs.getBigDecimal("price"));
            }
            if (!hasProducts) {
                System.out.println("No products found in this category.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public static void searchProducts(Scanner sc) {
        System.out.println("Enter search query:");
        String query = sc.nextLine();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root");
             PreparedStatement ps = conn.prepareStatement("SELECT product_id, name, description, price FROM products WHERE name LIKE ? OR description LIKE ?")) {
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt("product_id") + ". " + rs.getString("name") + " - " + rs.getString("description") + " - $" + rs.getBigDecimal("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	

}
