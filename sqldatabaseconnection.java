package edu.depaul;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class sqldatabaseconnection {
	public static void main(String args[]) throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con =DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB","root","root");
        if(con!=null)
            System.out.println("Connection established");
        else
            System.out.println("Connection failed");
    }
	

	
	
	
}
