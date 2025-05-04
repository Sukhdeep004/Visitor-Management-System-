package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import models.Visitor;
import utils.UIUtils;

public class VisitorLogsPanel extends JPanel {
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnRefresh;
    private JButton btnExportCSV;
    private JButton btnDeleteVisitor;
    private JButton btnEditVisitor;
    private JTable tblVisitors;
    private DefaultTableModel tableModel;
    
    public VisitorLogsPanel() {
        initComponents();
        loadVisitors();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(UIUtils.PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lblTitle = new JLabel("Visitor Logs");
        lblTitle.setFont(UIUtils.TITLE_FONT);
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        
        // Search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
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
        
        btnExportCSV = new JButton("Export to CSV");
        UIUtils.styleButton(btnExportCSV, UIUtils.PRIMARY_COLOR, Color.BLACK);
        
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        searchPanel.add(btnExportCSV);
        
        // Table panel
        JPanel tablePanel = UIUtils.createTitledPanel("All Visitors");
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
        tableModel.addColumn("Check-out Time");
        tableModel.addColumn("Status");
        
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
        tblVisitors.getColumnModel().getColumn(6).setPreferredWidth(150);
        tblVisitors.getColumnModel().getColumn(7).setPreferredWidth(100);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(tblVisitors);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR, 1));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(UIUtils.PANEL_BACKGROUND);
        
        btnDeleteVisitor = new JButton("Delete Visitor");
        btnDeleteVisitor.setFont(new Font("Arial", Font.BOLD, 14));
        btnDeleteVisitor.setBackground(new Color(220, 53, 69)); 
        btnDeleteVisitor.setForeground(Color.WHITE);
        btnDeleteVisitor.setFocusPainted(false);
        btnDeleteVisitor.setOpaque(true);
        btnDeleteVisitor.setBorderPainted(false);
        
        btnEditVisitor = new JButton("Edit Visitor");
        btnEditVisitor.setFont(new Font("Arial", Font.BOLD, 14));
        btnEditVisitor.setBackground(new Color(40, 167, 69)); // Bootstrap green
        btnEditVisitor.setForeground(Color.WHITE);
        btnEditVisitor.setFocusPainted(false);
        btnEditVisitor.setOpaque(true);
        btnEditVisitor.setBorderPainted(false);
        
        actionPanel.add(btnDeleteVisitor);
        actionPanel.add(btnEditVisitor);
        tablePanel.add(actionPanel, BorderLayout.SOUTH);
        
        // Add panels to main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIUtils.BACKGROUND_COLOR);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        
        // Add action listeners
        btnSearch.addActionListener(e -> searchVisitors());
        btnRefresh.addActionListener(e -> loadVisitors());
        btnExportCSV.addActionListener(e -> exportToCSV());
        btnDeleteVisitor.addActionListener(e -> deleteVisitor());
        btnEditVisitor.addActionListener(e -> editVisitor());
    }
    
    private void loadVisitors() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Get all visitors
        List<Visitor> visitors = Visitor.getAllVisitors();
        
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
                visitor.getCheckOutTime() != null ? sdf.format(visitor.getCheckOutTime()) : "",
                visitor.getStatus()
            });
        }
    }
    
    private void searchVisitors() {
        String searchTerm = txtSearch.getText().trim();
        if (searchTerm.isEmpty()) {
            loadVisitors();
            return;
        }
        
        // Clear table
        tableModel.setRowCount(0);
        
        // Search visitors
        List<Visitor> visitors = Visitor.searchVisitors(searchTerm);
        
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
                visitor.getCheckOutTime() != null ? sdf.format(visitor.getCheckOutTime()) : "",
                visitor.getStatus()
            });
        }
    }
    
    private void exportToCSV() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No data to export", 
                                         "Export Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files (*.csv)", "csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }
            
            try (FileWriter writer = new FileWriter(file)) {
                // Write header
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    writer.append(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
                
                // Write data
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        if (tableModel.getValueAt(i, j) != null) {
                            writer.append(tableModel.getValueAt(i, j).toString());
                        }
                        if (j < tableModel.getColumnCount() - 1) {
                            writer.append(",");
                        }
                    }
                    writer.append("\n");
                }
                
                JOptionPane.showMessageDialog(this, "Data exported successfully to " + file.getAbsolutePath(), 
                                             "Export Success", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error exporting data: " + e.getMessage(), 
                                             "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteVisitor() {
        int selectedRow = tblVisitors.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a visitor to delete", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int visitorId = (int) tableModel.getValueAt(selectedRow, 0);
        String visitorName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int option = JOptionPane.showConfirmDialog(this, 
                                                 "Are you sure you want to delete visitor: " + visitorName + "?", 
                                                 "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            if (Visitor.deleteVisitor(visitorId)) {
                JOptionPane.showMessageDialog(this, "Visitor deleted successfully!", 
                                             "Success", JOptionPane.INFORMATION_MESSAGE);
                loadVisitors(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete visitor", 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editVisitor() {
        int selectedRow = tblVisitors.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a visitor to edit", 
                                         "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int visitorId = (int) tableModel.getValueAt(selectedRow, 0);
        Visitor visitor = Visitor.getVisitorById(visitorId);
        
        if (visitor != null) {
            EditVisitorDialog dialog = new EditVisitorDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this), visitor);
            dialog.setVisible(true);
            
            if (dialog.isSaved()) {
                loadVisitors(); // Refresh the table
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to load visitor information", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
