package gui;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import database.DatabaseConnection;
import utils.UIUtils;

public class ReportsPanel extends JPanel {
    private JComboBox<String> cmbReportType;
    private JButton btnGenerateReport;
    private JTable tblReport;
    private DefaultTableModel tableModel;

    public ReportsPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(UIUtils.PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("Reports");
        lblTitle.setFont(UIUtils.TITLE_FONT);
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(lblTitle, BorderLayout.CENTER);

        // Controls Panel
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlsPanel.setOpaque(false);

        JLabel lblReportType = new JLabel("Report Type:");
        lblReportType.setFont(UIUtils.REGULAR_FONT);
        lblReportType.setForeground(UIUtils.TEXT_COLOR);

        cmbReportType = new JComboBox<>(new String[]{
            "Daily Visitors",
            "Monthly Visitors",
            "Department-wise Visitors",
            "Visitors by Status"
        });
        cmbReportType.setFont(UIUtils.REGULAR_FONT);
        cmbReportType.setPreferredSize(new Dimension(220, 30));

        btnGenerateReport = new JButton("Generate Report");
        btnGenerateReport.setFont(UIUtils.REGULAR_FONT);
        btnGenerateReport.setBackground(new Color(59, 89, 182));
        btnGenerateReport.setForeground(Color.BLACK);
        btnGenerateReport.setFocusPainted(false);
        btnGenerateReport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerateReport.setPreferredSize(new Dimension(160, 30));
        btnGenerateReport.setBorder(BorderFactory.createLineBorder(new Color(30, 60, 150)));

        controlsPanel.add(lblReportType);
        controlsPanel.add(cmbReportType);
        controlsPanel.add(btnGenerateReport);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(UIUtils.PANEL_BACKGROUND);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Report Output"));

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblReport = new JTable(tableModel);
        tblReport.setFont(UIUtils.REGULAR_FONT);
        tblReport.setRowHeight(25);
        tblReport.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblReport.getTableHeader().setBackground(UIUtils.PRIMARY_COLOR);
        tblReport.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(tblReport);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.PRIMARY_COLOR));

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add components to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(controlsPanel, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);

        // Action
        btnGenerateReport.addActionListener(e -> generateReport());
    }

    private void generateReport() {
        String reportType = (String) cmbReportType.getSelectedItem();
        if (reportType == null) return;

        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();

            switch (reportType) {
                case "Daily Visitors":
                    generateDailyVisitorsReport(stmt);
                    break;
                case "Monthly Visitors":
                    generateMonthlyVisitorsReport(stmt);
                    break;
                case "Department-wise Visitors":
                    generateDepartmentWiseReport(stmt);
                    break;
                case "Visitors by Status":
                    generateStatusWiseReport(stmt);
                    break;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage(),
                    "Report Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateDailyVisitorsReport(Statement stmt) {
        try {
            tableModel.addColumn("Date");
            tableModel.addColumn("Total Visitors");
            tableModel.addColumn("Checked In");
            tableModel.addColumn("Checked Out");

            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            String query = "SELECT COUNT(*) as total, " +
                    "SUM(CASE WHEN status = 'Checked In' THEN 1 ELSE 0 END) as checked_in, " +
                    "SUM(CASE WHEN status = 'Checked Out' THEN 1 ELSE 0 END) as checked_out " +
                    "FROM visitors WHERE date(check_in_time) = '" + today + "'";

            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                tableModel.addRow(new Object[]{
                        today,
                        rs.getInt("total"),
                        rs.getInt("checked_in"),
                        rs.getInt("checked_out")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating daily report: " + e.getMessage(),
                    "Report Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateMonthlyVisitorsReport(Statement stmt) {
        try {
            tableModel.addColumn("Month");
            tableModel.addColumn("Total Visitors");

            String query = "SELECT strftime('%Y-%m', check_in_time) AS month, COUNT(*) AS total " +
                    "FROM visitors " +
                    "WHERE check_in_time IS NOT NULL " +
                    "GROUP BY strftime('%Y-%m', check_in_time) " +
                    "ORDER BY strftime('%Y-%m', check_in_time) DESC";

            ResultSet rs = stmt.executeQuery(query);

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM yyyy");

            while (rs.next()) {
                String rawMonth = rs.getString("month");
                String formattedMonth;
                try {
                    Date date = inputFormat.parse(rawMonth);
                    formattedMonth = outputFormat.format(date);
                } catch (Exception ex) {
                    formattedMonth = rawMonth; // fallback if parsing fails
                }

                tableModel.addRow(new Object[]{
                        formattedMonth,
                        rs.getInt("total")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating monthly report: " + e.getMessage(),
                    "Report Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateDepartmentWiseReport(Statement stmt) {
        try {
            tableModel.addColumn("Department");
            tableModel.addColumn("Total Visitors");

            String query = "SELECT department, COUNT(*) as total FROM visitors GROUP BY department ORDER BY total DESC";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("department"),
                        rs.getInt("total")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating department-wise report: " + e.getMessage(),
                    "Report Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateStatusWiseReport(Statement stmt) {
        try {
            tableModel.addColumn("Status");
            tableModel.addColumn("Count");

            String query = "SELECT status, COUNT(*) as count FROM visitors GROUP BY status";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("status"),
                        rs.getInt("count")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating status-wise report: " + e.getMessage(),
                    "Report Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
