package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import model.Treatment;
import util.Dbh; 

public class TreatmentsService {

    public static Map<Integer, Treatment> getTreatmentOptions() {
    	Map<Integer, Treatment> treatmentsMap = new HashMap<>();
        String sql = "SELECT idtreatment, treatment_name, treatment_price FROM treatment";

        try (Connection conn = Dbh.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int treatmentId = rs.getInt("idtreatment");
                String name = rs.getString("treatment_name");
                int price = rs.getInt("treatment_price");

                Treatment treatment = new Treatment(treatmentId, name, price);
                treatmentsMap.put(treatmentId, treatment);
            }
        } catch (SQLException e) {
            System.err.println("Error reading treatment options from the database: " + e.getMessage());
            e.printStackTrace();
        }

        return treatmentsMap;
    }
    
    public static boolean addTreatment(String treatmentName, int treatmentPrice) {
        String sql = "INSERT INTO treatment (treatment_name, treatment_price) VALUES (?, ?)";

        try (Connection conn = Dbh.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, treatmentName);
            pstmt.setInt(2, treatmentPrice);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Treatment added successfully.");
                return true;
            } else {
                System.out.println("No treatment was added.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("SQL error during treatment insertion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean deleteTreatment(int treatmentId) {
        String sql = "DELETE FROM treatment WHERE idtreatment = ?";

        try (Connection conn = Dbh.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, treatmentId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Treatment deleted successfully.");
                return true;
            } else {
                System.out.println("No treatment was deleted.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("SQL error during treatment deletion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

