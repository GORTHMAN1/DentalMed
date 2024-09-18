package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Appointment;
import model.AvailableDate;
import model.Treatment;
import util.Dbh;

public class AppointmentService {
    
    public static Appointment createAppointment(Treatment treatment, AvailableDate availableDate, int userId) {
        if (treatment == null || availableDate == null) {
            System.out.println("Error: Treatment or AvailableDate not found.");
            return null;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = Dbh.getConnection();
       
            conn.setAutoCommit(false);
            
            String insertSql = "INSERT INTO appointment (appointment_option_id, appointment_date_id, user_id) VALUES (?, ?, ?)";
            
            pstmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, treatment.getTreatmentId());
            pstmt.setInt(2, availableDate.getAvailableDateId());
            pstmt.setInt(3, userId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating appointment failed, no rows affected.");
            }
            
            int appointmentId = 0;
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                appointmentId = rs.getInt(1);
            } else {
                throw new SQLException("Creating appointment failed, no ID obtained.");
            }
            
            String updateSql = "UPDATE availabledate SET date_taken = 1 WHERE idavailabledate = ?";
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setInt(1, availableDate.getAvailableDateId());
            
            pstmt.executeUpdate();
            
            conn.commit();
            
            return new Appointment(appointmentId, treatment, availableDate);
            
        } catch (SQLException e) {
            System.err.println("SQL error during appointment creation: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("SQL error during rollback: " + ex.getMessage());
                }
            }
            return null;
        } finally {
        
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.setAutoCommit(true); 
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.err.println("SQL error during cleanup: " + ex.getMessage());
            }
        }
    }
    
    public static boolean deleteAppointmentAndUpdateDate(List<Appointment> appointments, int chosenAppointmentId) {
    
        Integer availableDateId = appointments.stream()
                .filter(a -> a.getIdAppointment() == chosenAppointmentId)
                .findAny()
                .map(a -> a.getDate().getAvailableDateId())
                .orElse(null);
        
        if (availableDateId == null) {
            System.out.println("Appointment not found.");
            return false;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = Dbh.getConnection();
            conn.setAutoCommit(false); 

            String deleteSql = "DELETE FROM appointment WHERE idappointment = ?";
            pstmt = conn.prepareStatement(deleteSql);
            pstmt.setInt(1, chosenAppointmentId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting appointment failed, no rows affected.");
            }

            String updateSql = "UPDATE availabledate SET date_taken = 0 WHERE idavailabledate = ?";
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setInt(1, availableDateId);
            pstmt.executeUpdate();

            conn.commit(); 

            System.out.println("Appointment deleted and date updated successfully.");
            return true;
            
        } catch (SQLException e) {
            System.err.println("SQL error during appointment deletion and date update: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException ex) {
                    System.err.println("SQL error during rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.setAutoCommit(true); 
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.err.println("SQL error during cleanup: " + ex.getMessage());
            }
        }
    }
    
    public static List<Appointment> getAppointmentsList() {
        List<Appointment> appointmentsList = new ArrayList<>();
        String sql = "SELECT a.idappointment, a.appointment_option_id, a.appointment_date_id, a.user_id, a.appointment_done, " + // Added missing comma here
                "t.treatment_name, t.treatment_price, " +
                "ad.availabledate, ad.date_taken " +
                "FROM appointment a " +
                "JOIN treatment t ON a.appointment_option_id = t.idtreatment " + // Moved WHERE clause after JOINs
                "JOIN availabledate ad ON a.appointment_date_id = ad.idavailabledate " +
                "WHERE a.appointment_done = 0";

        try (Connection conn = Dbh.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int idAppointment = rs.getInt("idappointment");
                int userId = rs.getInt("user_id"); 
                
                Treatment treatment = new Treatment(rs.getInt("appointment_option_id"), rs.getString("treatment_name"), rs.getInt("treatment_price"));
                AvailableDate availableDate = new AvailableDate(rs.getInt("appointment_date_id"), rs.getString("availabledate"), new String[]{rs.getString("date_taken")});

                Appointment appointment = new Appointment(idAppointment, treatment, availableDate);
                appointmentsList.add(appointment);
            }
        } catch (SQLException e) {
            System.err.println("SQL error during fetching appointments: " + e.getMessage());
            e.printStackTrace();
        }
        return appointmentsList;
    }
    
    public static List<Appointment> getAppointmentsHistory() {
        List<Appointment> appointmentsList = new ArrayList<>();
        String sql = "SELECT a.idappointment, a.appointment_option_id, a.appointment_date_id, a.user_id, a.appointment_done, " + 
                "t.treatment_name, t.treatment_price, " +
                "ad.availabledate, ad.availablehour " +
                "FROM appointment a " +
                "JOIN treatment t ON a.appointment_option_id = t.idtreatment " + 
                "JOIN availabledate ad ON a.appointment_date_id = ad.idavailabledate " +
                "WHERE a.appointment_done = 1";

        try (Connection conn = Dbh.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int idAppointment = rs.getInt("idappointment");
                int userId = rs.getInt("user_id"); 
                
                Treatment treatment = new Treatment(rs.getInt("appointment_option_id"), rs.getString("treatment_name"), rs.getInt("treatment_price"));
                AvailableDate availableDate = new AvailableDate(rs.getInt("appointment_date_id"), rs.getString("availabledate"), new String[]{rs.getString("availablehour")});

                Appointment appointment = new Appointment(idAppointment, treatment, availableDate);
                appointmentsList.add(appointment);
            }
        } catch (SQLException e) {
            System.err.println("SQL error during fetching appointments: " + e.getMessage());
            e.printStackTrace();
        }
        return appointmentsList;
    }
    
    public static List<Appointment> getPendingAppointments() {
        List<Appointment> pendingAppointments = new ArrayList<>();
        String sql = "SELECT a.idappointment, a.appointment_option_id, a.appointment_date_id, a.user_id, " +
                     "t.treatment_name, t.treatment_price, " +
                     "ad.availabledate, ad.availablehour " +
                     "FROM appointment a " +
                     "JOIN treatment t ON a.appointment_option_id = t.idtreatment " +
                     "JOIN availabledate ad ON a.appointment_date_id = ad.idavailabledate " +
                     "WHERE a.appointment_done = 0";

        try (Connection conn = Dbh.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int idAppointment = rs.getInt("idappointment");
                Treatment treatment = new Treatment(rs.getInt("appointment_option_id"), rs.getString("treatment_name"), rs.getInt("treatment_price"));
                AvailableDate availableDate = new AvailableDate(rs.getInt("appointment_date_id"), rs.getString("availabledate"), new String[]{rs.getString("availablehour")});

                Appointment appointment = new Appointment(idAppointment, treatment, availableDate);
                pendingAppointments.add(appointment);
            }
        } catch (SQLException e) {
            System.err.println("SQL error during fetching pending appointments: " + e.getMessage());
            e.printStackTrace();
        }
        return pendingAppointments;
    }
    
    public static boolean markAppointmentAsDone(int appointmentId) {
        String sql = "UPDATE appointment SET appointment_done = 1 WHERE idappointment = ?";

        try (Connection conn = Dbh.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, appointmentId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Appointment marked as done successfully.");
                return true;
            } else {
                System.out.println("No appointment was marked as done.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("SQL error during updating appointment status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    
}
