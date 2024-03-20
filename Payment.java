package edu.depaul;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Payment implements PaymentGateway{
	

	    
	 @Override
	    public void proceedToPayment(String username, Scanner sc) throws SQLException {
	        // Display available payment methods 
	        System.out.println("Your Payment Methods:");
	        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root")) {
	            String query = "SELECT pm.id, pm.card_number, pm.card_type, pm.expiry_date FROM payment_methods pm JOIN users u ON pm.user_id = u.id WHERE u.username = ?";
	            try (PreparedStatement ps = conn.prepareStatement(query)) {
	                ps.setString(1, username);
	                ResultSet rs = ps.executeQuery();
	                
	                if (!rs.isBeforeFirst()) {
	                    System.out.println("No payment methods found for this user.");
	                    addNewPaymentMethod(username, sc);
	                    return;
	                }
	                
	                while (rs.next()) {
	                    System.out.println("ID: " + rs.getInt("id") + ", Card Number: " + rs.getString("card_number") + ", Type: " + rs.getString("card_type") + ", Expiry Date: " + rs.getDate("expiry_date"));
	                }
	            }

	            //add a new payment method
	            System.out.println("\nDo you want to add a new card? (yes/no)");
	            String userDecision = sc.nextLine();
	            if ("yes".equalsIgnoreCase(userDecision)) {
	                addNewPaymentMethod(username, sc);
	                return;
	            }

	            // Proceed with existing card selection for payment
	            System.out.println("Select a card ID for payment or enter 'new' to add a new card:");
	            String cardIdInput = sc.nextLine();
	            if ("new".equalsIgnoreCase(cardIdInput)) {
	                addNewPaymentMethod(username, sc);
	                return;
	            }

	            int cardId = Integer.parseInt(cardIdInput);
	            if (!cardIdExists(cardId, username, conn)) {
	                System.out.println("Invalid card ID. Please select a valid card ID.");
	                return;
	            }

	            // Display total cart amount before proceeding with payment
	            proceedToPaymentWithCardId(cardId, username, sc, conn); // Process payment with selected card ID
	        }
	    }

	    private void proceedToPaymentWithCardId(int cardId, String username, Scanner sc, Connection conn) throws SQLException {
	        BigDecimal cartTotal = Cartclass.calculateCartTotal(username);
	        System.out.println("Total cart amount: $" + cartTotal);

	        System.out.println("Enter the amount to pay:");
	        BigDecimal amount = new BigDecimal(sc.nextLine());

	        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
	            System.out.println("Invalid amount. Amount must be greater than zero.");
	            return;
	        }

	        if (amount.compareTo(cartTotal) != 0) {
	            System.out.println("The entered amount does not match the total cart value. Please try again.");
	            return;
	        }

	        // Confirm payment processing
	        System.out.println("Payment of $" + amount + " processed successfully with card ID: " + cardId);
	        Cartclass.clearCart(UserAuthenticationclass.getUserId(username));
	        System.out.println("Your cart has been cleared. Thank you for your payment.");
	    }



	private boolean cardIdExists(int cardId, String username, Connection conn) throws SQLException {
	    String query = "SELECT 1 FROM payment_methods WHERE id = ? AND user_id = (SELECT id FROM users WHERE username = ?)";
	    try (PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setInt(1, cardId);
	        ps.setString(2, username);
	        try (ResultSet rs = ps.executeQuery()) {
	            return rs.next(); 
	        }
	    }
	}


	@Override
	public void addNewPaymentMethod(String username, Scanner sc) throws SQLException {
	    System.out.println("Enter card number: (card number must be 12 digits)");
	    String cardNumber;
	    while (true) {
	        cardNumber = sc.nextLine();
	        if (cardNumber.length() != 12) {
	            System.out.println("Invalid input. Please enter a valid 12 digit card number:");
	        } else if (cardNumberExists(cardNumber)) {
	            System.out.println("This card number already exists. Please enter a different card number:");
	        } else {
	            break; // Valid and unique card number
	        }
	    }

	    System.out.println("Enter expiry date (YYYY-MM-DD):");
	    String expiryDate;
	    while (true) {
	        expiryDate = sc.nextLine();
	        if (expiryDate.length() != 10) {
	            System.out.println("Invalid input. Please enter a valid expiry date in the format YYYY-MM-DD:");
	        } else {
	            break; 
	        }
	    }

	    System.out.println("Enter CVV:");
	    String cvv = sc.nextLine();
	    while (cvv.length() != 3) {
	        System.out.println("Invalid input. CVV must be 3 digits. Please enter a valid CVV:");
	        cvv = sc.nextLine();
	    }

	    System.out.println("Enter card type (DEBIT/CREDIT):");
	    String cardType;
	    while (true) {
	        cardType = sc.nextLine().toUpperCase();
	        if (!cardType.equals("DEBIT") && !cardType.equals("CREDIT")) {
	            System.out.println("Invalid input. Please enter either DEBIT or CREDIT:");
	        } else {
	            break; // Valid card type
	        }
	    }

	    System.out.println("Enter billing address:");
	    String billingAddress = sc.nextLine();

	    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root");
	         PreparedStatement ps = conn.prepareStatement(
	                 "INSERT INTO payment_methods (user_id, card_number, expiry_date, cvv, card_type, billing_address) VALUES " +
	                         "((SELECT id FROM users WHERE username = ?), ?, ?, ?, ?, ?)")) {
	        ps.setString(1, username);
	        ps.setString(2, cardNumber);
	        ps.setString(3, expiryDate);
	        ps.setString(4, cvv);
	        ps.setString(5, cardType);
	        ps.setString(6, billingAddress);

	        int rowsAffected = ps.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("New payment method added successfully.");
	        } else {
	            System.out.println("Failed to add the new payment method.");
	        }
	    }
	}

	


	@Override
	public boolean cardNumberExists(String cardNumber) throws SQLException {
	    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root");
	         PreparedStatement ps = conn.prepareStatement("SELECT card_number FROM payment_methods WHERE card_number = ?")) {
	        ps.setString(1, cardNumber);
	        try (ResultSet rs = ps.executeQuery()) {
	            return rs.next();
	        }
	    }
	}
}
