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

public class Department {
    private int id;
    private String name;
    
    public Department() {}
    
    public Department(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    // Get all departments
    public static List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM departments ORDER BY name";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Department department = new Department(
                    rs.getInt("id"),
                    rs.getString("name")
                );
                departments.add(department);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving departments: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return departments;
    }
    
    // Save department
    public boolean save() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO departments (name) VALUES (?)";
            
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, name);
            
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
            JOptionPane.showMessageDialog(null, "Error saving department: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // Get department by ID
    public static Department getDepartmentById(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM departments WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Department(
                    rs.getInt("id"),
                    rs.getString("name")
                );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving department: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return null;
    }
    
    // Delete department
    public static boolean deleteDepartment(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // First check if department is in use
            String checkQuery = "SELECT COUNT(*) FROM visitors WHERE department = (SELECT name FROM departments WHERE id = ?)";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                // Department is in use by visitors
                return false;
            }
            
            // Also check if department is in use by hosts
            checkQuery = "SELECT COUNT(*) FROM hosts WHERE department_id = ?";
            checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, id);
            rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                // Department is in use by hosts
                return false;
            }
            
            // If not in use, delete the department
            String deleteQuery = "DELETE FROM departments WHERE id = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, id);
            
            int rowsAffected = deleteStmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting department: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    @Override
    public String toString() {
        return name;
    }
}
