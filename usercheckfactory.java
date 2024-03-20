package edu.depaul;

public class usercheckfactory {
    
    // Factory method to return either a Login or Register instance
    public static usercheck getAction(String actionType) {
        if ("login".equalsIgnoreCase(actionType)) {
            return new Login();
        } else if ("register".equalsIgnoreCase(actionType)) {
            return new Register();
        } else {
            throw new IllegalArgumentException("Invalid action type: " + actionType);
        }
    }
}

