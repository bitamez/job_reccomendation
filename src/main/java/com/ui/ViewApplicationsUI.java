package com.mesi.jobai.ui;

import com.mesi.jobai.dao.AdminDAO;
import com.mesi.jobai.model.Admin;
import com.mesi.jobai.model.Application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewApplicationsUI extends JFrame {
    private Admin currentAdmin;
    private AdminDAO adminDAO;
    private JTable applicationsTable;
    private DefaultTableModel tableModel;

    public ViewApplicationsUI(Admin admin) {
        this.currentAdmin = admin;
        this.adminDAO = new AdminDAO();
        initializeComponents();
        loadApplications();
    }

    private void initializeComponents() {
        setTitle("View Applications - Admin Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 73, 94));
        JLabel titleLabel = new JLabel("View Applications", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        headerPanel.add(titleLabel);

        // Table Panel
        JPanel tablePanel = createTablePanel();

        // Button Panel
        JPanel buttonPanel = createButtonPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        String[] columnNames = {"Application ID", "Job ID", "User ID", "Status", "Applied Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        applicationsTable = new JTable(tableModel);
        applicationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        applicationsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        applicationsTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        applicationsTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        applicationsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        applicationsTable.getColumnModel().getColumn(4).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(applicationsTable);
        scrollPane.setPreferredSize(new Dimension(0, 450));

        // Info Panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel infoLabel = new JLabel("Applications Overview - Monitor all job applications in the system");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(127, 140, 141));
        infoPanel.add(infoLabel);

        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.BLACK);  // Black text
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> loadApplications());

        JButton exportButton = new JButton("Export to CSV");
        exportButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exportButton.setBackground(new Color(46, 204, 113));
        exportButton.setForeground(Color.BLACK);  // Black text
        exportButton.setFocusPainted(false);
        exportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportButton.addActionListener(e -> exportToCSV());

        JButton detailsButton = new JButton("View Details");
        detailsButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailsButton.setForeground(Color.BLACK);  // Black text
        detailsButton.setFocusPainted(false);
        detailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        detailsButton.addActionListener(e -> viewApplicationDetails());

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        closeButton.setForeground(Color.BLACK);  // Black text
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());

        panel.add(refreshButton);
        panel.add(exportButton);
        panel.add(detailsButton);
        panel.add(closeButton);

        return panel;
    }

    private void loadApplications() {
        tableModel.setRowCount(0);
        List<Application> applications = adminDAO.getAllApplications();
        
        for (Application application : applications) {
            Object[] row = {
                application.getId(),
                application.getJobId(),
                application.getApplicantId(),
                application.getStatus(),
                application.getAppliedAt()
            };
            tableModel.addRow(row);
        }

        // Update status bar
        updateStatusInfo(applications.size());
    }

    private void updateStatusInfo(int totalApplications) {
        // You could add a status bar here to show statistics
        setTitle("View Applications - Admin Panel (" + totalApplications + " applications)");
    }

    private void viewApplicationDetails() {
        int selectedRow = applicationsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select an application to view details.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int applicationId = (Integer) tableModel.getValueAt(selectedRow, 0);
        int jobId = (Integer) tableModel.getValueAt(selectedRow, 1);
        int userId = (Integer) tableModel.getValueAt(selectedRow, 2);
        String status = (String) tableModel.getValueAt(selectedRow, 3);
        String appliedDate = (String) tableModel.getValueAt(selectedRow, 4);

        String details = String.format(
            "Application Details:\n\n" +
            "Application ID: %d\n" +
            "Job ID: %d\n" +
            "User ID: %d\n" +
            "Status: %s\n" +
            "Applied Date: %s\n\n" +
            "Note: Use the job and user management panels to view more details about the job and applicant.",
            applicationId, jobId, userId, status, appliedDate
        );

        JOptionPane.showMessageDialog(this, details, 
            "Application Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Applications Report");
        fileChooser.setSelectedFile(new java.io.File("applications_report.csv"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                java.io.PrintWriter writer = new java.io.PrintWriter(fileToSave);
                
                // Write header
                writer.println("Application ID,Job ID,User ID,Status,Applied Date");
                
                // Write data
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    StringBuilder line = new StringBuilder();
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        if (j > 0) line.append(",");
                        line.append(tableModel.getValueAt(i, j));
                    }
                    writer.println(line.toString());
                }
                
                writer.close();
                JOptionPane.showMessageDialog(this,
                    "Applications report exported successfully!",
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error exporting file: " + e.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}