package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import database.DatabaseConnection;
import models.User;
import utils.UIUtils;

public class UserManagementPanel extends JPanel {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JButton btnAddUser;
    private JButton btnClear;
    private JButton btnDeleteUser;
    private JTable tblUsers;
    private DefaultTableModel tableModel;
    
    public UserManagementPanel() {
        initComponents();
        loadUsers();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(UIUtils.PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lblTitle = new JLabel("User Management");
        lblTitle.setFont(UIUtils.TITLE_FONT);
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        
        // Form panel
        JPanel formPanel = UIUtils.createTitledPanel("Add New User");
        formPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(UIUtils.REGULAR_FONT);
        lblUsername.setForeground(UIUtils.TEXT_COLOR);
        
        txtUsername = new JTextField(15);
        txtUsername.setFont(UIUtils.REGULAR_FONT);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(UIUtils.REGULAR_FONT);
        lblPassword.setForeground(UIUtils.TEXT_COLOR);
        
        txtPassword = new JPasswordField(15);
        txtPassword.setFont(UIUtils.REGULAR_FONT);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JLabel lblRole = new JLabel("Role:");
        lblRole.setFont(UIUtils.REGULAR_FONT);
        lblRole.setForeground(UIUtils.TEXT_COLOR);
        
        cmbRole = new JComboBox<>(new String[]{"admin", "receptionist"});
        cmbRole.setFont(UIUtils.REGULAR_FONT);
        cmbRole.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        btnAddUser = new JButton("Add User");
        btnAddUser.setFont(new Font("Arial", Font.BOLD, 14));
btnAddUser.setBackground(new Color(40, 167, 69)); // Bootstrap green
btnAddUser.setForeground(Color.WHITE);
btnAddUser.setFocusPainted(false);
btnAddUser.setOpaque(true);
btnAddUser.setBorderPainted(false);
        
        btnClear = new JButton("Clear");
        btnClear.setFont(new Font("Arial", Font.BOLD, 14));
        btnClear.setBackground(new Color(220, 53, 69)); 
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.setOpaque(true);
        btnClear.setBorderPainted(false);
                
        // Add components to form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(lblUsername, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        formPanel.add(txtUsername, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        formPanel.add(lblPassword, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtPassword, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        formPanel.add(lblRole, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.7;
        formPanel.add(cmbRole, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnAddUser);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 8, 8);
        formPanel.add(buttonPanel, gbc);
        
        // Table panel
        JPanel tablePanel = UIUtils.createTitledPanel("User List");
        tablePanel.setLayout(new BorderLayout(10, 10));
        
        // Create table model with non-editable cells
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Add columns to table model
        tableModel.addColumn("ID");
        tableModel.addColumn("Username");
        tableModel.addColumn("Role");
        tableModel.addColumn("Created At");
        
        // Create table with the model
        tblUsers = new JTable(tableModel);
        tblUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblUsers.getTableHeader().setReorderingAllowed(false);
        tblUsers.setRowHeight(25);
        tblUsers.setFont(UIUtils.REGULAR_FONT);
        tblUsers.setGridColor(UIUtils.LIGHT_TEXT_COLOR);
        tblUsers.setShowVerticalLines(true);
        tblUsers.setShowHorizontalLines(true);
        
        // Style the table header
        JTableHeader header = tblUsers.getTableHeader();
        header.setBackground(UIUtils.PRIMARY_COLOR);
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBorder(BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR.darker(), 1));
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(tblUsers);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add delete button below the table
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(UIUtils.PANEL_BACKGROUND);
        
        btnDeleteUser = new JButton("Delete Selected User");
        btnDeleteUser.setFont(new Font("Arial", Font.BOLD, 14));
        btnDeleteUser.setBackground(new Color(220, 53, 69)); // Bootstrap green
        btnDeleteUser.setForeground(Color.WHITE);
        btnDeleteUser.setFocusPainted(false);
        btnDeleteUser.setOpaque(true);
        btnDeleteUser.setBorderPainted(false);
        
        actionPanel.add(btnDeleteUser);
        tablePanel.add(actionPanel, BorderLayout.SOUTH);
        
        // Create a split panel for form and table
        JPanel contentPanel = new JPanel(new BorderLayout(10, 15));
        contentPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        contentPanel.add(formPanel, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Add panels to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        
        // Add action listeners
        btnAddUser.addActionListener(e -> addUser());
        btnClear.addActionListener(e -> clearForm());
        btnDeleteUser.addActionListener(e -> deleteUser());
    }
    
    private void loadUsers() {
        // Clear table
        tableModel.setRowCount(0);
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users ORDER BY username";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("role"),
                    rs.getString("created_at")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String role = (String) cmbRole.getSelectedItem();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password", 
                                         "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        
        if (user.save()) {
            JOptionPane.showMessageDialog(this, "User added successfully!", 
                                         "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add user. Username may already exist.", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        cmbRole.setSelectedIndex(0);
        txtUsername.requestFocus();
    }
    
    private void deleteUser() {
        int selectedRow = tblUsers.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        
        int option = JOptionPane.showConfirmDialog(this, 
                                                 "Are you sure you want to delete user: " + username + "?", 
                                                 "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            if (User.deleteUser(userId)) {
                JOptionPane.showMessageDialog(this, "User deleted successfully!", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUsers(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, 
                                             "Cannot delete user. This may be the last admin user.", 
                                             "Delete Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
