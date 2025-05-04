package models;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    
    public User() {}
    
    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    // Authentication method
    public static User authenticate(String username, String password) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Authentication error: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
    // Create new user
    public boolean save() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error creating user: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // Delete user
    public static boolean deleteUser(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // First check if this is the last admin user
            if (isLastAdminUser(conn, id)) {
                return false;
            }
            
            String query = "DELETE FROM users WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting user: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // Check if this is the last admin user
    private static boolean isLastAdminUser(Connection conn, int userId) throws SQLException {
        // Get the role of the user to be deleted
        String roleQuery = "SELECT role FROM users WHERE id = ?";
        PreparedStatement roleStmt = conn.prepareStatement(roleQuery);
        roleStmt.setInt(1, userId);
        ResultSet roleRs = roleStmt.executeQuery();
        
        if (roleRs.next() && "admin".equals(roleRs.getString("role"))) {
            // Count the number of admin users
            String countQuery = "SELECT COUNT(*) FROM users WHERE role = 'admin'";
            PreparedStatement countStmt = conn.prepareStatement(countQuery);
            ResultSet countRs = countStmt.executeQuery();
            
            if (countRs.next() && countRs.getInt(1) <= 1) {
                // This is the last admin user
                return true;
            }
        }
        
        return false;
    }
}
