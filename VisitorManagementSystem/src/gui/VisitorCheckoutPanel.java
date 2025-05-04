package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.List;
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
import models.Visitor;
import utils.UIUtils;

public class VisitorCheckoutPanel extends JPanel {
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnRefresh;
    private JButton btnCheckout;
    private JTable tblVisitors;
    private DefaultTableModel tableModel;
    
    public VisitorCheckoutPanel() {
        initComponents();
        loadActiveVisitors();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(UIUtils.PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lblTitle = new JLabel("Visitor Checkout");
lblTitle.setFont(new Font("Arial", Font.BOLD, 22)); // Clean, modern font
lblTitle.setForeground(Color.BLACK);
lblTitle.setHorizontalAlignment(JLabel.CENTER);
titlePanel.add(lblTitle, BorderLayout.CENTER);

        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(UIUtils.PANEL_BACKGROUND);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, UIUtils.PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(UIUtils.REGULAR_FONT);
        lblSearch.setForeground(UIUtils.TEXT_COLOR);
        
        txtSearch = new JTextField(20);
        txtSearch.setFont(UIUtils.REGULAR_FONT);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        btnSearch = new JButton("Search");
        UIUtils.styleButton(btnSearch, UIUtils.PRIMARY_COLOR, Color.BLACK);
        
        btnRefresh = new JButton("Refresh");
        UIUtils.styleButton(btnRefresh, UIUtils.SECONDARY_COLOR, Color.BLACK);
        
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        
        // Table panel
        JPanel tablePanel = UIUtils.createTitledPanel("Active Visitors");
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
        tableModel.addColumn("Name");
        tableModel.addColumn("Phone");
        tableModel.addColumn("Host");
        tableModel.addColumn("Department");
        tableModel.addColumn("Check-in Time");
        tableModel.addColumn("Purpose");
        
        // Create table with the model
        tblVisitors = new JTable(tableModel);
        tblVisitors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblVisitors.getTableHeader().setReorderingAllowed(false);
        tblVisitors.setRowHeight(25);
        tblVisitors.setFont(UIUtils.REGULAR_FONT);
        tblVisitors.setGridColor(UIUtils.LIGHT_TEXT_COLOR);
        tblVisitors.setShowVerticalLines(true);
        tblVisitors.setShowHorizontalLines(true);
        
        // Style the table header
        JTableHeader header = tblVisitors.getTableHeader();
        header.setBackground(UIUtils.PRIMARY_COLOR);
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBorder(BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR.darker(), 1));
        
        // Set column widths
        tblVisitors.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblVisitors.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblVisitors.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblVisitors.getColumnModel().getColumn(3).setPreferredWidth(150);
        tblVisitors.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblVisitors.getColumnModel().getColumn(5).setPreferredWidth(150);
        tblVisitors.getColumnModel().getColumn(6).setPreferredWidth(200);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(tblVisitors);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
buttonPanel.setBackground(new Color(245, 245, 245)); // Light gray for a clean panel background

        
btnCheckout = new JButton("Checkout Selected Visitor");
btnCheckout.setFont(new Font("Arial", Font.BOLD, 14));
btnCheckout.setBackground(new Color(40, 167, 69)); // Bootstrap green
btnCheckout.setForeground(Color.WHITE);
btnCheckout.setFocusPainted(false);
btnCheckout.setOpaque(true);
btnCheckout.setBorderPainted(false);

        buttonPanel.add(btnCheckout);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add panels to main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        
        // Add action listeners
        btnSearch.addActionListener(e -> searchVisitors());
        btnRefresh.addActionListener(e -> loadActiveVisitors());
        btnCheckout.addActionListener(e -> checkoutVisitor());
    }
    
    private void loadActiveVisitors() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Get active visitors
        List<Visitor> visitors = Visitor.getActiveVisitors();
        
        // Add visitors to table
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Visitor visitor : visitors) {
            tableModel.addRow(new Object[]{
                visitor.getId(),
                visitor.getName(),
                visitor.getPhone(),
                visitor.getHostName(),
                visitor.getDepartment(),
                visitor.getCheckInTime() != null ? sdf.format(visitor.getCheckInTime()) : "",
                visitor.getPurpose()
            });
        }
    }
    
    private void searchVisitors() {
        String searchTerm = txtSearch.getText().trim();
        if (searchTerm.isEmpty()) {
            loadActiveVisitors();
            return;
        }
        
        // Clear table
        tableModel.setRowCount(0);
        
        // Search visitors
        List<Visitor> visitors = Visitor.searchVisitors(searchTerm);
        
        // Filter for active visitors only
        visitors.removeIf(visitor -> !"Checked In".equals(visitor.getStatus()));
        
        // Add visitors to table
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Visitor visitor : visitors) {
            tableModel.addRow(new Object[]{
                visitor.getId(),
                visitor.getName(),
                visitor.getPhone(),
                visitor.getHostName(),
                visitor.getDepartment(),
                visitor.getCheckInTime() != null ? sdf.format(visitor.getCheckInTime()) : "",
                visitor.getPurpose()
            });
        }
    }
    
    private void checkoutVisitor() {
        int selectedRow = tblVisitors.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a visitor to checkout", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int visitorId = (int) tableModel.getValueAt(selectedRow, 0);
        String visitorName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int option = JOptionPane.showConfirmDialog(this, 
                                                 "Are you sure you want to checkout " + visitorName + "?", 
                                                 "Checkout Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            Visitor visitor = Visitor.getVisitorById(visitorId);
            if (visitor != null && visitor.checkOut()) {
                JOptionPane.showMessageDialog(this, visitorName + " has been checked out successfully!", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                loadActiveVisitors();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to checkout visitor", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
