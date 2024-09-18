package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import model.AvailableDate;
import util.Dbh;

public class AvailableDatesService {  

    public static Map<Integer, AvailableDate> getAvailableDates() {
        Map<Integer, AvailableDate> availableDatesMap = new HashMap<>();
        String sql = "SELECT idavailabledate, availabledate, availablehour FROM availabledate WHERE date_taken = 0 ORDER BY availabledate, idavailabledate";

        try (Connection conn = Dbh.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int id = rs.getInt("idavailabledate");
                String date = rs.getDate("availabledate").toString();
                String hour = rs.getString("availablehour");

                // Directly use the id as the key and create a new AvailableDate with the hour in an array
                availableDatesMap.put(id, new AvailableDate(id, date, new String[]{hour}));
            }
        } catch (SQLException e) {
            System.err.println("Error reading available dates from the database: " + e.getMessage());
            e.printStackTrace();
        }

        return availableDatesMap;
    }
    
    public static boolean addAvailableDate(String date, String hour) {
        String sql = "INSERT INTO availabledate (availabledate, availablehour, date_taken) VALUES (?, ?, 0)";

        try (Connection conn = Dbh.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, java.sql.Date.valueOf(date)); 
            pstmt.setString(2, hour);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Available date added successfully.");
                return true;
            } else {
                System.out.println("No available date was added.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("SQL error during available date insertion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean deleteAvailableDate(int idAvailableDate) {
        String sql = "DELETE FROM availabledate WHERE idavailabledate = ?";

        try (Connection conn = Dbh.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idAvailableDate);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Available date deleted successfully.");
                return true;
            } else {
                System.out.println("No available date was deleted.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("SQL error during available date deletion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
}
