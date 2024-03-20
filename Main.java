package edu.depaul;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            String username = null;
            while (true) {
                if (username == null) {
                    System.out.println("\nPlease choose an option:");
                    System.out.println("1. Login");
                    System.out.println("2. Register");
                    System.out.println("3. Exit");
                    System.out.print("Your choice: ");

                    String choice = sc.nextLine();

                    switch (choice) {
                        case "1":
                            username = attemptLogin(sc);
                            if (username != null) {
                                System.out.println("Login Successful.");
                                // After successful login, the cart options should be displayed.
          
                            } else {
                                System.out.println("Login Failed.");
                            }
                            break;
                        case "2":
                            attemptRegister(sc);
                            break;
                        case "3":
                            System.out.println("Exiting...");
                            return;
                        default:
                            System.out.println("Invalid option. Please try again.");
                            break;
                    }
                } else {
                    // User is logged in, display cart and other options
                    Cartclass.displayCartForUser(username, sc);
                   
                }
            }
        }
    }

    private static String attemptLogin(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        usercheck loginChecker = usercheckfactory.getAction("login");
        boolean isAuthenticated = loginChecker.check(username, password); 
        if (isAuthenticated) {
            return username; 
        } else {
            return null; 
        }
    }

    private static void attemptRegister(Scanner scanner) {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine();
        
        Register register = new Register();
        if (register.check(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return;
        }
        
        System.out.print("Choose a password: ");
        String password = scanner.nextLine();
        
        if (register.registerUser(username, password)) {
            System.out.println("User registered successfully! Please login.");
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }

}
