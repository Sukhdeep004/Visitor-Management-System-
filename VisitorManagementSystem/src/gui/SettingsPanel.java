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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import database.DatabaseConnection;
import models.Department;
import utils.UIUtils;

public class SettingsPanel extends JPanel {
    private JTextField txtDepartmentName;
    private JButton btnAddDepartment;
    private JButton btnClear;
    private JButton btnDeleteDepartment;
    private JTable tblDepartments;
    private DefaultTableModel tableModel;
    
    public SettingsPanel() {
        initComponents();
        loadDepartments();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(UIUtils.PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lblTitle = new JLabel("Settings");
        lblTitle.setFont(UIUtils.TITLE_FONT);
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        
        // Department panel
        JPanel departmentPanel = UIUtils.createTitledPanel("Add Department");
        departmentPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel lblDepartmentName = new JLabel("Department Name:");
        lblDepartmentName.setFont(UIUtils.REGULAR_FONT);
        lblDepartmentName.setForeground(UIUtils.TEXT_COLOR);
        
        txtDepartmentName = new JTextField(20);
        txtDepartmentName.setFont(UIUtils.REGULAR_FONT);
        txtDepartmentName.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        btnAddDepartment = new JButton("Add Department");
        btnAddDepartment.setFont(new Font("Arial", Font.BOLD, 14));
        btnAddDepartment.setBackground(new Color(40, 167, 69)); // Bootstrap green
        btnAddDepartment.setForeground(Color.WHITE);
        btnAddDepartment.setFocusPainted(false);
        btnAddDepartment.setOpaque(true);
        btnAddDepartment.setBorderPainted(false);
        
        btnClear = new JButton("Clear");
        btnClear.setFont(new Font("Arial", Font.BOLD, 14));
        btnClear.setBackground(new Color(220, 53, 69)); 
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.setOpaque(true);
        btnClear.setBorderPainted(false);
                    
        // Add components to department panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        departmentPanel.add(lblDepartmentName, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        departmentPanel.add(txtDepartmentName, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnAddDepartment);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 8, 8);
        departmentPanel.add(buttonPanel, gbc);
        
        // Table panel
        JPanel tablePanel = UIUtils.createTitledPanel("Department List");
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
        tableModel.addColumn("Department Name");
        tableModel.addColumn("Created At");
        
        // Create table with the model
        tblDepartments = new JTable(tableModel);
        tblDepartments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDepartments.getTableHeader().setReorderingAllowed(false);
        tblDepartments.setRowHeight(25);
        tblDepartments.setFont(UIUtils.REGULAR_FONT);
        tblDepartments.setGridColor(UIUtils.LIGHT_TEXT_COLOR);
        tblDepartments.setShowVerticalLines(true);
        tblDepartments.setShowHorizontalLines(true);
        
        // Style the table header
        JTableHeader header = tblDepartments.getTableHeader();
        header.setBackground(UIUtils.PRIMARY_COLOR);
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBorder(BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR.darker(), 1));
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(tblDepartments);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add delete button below the table
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(UIUtils.PANEL_BACKGROUND);
        
        btnDeleteDepartment = new JButton("Delete Selected Department");
        btnDeleteDepartment.setFont(new Font("Arial", Font.BOLD, 14));
        btnDeleteDepartment.setBackground(new Color(220, 53, 69)); 
        btnDeleteDepartment.setForeground(Color.WHITE);
        btnDeleteDepartment.setFocusPainted(false);
        btnDeleteDepartment.setOpaque(true);
        btnDeleteDepartment.setBorderPainted(false);
        
        actionPanel.add(btnDeleteDepartment);
        tablePanel.add(actionPanel, BorderLayout.SOUTH);
        
        // Create a split panel for form and table
        JPanel contentPanel = new JPanel(new BorderLayout(10, 15));
        contentPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        contentPanel.add(departmentPanel, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Add panels to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        
        // Add action listeners
        btnAddDepartment.addActionListener(e -> addDepartment());
        btnClear.addActionListener(e -> clearForm());
        btnDeleteDepartment.addActionListener(e -> deleteDepartment());
    }
    
    private void loadDepartments() {
        // Clear table
        tableModel.setRowCount(0);
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM departments ORDER BY name";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("created_at")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading departments: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addDepartment() {
        String departmentName = txtDepartmentName.getText().trim();
        
        if (departmentName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter department name", 
                                         "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Department department = new Department();
        department.setName(departmentName);
        
        if (department.save()) {
            JOptionPane.showMessageDialog(this, "Department added successfully!", 
                                         "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadDepartments();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add department. Department may already exist.", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        txtDepartmentName.setText("");
        txtDepartmentName.requestFocus();
    }
    
    private void deleteDepartment() {
        int selectedRow = tblDepartments.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a department to delete", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int departmentId = (int) tableModel.getValueAt(selectedRow, 0);
        String departmentName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int option = JOptionPane.showConfirmDialog(this, 
                                                 "Are you sure you want to delete department: " + departmentName + "?", 
                                                 "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            if (Department.deleteDepartment(departmentId)) {
                JOptionPane.showMessageDialog(this, "Department deleted successfully!", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDepartments(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, 
                                             "Cannot delete department. It may be in use by visitors or hosts.", 
                                             "Delete Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
