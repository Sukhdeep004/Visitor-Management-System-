package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:./vms_database.db";
    private static Connection connection = null;
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("Database connection established");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "SQLite JDBC Driver not found: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection error: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error closing database connection: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            var stmt = conn.createStatement();
            
            // Create users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "username TEXT NOT NULL UNIQUE," +
                         "password TEXT NOT NULL," +
                         "role TEXT NOT NULL," +
                         "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            
            // Create visitors table
            stmt.execute("CREATE TABLE IF NOT EXISTS visitors (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "name TEXT NOT NULL," +
                         "phone TEXT," +
                         "email TEXT," +
                         "address TEXT," +
                         "purpose TEXT," +
                         "host_name TEXT," +
                         "department TEXT," +
                         "check_in_time TIMESTAMP," +
                         "check_out_time TIMESTAMP," +
                         "status TEXT," +
                         "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            
            // Create departments table
            stmt.execute("CREATE TABLE IF NOT EXISTS departments (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "name TEXT NOT NULL UNIQUE," +
                         "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            
            // Create hosts table
            stmt.execute("CREATE TABLE IF NOT EXISTS hosts (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "name TEXT NOT NULL," +
                         "department_id INTEGER," +
                         "contact TEXT," +
                         "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                         "FOREIGN KEY (department_id) REFERENCES departments(id))");
            
            // Insert default admin user if not exists
            var checkAdmin = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?");
            checkAdmin.setString(1, "admin");
            var rs = checkAdmin.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                var insertAdmin = conn.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, ?)");
                insertAdmin.setString(1, "admin");
                insertAdmin.setString(2, "admin123"); // In a real app, use password hashing
                insertAdmin.setString(3, "admin");
                insertAdmin.executeUpdate();
                System.out.println("Default admin user created");
            }
            
            // Insert some default departments
            String[] defaultDepts = {"IT", "HR", "Finance", "Marketing", "Operations"};
            for (String dept : defaultDepts) {
                try {
                    var insertDept = conn.prepareStatement("INSERT INTO departments (name) VALUES (?)");
                    insertDept.setString(1, dept);
                    insertDept.executeUpdate();
                } catch (SQLException e) {
                    // Ignore duplicate entry errors
                    if (!e.getMessage().contains("UNIQUE constraint failed")) {
                        throw e;
                    }
                }
            }
            
            System.out.println("Database initialized successfully");
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error initializing database: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
