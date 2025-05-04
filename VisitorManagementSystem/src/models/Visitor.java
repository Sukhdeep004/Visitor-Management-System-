package models;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class Visitor {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String purpose;
    private String hostName;
    private String department;
    private Timestamp checkInTime;
    private Timestamp checkOutTime;
    private String status;
    
    public Visitor() {}
    
    public Visitor(int id, String name, String phone, String email, String address, 
                  String purpose, String hostName, String department, 
                  Timestamp checkInTime, Timestamp checkOutTime, String status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.purpose = purpose;
        this.hostName = hostName;
        this.department = department;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    
    public String getHostName() { return hostName; }
    public void setHostName(String hostName) { this.hostName = hostName; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public Timestamp getCheckInTime() { return checkInTime; }
    public void setCheckInTime(Timestamp checkInTime) { this.checkInTime = checkInTime; }
    
    public Timestamp getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(Timestamp checkOutTime) { this.checkOutTime = checkOutTime; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    // Save visitor to database
    public boolean save() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO visitors (name, phone, email, address, purpose, host_name, " +
                          "department, check_in_time, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.setString(4, address);
            pstmt.setString(5, purpose);
            pstmt.setString(6, hostName);
            pstmt.setString(7, department);
            pstmt.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(9, "Checked In");
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saving visitor: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // Update visitor information
    public boolean update() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE visitors SET name = ?, phone = ?, email = ?, address = ?, " +
                          "purpose = ?, host_name = ?, department = ? WHERE id = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.setString(4, address);
            pstmt.setString(5, purpose);
            pstmt.setString(6, hostName);
            pstmt.setString(7, department);
            pstmt.setInt(8, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating visitor: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // Delete visitor
    public static boolean deleteVisitor(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM visitors WHERE id = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting visitor: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // Check out visitor
    public boolean checkOut() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE visitors SET check_out_time = ?, status = ? WHERE id = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(2, "Checked Out");
            pstmt.setInt(3, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error checking out visitor: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // Get all visitors
    public static List<Visitor> getAllVisitors() {
        List<Visitor> visitors = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM visitors ORDER BY created_at DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Visitor visitor = new Visitor(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("purpose"),
                    rs.getString("host_name"),
                    rs.getString("department"),
                    rs.getTimestamp("check_in_time"),
                    rs.getTimestamp("check_out_time"),
                    rs.getString("status")
                );
                visitors.add(visitor);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving visitors: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return visitors;
    }
    
    // Get active visitors (checked in but not checked out)
    public static List<Visitor> getActiveVisitors() {
        List<Visitor> visitors = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM visitors WHERE status = 'Checked In' ORDER BY check_in_time DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Visitor visitor = new Visitor(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("purpose"),
                    rs.getString("host_name"),
                    rs.getString("department"),
                    rs.getTimestamp("check_in_time"),
                    rs.getTimestamp("check_out_time"),
                    rs.getString("status")
                );
                visitors.add(visitor);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving active visitors: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return visitors;
    }
    
    // Get visitor by ID
    public static Visitor getVisitorById(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM visitors WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Visitor(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("purpose"),
                    rs.getString("host_name"),
                    rs.getString("department"),
                    rs.getTimestamp("check_in_time"),
                    rs.getTimestamp("check_out_time"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving visitor: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return null;
    }
    
    // Search visitors by name or phone
    public static List<Visitor> searchVisitors(String searchTerm) {
        List<Visitor> visitors = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM visitors WHERE name LIKE ? OR phone LIKE ? ORDER BY created_at DESC";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, "%" + searchTerm + "%");
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Visitor visitor = new Visitor(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("purpose"),
                    rs.getString("host_name"),
                    rs.getString("department"),
                    rs.getTimestamp("check_in_time"),
                    rs.getTimestamp("check_out_time"),
                    rs.getString("status")
                );
                visitors.add(visitor);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error searching visitors: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return visitors;
    }
}
