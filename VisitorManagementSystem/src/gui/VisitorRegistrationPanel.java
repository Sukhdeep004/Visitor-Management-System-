package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import models.Department;
import models.Visitor;
import utils.UIUtils;

public class VisitorRegistrationPanel extends JPanel {
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextArea txtAddress;
    private JTextArea txtPurpose;
    private JTextField txtHostName;
    private JComboBox<Department> cmbDepartment;
    private JButton btnRegister;
    private JButton btnClear;
    
    public VisitorRegistrationPanel() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(UIUtils.PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lblTitle = new JLabel("Visitor Registration");
        lblTitle.setFont(UIUtils.TITLE_FONT);
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        
        // Form panel
        JPanel formContainer = UIUtils.createTitledPanel("Visitor Information");
        formContainer.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIUtils.PANEL_BACKGROUND);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Initialize form components
        JLabel lblName = new JLabel("Full Name:");
        lblName.setFont(UIUtils.REGULAR_FONT);
        lblName.setForeground(UIUtils.TEXT_COLOR);
        
        txtName = new JTextField(20);
        txtName.setFont(UIUtils.REGULAR_FONT);
        txtName.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JLabel lblPhone = new JLabel("Phone Number:");
        lblPhone.setFont(UIUtils.REGULAR_FONT);
        lblPhone.setForeground(UIUtils.TEXT_COLOR);
        
        txtPhone = new JTextField(20);
        txtPhone.setFont(UIUtils.REGULAR_FONT);
        txtPhone.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(UIUtils.REGULAR_FONT);
        lblEmail.setForeground(UIUtils.TEXT_COLOR);
        
        txtEmail = new JTextField(20);
        txtEmail.setFont(UIUtils.REGULAR_FONT);
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setFont(UIUtils.REGULAR_FONT);
        lblAddress.setForeground(UIUtils.TEXT_COLOR);
        
        txtAddress = new JTextArea(3, 20);
        txtAddress.setFont(UIUtils.REGULAR_FONT);
        txtAddress.setLineWrap(true);
        txtAddress.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JScrollPane scrollAddress = new JScrollPane(txtAddress);
        
        JLabel lblPurpose = new JLabel("Purpose of Visit:");
        lblPurpose.setFont(UIUtils.REGULAR_FONT);
        lblPurpose.setForeground(UIUtils.TEXT_COLOR);
        
        txtPurpose = new JTextArea(3, 20);
        txtPurpose.setFont(UIUtils.REGULAR_FONT);
        txtPurpose.setLineWrap(true);
        txtPurpose.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JScrollPane scrollPurpose = new JScrollPane(txtPurpose);
        
        JLabel lblHostName = new JLabel("Host Name:");
        lblHostName.setFont(UIUtils.REGULAR_FONT);
        lblHostName.setForeground(UIUtils.TEXT_COLOR);
        
        txtHostName = new JTextField(20);
        txtHostName.setFont(UIUtils.REGULAR_FONT);
        txtHostName.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JLabel lblDepartment = new JLabel("Department:");
        lblDepartment.setFont(UIUtils.REGULAR_FONT);
        lblDepartment.setForeground(UIUtils.TEXT_COLOR);
        
        cmbDepartment = new JComboBox<>();
        cmbDepartment.setFont(UIUtils.REGULAR_FONT);
        cmbDepartment.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        loadDepartments();
        
        // Add components to form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(lblName, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        formPanel.add(txtName, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        formPanel.add(lblPhone, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtPhone, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        formPanel.add(lblEmail, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.7;
        formPanel.add(txtEmail, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        formPanel.add(lblAddress, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.7;
        formPanel.add(scrollAddress, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        formPanel.add(lblPurpose, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 0.7;
        formPanel.add(scrollPurpose, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        formPanel.add(lblHostName, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 0.7;
        formPanel.add(txtHostName, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        formPanel.add(lblDepartment, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 0.7;
        formPanel.add(cmbDepartment, gbc);
        
        formContainer.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(UIUtils.PANEL_BACKGROUND);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnRegister = new JButton("Register Visitor");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setBackground(new Color(40, 167, 69)); // Bootstrap-style Green
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setOpaque(true);
        btnRegister.setBorderPainted(false);

        btnClear = new JButton("Clear Form");
        btnClear.setFont(new Font("Arial", Font.BOLD, 14));
        btnClear.setBackground(new Color(220, 53, 69)); // Modern red
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.setOpaque(true);
        btnClear.setBorderPainted(false);

        buttonPanel.add(btnClear);
        buttonPanel.add(btnRegister);
        
        formContainer.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add panels to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(formContainer, BorderLayout.CENTER);
        
        // Add action listeners
        btnRegister.addActionListener(e -> registerVisitor());
        btnClear.addActionListener(e -> clearForm());
    }
    
    private void loadDepartments() {
        List<Department> departments = Department.getAllDepartments();
        for (Department department : departments) {
            cmbDepartment.addItem(department);
        }
    }
    
    private void registerVisitor() {
        // Validate form
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter visitor name", 
                                         "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtName.requestFocus();
            return;
        }
        
        if (txtPhone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter phone number", 
                                         "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtPhone.requestFocus();
            return;
        }
        
        if (txtHostName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter host name", 
                                         "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtHostName.requestFocus();
            return;
        }
        
        // Create visitor object
        Visitor visitor = new Visitor();
        visitor.setName(txtName.getText().trim());
        visitor.setPhone(txtPhone.getText().trim());
        visitor.setEmail(txtEmail.getText().trim());
        visitor.setAddress(txtAddress.getText().trim());
        visitor.setPurpose(txtPurpose.getText().trim());
        visitor.setHostName(txtHostName.getText().trim());
        
        Department selectedDepartment = (Department) cmbDepartment.getSelectedItem();
        if (selectedDepartment != null) {
            visitor.setDepartment(selectedDepartment.getName());
        }
        
        // Save visitor
        if (visitor.save()) {
            JOptionPane.showMessageDialog(this, "Visitor registered successfully!", 
                                         "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to register visitor", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtPurpose.setText("");
        txtHostName.setText("");
        if (cmbDepartment.getItemCount() > 0) {
            cmbDepartment.setSelectedIndex(0);
        }
        txtName.requestFocus();
    }
}
