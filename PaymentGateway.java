package edu.depaul;

import java.sql.SQLException;
import java.util.Scanner;

public interface PaymentGateway {
    void proceedToPayment(String username, Scanner sc) throws SQLException;
    void addNewPaymentMethod(String username, Scanner sc) throws SQLException;
    boolean cardNumberExists(String cardNumber) throws SQLException;
	

	
    
}
