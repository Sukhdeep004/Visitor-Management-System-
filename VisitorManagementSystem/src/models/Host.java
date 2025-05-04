package models;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Host {
    private int id;
    private String name;
    private int departmentId;
    private String contact;
    private String departmentName; // For display purposes
    
    public Host() {}
    
    public Host(int id, String name, int departmentId, String contact) {
        this.id = id;
        this.name = name;
        this.departmentId = departmentId;
        this.contact = contact;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
    
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    
    // Get all hosts with department names
    public static List<Host> getAllHosts() {
        List<Host> hosts = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT h.*, d.name as department_name FROM hosts h " +
                          "LEFT JOIN departments d ON h.department_id = d.id " +
                          "ORDER BY h.name";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Host host = new Host(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("department_id"),
                    rs.getString("contact")
                );
                host.setDepartmentName(rs.getString("department_name"));
                hosts.add(host);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving hosts: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return hosts;
    }
    
    // Get hosts by department
    public static List<Host> getHostsByDepartment(int departmentId) {
        List<Host> hosts = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT h.*, d.name as department_name FROM hosts h " +
                          "LEFT JOIN departments d ON h.department_id = d.id " +
                          "WHERE h.department_id = ? ORDER BY h.name";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, departmentId);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Host host = new Host(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("department_id"),
                    rs.getString("contact")
                );
                host.setDepartmentName(rs.getString("department_name"));
                hosts.add(host);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving hosts: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return hosts;
    }
    
    // Save host
    public boolean save() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO hosts (name, department_id, contact) VALUES (?, ?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, name);
            pstmt.setInt(2, departmentId);
            pstmt.setString(3, contact);
            
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
            JOptionPane.showMessageDialog(null, "Error saving host: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    @Override
    public String toString() {
        return name;
    }
}
