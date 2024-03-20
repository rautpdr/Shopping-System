package edu.depaul.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import edu.depaul.Payment;

public class Paymenttest {
	
	 private Connection connection;
	    private Payment payment;

	    @BeforeEach
	    public void setUp() throws SQLException {
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDBTest", "root", "root");
	        payment = new Payment();

	    }

	    @Test
	    public void testAddNewPaymentMethod() throws SQLException {
	        String cardNumber = "123456789012";
	        String expiryDate = "2025-12-12"; 
	        String cvv = "123";
	        String cardType = "DEBIT";
	        String billingAddress = "Test Address";
	        String simulatedInput = cardNumber + "\n" + expiryDate + "\n" + cvv + "\n" + cardType + "\n" + billingAddress;
	        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

	        Scanner scanner = new Scanner(System.in);
	        String username = "testuser";

	        payment.addNewPaymentMethod(username, scanner);

	        String query = "SELECT * FROM payment_methods WHERE card_number = ?";
	        try (PreparedStatement ps = connection.prepareStatement(query)) {
	            ps.setString(1, cardNumber);
	            ResultSet rs = ps.executeQuery();
	            assertTrue(rs.next(), "Payment method should be added to the database.");
	        }

	        System.setIn(System.in); 
	    }

	    @AfterEach
	    public void tearDown() throws SQLException {
	        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM payment_methods WHERE card_number = ?")) {
	            ps.setString(1, "123456789012"); 
	            ps.executeUpdate();
	        }

	        if (connection != null) {
	            connection.close();
	        }
	    }
	
	
	@Test
    public void testProceedToPayment() throws SQLException {
        String username = "manthan@gmail.com"; 
        Payment pp = new Payment();
        Scanner mockScanner = new Scanner(System.in);
        
        pp.proceedToPayment(username,mockScanner );
        
     
    }
	
	@Test
    public void proceedToPaymentWithCardId() throws SQLException {
        String username = "manthan@gmail.com"; 
        Payment pp = new Payment();
        Scanner mockScanner = new Scanner(System.in);
        
        pp.proceedToPayment(username,mockScanner );
        
     
    }
	
	
	@Test
	public void testCardNumberExists() throws SQLException {
	    Payment payment = new Payment();
	    boolean exists = payment.cardNumberExists("879360683312");
	    Assertions.assertTrue(exists, "The card number should exist in the database.");

	    boolean notExists = payment.cardNumberExists("000000000000");
	    Assertions.assertFalse(notExists, "The card number should not exist in the database.");
	}



}
