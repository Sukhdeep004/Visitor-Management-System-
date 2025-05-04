package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import models.User;
import utils.UIUtils;

public class MainDashboard extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel lblDateTime;
    private JLabel lblUserInfo;
    private Timer timer;

    // Panels for different sections
    private VisitorRegistrationPanel visitorRegistrationPanel;
    private VisitorCheckoutPanel visitorCheckoutPanel;
    private VisitorLogsPanel visitorLogsPanel;
    private ReportsPanel reportsPanel;
    private UserManagementPanel userManagementPanel;
    private SettingsPanel settingsPanel;

    public MainDashboard(User user) {
        this.currentUser = user;
        initComponents();
        startClock();
    }

    private void initComponents() {
        setTitle("Visitor Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 700));
        getContentPane().setBackground(UIUtils.BACKGROUND_COLOR);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = createHeaderPanel();
        JPanel sidebarPanel = createSidebarPanel();

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        visitorRegistrationPanel = new VisitorRegistrationPanel();
        visitorCheckoutPanel = new VisitorCheckoutPanel();
        visitorLogsPanel = new VisitorLogsPanel();
        reportsPanel = new ReportsPanel();

        contentPanel.add(visitorRegistrationPanel, "VisitorRegistration");
        contentPanel.add(visitorCheckoutPanel, "VisitorCheckout");
        contentPanel.add(visitorLogsPanel, "VisitorLogs");
        contentPanel.add(reportsPanel, "Reports");

        if ("admin".equals(currentUser.getRole())) {
            userManagementPanel = new UserManagementPanel();
            settingsPanel = new SettingsPanel();
            contentPanel.add(userManagementPanel, "UserManagement");
            contentPanel.add(settingsPanel, "Settings");
        }

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        cardLayout.show(contentPanel, "VisitorRegistration");
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBackground(UIUtils.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, UIUtils.PRIMARY_COLOR.darker()),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel("Visitor Management System");
        lblTitle.setFont(UIUtils.TITLE_FONT);
        lblTitle.setForeground(Color.BLACK);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        infoPanel.setOpaque(false);

        lblUserInfo = new JLabel("User: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        lblUserInfo.setFont(UIUtils.REGULAR_FONT);
        lblUserInfo.setForeground(Color.BLACK);
        lblUserInfo.setHorizontalAlignment(SwingConstants.RIGHT);

        lblDateTime = new JLabel();
        lblDateTime.setFont(UIUtils.REGULAR_FONT);
        lblDateTime.setForeground(Color.BLACK);
        lblDateTime.setHorizontalAlignment(SwingConstants.RIGHT);

        infoPanel.add(lblUserInfo);
        infoPanel.add(lblDateTime);

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(infoPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        sidebarPanel.setBackground(UIUtils.SECONDARY_COLOR);
        sidebarPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 2, UIUtils.SECONDARY_COLOR.darker()),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        sidebarPanel.setPreferredSize(new Dimension(250, 0));

        JButton btnVisitorRegistration = createMenuButton("Visitor Registration");
        JButton btnVisitorCheckout = createMenuButton("Visitor Checkout");
        JButton btnVisitorLogs = createMenuButton("Visitor Logs");
        JButton btnReports = createMenuButton("Reports");
        JButton btnUserManagement = createMenuButton("User Management");
        JButton btnSettings = createMenuButton("Settings");
        JButton btnLogout = createMenuButton("Logout");

        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.setBackground(new Color(217, 83, 74));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setOpaque(true);
        btnLogout.setBorderPainted(false);
        btnLogout.setHorizontalAlignment(SwingConstants.CENTER); // Center text

        btnVisitorRegistration.addActionListener(e -> cardLayout.show(contentPanel, "VisitorRegistration"));
        btnVisitorCheckout.addActionListener(e -> cardLayout.show(contentPanel, "VisitorCheckout"));
        btnVisitorLogs.addActionListener(e -> cardLayout.show(contentPanel, "VisitorLogs"));
        btnReports.addActionListener(e -> cardLayout.show(contentPanel, "Reports"));

        if ("admin".equals(currentUser.getRole())) {
            btnUserManagement.addActionListener(e -> cardLayout.show(contentPanel, "UserManagement"));
            btnSettings.addActionListener(e -> cardLayout.show(contentPanel, "Settings"));
        } else {
            btnUserManagement.setEnabled(false);
            btnSettings.setEnabled(false);
        }

        btnLogout.addActionListener(e -> logout());

        sidebarPanel.add(btnVisitorRegistration);
        sidebarPanel.add(btnVisitorCheckout);
        sidebarPanel.add(btnVisitorLogs);
        sidebarPanel.add(btnReports);
        sidebarPanel.add(btnUserManagement);
        sidebarPanel.add(btnSettings);

        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        sidebarPanel.add(spacer);
        sidebarPanel.add(btnLogout);

        return sidebarPanel;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        UIUtils.styleButton(button, new Color(100, 150, 200), Color.BLACK);
        button.setHorizontalAlignment(SwingConstants.CENTER); // Center text
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(220, 45));
        return button;
    }

    private void startClock() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClock();
            }
        });
        timer.start();
        updateClock(); // Update immediately
    }

    private void updateClock() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        lblDateTime.setText(sdf.format(new Date()));
    }

    private void logout() {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?",
                "Logout Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            timer.stop();
            this.dispose();
            new LoginForm().setVisible(true);
        }
    }
}
