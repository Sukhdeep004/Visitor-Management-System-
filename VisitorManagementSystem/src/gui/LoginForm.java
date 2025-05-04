package gui;

import database.DatabaseConnection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import models.User;

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnExit;

    public LoginForm() {
        initComponents();
        initDatabase();
    }

    private void initDatabase() {
        DatabaseConnection.initializeDatabase();
    }

    private void initComponents() {
        setTitle("Visitor Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Visitor Management System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblSubtitle = new JLabel("Login to continue");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitle.setForeground(Color.WHITE);
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(lblTitle, BorderLayout.CENTER);
        headerPanel.add(lblSubtitle, BorderLayout.SOUTH);

        // Login panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsername.setForeground(Color.DARK_GRAY);

        txtUsername = new JTextField(15);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsername.setPreferredSize(new Dimension(200, 35));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPassword.setForeground(Color.DARK_GRAY);

        txtPassword = new JPasswordField(15);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(200, 35));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        // Styled Buttons
        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(70, 130, 180)); // Steel Blue
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setBorderPainted(false);
        
        btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Arial", Font.BOLD, 14));
        btnExit.setBackground(new Color(220, 53, 69)); // Red
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);
        btnExit.setOpaque(true);
        btnExit.setBorderPainted(false);
        // Add components
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(lblUsername, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        loginPanel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(txtPassword, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnExit);
        buttonPanel.add(btnLogin);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        loginPanel.add(buttonPanel, gbc);

        // Add to main
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Action listeners
        btnLogin.addActionListener(e -> login());
        btnExit.addActionListener(e -> System.exit(0));

        // Default button
        getRootPane().setDefaultButton(btnLogin);
    }

    private void login() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password",
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = User.authenticate(username, password);

        if (user != null) {
            this.dispose();
            new MainDashboard(user).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password",
                    "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
