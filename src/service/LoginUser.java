package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;
import util.Dbh;

public class LoginUser {

    public static User loginUser(String username, String password) {
        // Modified SQL to select the id as well
        String sql = "SELECT idUser FROM user WHERE username = ? AND password = ?";
        try (Connection conn = Dbh.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    // Fetch the id of the user
                    int id = resultSet.getInt("idUser");
                    return new User(id, username, password); // Return the user object with id
                }
            }
        } catch (SQLException e) {
            System.err.println("Login failed. Error accessing the database: " + e.getMessage());
            e.printStackTrace();
        }

        return null; // Return null if login failed
    }
}