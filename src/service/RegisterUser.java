package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

import util.Dbh; 

public class RegisterUser {

    public static boolean registerUser(String username, String password, String passwordRepeat) {
        if (!isValidUsername(username) || !isValidPassword(password) || !isValidPasswordRepeat(passwordRepeat)) {
            System.out.println("Registration failed. Invalid input.");
            return false;
        }

        if (!comparePasswords(password, passwordRepeat)) {
            System.out.println("Registration failed. Passwords do not match.");
            return false;
        }
        
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        
        try (Connection conn = Dbh.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password); 
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Registration successful!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false; 
    }
	
	public static boolean isValidUsername(String username) {
		
		if(username.equalsIgnoreCase("admin")) {
			return false;
		} 
		
	    String regex = "^[a-zA-Z0-9_]+$"; 
	    return Pattern.matches(regex, username);
	}
	
	public static boolean isValidPassword(String password) {
		return password.length() >= 8 && password.length() <= 30;
	}
	
	public static boolean isValidPasswordRepeat(String passwordRepeat) {
		return passwordRepeat.length() >= 8 && passwordRepeat.length() <= 30;
	}
	
	public static boolean comparePasswords(String password, String passwordRepeat) {
		return password.equals(passwordRepeat);
	}
}
