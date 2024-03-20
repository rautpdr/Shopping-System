package edu.depaul.Test;

import edu.depaul.Cartclass; 
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.*;

import java.math.BigDecimal;
import java.sql.*;

public class CartclassTest {

    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        Cartclass.getInstance();
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root");
    }
    
    @Test
    @DisplayName("Cart Display to user")
    public void testDisplayCartForUser() {
        String username = "testUser";
        Scanner mockScanner = new Scanner(System.in); 
        Cartclass.displayCartForUser(username, mockScanner);
        
    }
    
    @Test
    @DisplayName("Continue Shopping function")
    public void testContinueShopping() throws SQLException {
        String username = "pratham@gmail.com";
        Scanner mockScanner = new Scanner(System.in);
        
        Cartclass.continueShopping(username, mockScanner);
        
    }

    @Test
    @DisplayName("Add products to cart")
    public void testAddProductToCart() throws SQLException {
        int userId = 1;
        int productId = 1;
        int quantity = 2;

        Cartclass.addProductToCart(userId, productId, quantity);

        String query = "SELECT * FROM carts WHERE user_id = ? AND product_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                Assertions.assertTrue(rs.next());
                Assertions.assertEquals(quantity, rs.getInt("quantity"));
            }
        }
    }
    
    @Test
    @DisplayName("Remove products from cart")

    public void testRemoveProductFromCart() throws SQLException {
        int userId = 1; 
        int cartIdToRemove = 1;

        Cartclass.removeProductFromCart(userId, cartIdToRemove);

        String query = "SELECT * FROM carts WHERE user_id = ? AND cart_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, cartIdToRemove);
            try (ResultSet rs = ps.executeQuery()) {
                Assertions.assertFalse(rs.next(), "Product should have been removed from the cart");
            }
        }
    }

    
    @Test
    @DisplayName("Calculate cart value")

    public void testCalculateCartTotal() throws SQLException {
        String username = "pratham@gmail.com";
        
        BigDecimal expectedTotal = new BigDecimal("3000"); 

        BigDecimal total = Cartclass.calculateCartTotal(username);

        Assertions.assertNotNull(total, "Total should not be null");
        Assertions.assertEquals(expectedTotal.stripTrailingZeros(), total.stripTrailingZeros(), "Cart total should match expected value");
    }
    
    @Test
    @DisplayName("Clear cart")

    public void testClearCart() throws SQLException {
        int userId = 1; 

        Cartclass.clearCart(userId);

        String query = "SELECT * FROM carts WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                Assertions.assertFalse(rs.next(), "Cart should be empty after clearing");
            }
        }
    }
    
   
    
    @Test
    public void testDisplayCartOptions() throws SQLException {
        String username = "pratham@gmail.com";
        Scanner mockScanner = new Scanner(System.in); 
        
        Cartclass.displayCartOptions(username, mockScanner);
        
        
    }
    
    





}
