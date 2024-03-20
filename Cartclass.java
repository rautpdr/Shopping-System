package edu.depaul;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Cartclass {
    
    private static Cartclass instance;
    
    private Cartclass() { }

    public static synchronized Cartclass getInstance() {
        if (instance == null) {
            instance = new Cartclass();
        }
        return instance;
    }
    
    public static void displayCartForUser(String username, Scanner sc) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root")) {
            String query = "SELECT c.product_id, p.name, c.quantity FROM carts c JOIN users u ON c.user_id = u.id JOIN products p ON c.product_id = p.product_id WHERE u.username = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            System.out.println("Your Cart:");
            if (!rs.isBeforeFirst()) {
                System.out.println("Cart is empty.");
            } else {
                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    String productName = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    System.out.println(" Product ID: " + productId + ", Product Name: " + productName + ", Quantity: " + quantity);
                }
            }
            displayCartOptions(username, sc); // Call to display next options after cart
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	    
	    public static void displayCartOptions(String username, Scanner sc) throws SQLException {
	        System.out.println("\nWhat would you like to do next?");
	        System.out.println("1. Proceed to Payment");
	        System.out.println("2. Continue Shopping");
	        System.out.println("3. Add Product to Cart");
	        System.out.println("4. Remove Product from Cart");
	        System.out.println("5. View Cart Total");
	        System.out.println("6. Logout");
	        System.out.print("Your choice: ");
	        int choice = sc.nextInt();
	        sc.nextLine(); // Consume the newline

	        switch (choice) {
	            case 1:
	            	Payment paymentProcessor = new Payment(); 
	                paymentProcessor.proceedToPayment(username, sc); 
	                break;
	            case 2:
	                continueShopping(username, sc);
	                break;
	            case 3:
	                System.out.print("Enter Product ID: ");
	                int productIdToAdd = sc.nextInt();
	                System.out.print("Enter Quantity: ");
	                int quantityToAdd = sc.nextInt();
	                addProductToCart(UserAuthenticationclass.getUserId(username), productIdToAdd, quantityToAdd);
	                sc.nextLine(); 
	                break;
	            case 4:
	                System.out.print("Enter Product ID to remove: ");
	                int cartIdToRemove = sc.nextInt();
	                removeProductFromCart(UserAuthenticationclass.getUserId(username), cartIdToRemove);
	                sc.nextLine(); 
	                break;
	            case 5:
	                calculateCartTotal(username);
	                break;
	            case 6:
	            	UserAuthenticationclass.logout(sc);
	                break;
	            default:
	                System.out.println("Invalid option. Please try again.");
	                displayCartOptions(username, sc);
	                break;
	        }
	    }
	    
	    public static void continueShopping(String username, Scanner sc) throws SQLException {
	        System.out.println("Continuing shopping...");
	        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root");
	            PreparedStatement ps = conn.prepareStatement("SELECT category_id, name FROM categories WHERE parent_id IS NOT NULL")) {
	            ResultSet rs = ps.executeQuery();
	            if (rs.next()) {
	                
	                Product.showCategories(sc);
	                Product.searchProducts(sc);
	            } else {
	                System.out.println("No categories found.");
	            }
	        }
	    }
	    
	    public static void addProductToCart(int userId, int productId, int quantity) throws SQLException {
	        String query = "INSERT INTO carts (user_id, product_id, quantity) VALUES (?, ?, ?)";
	        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root");
	             PreparedStatement ps = conn.prepareStatement(query)) {
	            ps.setInt(1, userId);
	            ps.setInt(2, productId);
	            ps.setInt(3, quantity);
	            ps.executeUpdate();
	            System.out.println("Product added to cart successfully.");
	        }
	    }
	    
	    public static void removeProductFromCart(int userId, int cartId) throws SQLException {
	        String query = "DELETE FROM carts WHERE product_id = ? AND user_id = ?";
	        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root");
	             PreparedStatement ps = conn.prepareStatement(query)) {
	            ps.setInt(1, cartId);
	            ps.setInt(2, userId);
	            int rowsAffected = ps.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Product removed from cart successfully.");
	            } else {
	                System.out.println("No product found with the specified ID in your cart.");
	            }
	        }
	    }


	    
	    public static BigDecimal calculateCartTotal(String username) throws SQLException {
	        BigDecimal total = BigDecimal.ZERO;
	        String query = "SELECT SUM(p.price * c.quantity) AS total FROM carts c JOIN products p ON c.product_id = p.product_id JOIN users u ON c.user_id = u.id WHERE u.username = ?";
	        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root");
	             PreparedStatement ps = conn.prepareStatement(query)) {
	            ps.setString(1, username);
	            ResultSet rs = ps.executeQuery();
	            if (rs.next()) {
	                total = rs.getBigDecimal("total");
	                System.out.println("Total Cart Value: $" + total);
	            }
	        }
	        return total;
	    }
	    
	    public static void clearCart(int userId) throws SQLException {
	        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root")) {
	            String query = "DELETE FROM carts WHERE user_id = ?";
	            PreparedStatement ps = conn.prepareStatement(query);
	            ps.setInt(1, userId);
	            int rowsAffected = ps.executeUpdate();
	            
	            if (rowsAffected > 0) {
	                System.out.println("Cart cleared successfully.");
	            } else {
	                System.out.println("Your cart is already empty.");
	            }
	        }
	    }
	    
	    
	    
	    

}
