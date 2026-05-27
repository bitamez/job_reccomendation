package com.mesi.jobai.ui;

import com.mesi.jobai.dao.AdminDAO;
import com.mesi.jobai.model.Admin;
import com.mesi.jobai.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManageEmployersUI extends JFrame {
    private Admin currentAdmin;
    private AdminDAO adminDAO;
    private JTable employersTable;
    private DefaultTableModel tableModel;

    public ManageEmployersUI(Admin admin) {
        this.currentAdmin = admin;
        this.adminDAO = new AdminDAO();
        initializeComponents();
        loadEmployers();
    }

    private void initializeComponents() {
        setTitle("Manage Employers - Admin Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 73, 94));
        JLabel titleLabel = new JLabel("Manage Employers", SwingConstants.CENTER);
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

        String[] columnNames = {"ID", "Name", "Email", "Role", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employersTable = new JTable(tableModel);
        employersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(employersTable);
        scrollPane.setPreferredSize(new Dimension(0, 350));

        panel.add(new JLabel("Employers List:"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton approveButton = new JButton("Approve Employer");
        approveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        approveButton.setBackground(new Color(46, 204, 113));
        approveButton.setForeground(Color.BLACK);  // Black text
        approveButton.setFocusPainted(false);
        approveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        approveButton.addActionListener(new ApproveEmployerActionListener());

        JButton deleteButton = new JButton("Delete Employer");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.BLACK);  // Black text
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(new DeleteEmployerActionListener());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshButton.setForeground(Color.BLACK);  // Black text
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> loadEmployers());

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        closeButton.setForeground(Color.BLACK);  // Black text
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());

        panel.add(approveButton);
        panel.add(deleteButton);
        panel.add(refreshButton);
        panel.add(closeButton);

        return panel;
    }

    private void loadEmployers() {
        tableModel.setRowCount(0);
        List<User> employers = adminDAO.getAllEmployers();
        for (User employer : employers) {
            Object[] row = {
                employer.getId(),
                employer.getName(),
                employer.getEmail(),
                employer.getRole(),
                "Approved" // Since we're getting employers, they're already approved
            };
            tableModel.addRow(row);
        }
    }

    private class ApproveEmployerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = employersTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(ManageEmployersUI.this,
                    "Please select an employer to approve.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int employerId = (Integer) tableModel.getValueAt(selectedRow, 0);
            String employerName = (String) tableModel.getValueAt(selectedRow, 1);

            int option = JOptionPane.showConfirmDialog(ManageEmployersUI.this,
                "Approve employer '" + employerName + "'?",
                "Confirm Approval", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                if (adminDAO.approveEmployer(employerId)) {
                    JOptionPane.showMessageDialog(ManageEmployersUI.this,
                        "Employer approved successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadEmployers();
                } else {
                    JOptionPane.showMessageDialog(ManageEmployersUI.this,
                        "Failed to approve employer.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class DeleteEmployerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = employersTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(ManageEmployersUI.this,
                    "Please select an employer to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int employerId = (Integer) tableModel.getValueAt(selectedRow, 0);
            String employerName = (String) tableModel.getValueAt(selectedRow, 1);

            int option = JOptionPane.showConfirmDialog(ManageEmployersUI.this,
                "Are you sure you want to delete employer '" + employerName + "'?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                if (adminDAO.deleteUser(employerId)) {
                    JOptionPane.showMessageDialog(ManageEmployersUI.this,
                        "Employer deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadEmployers();
                } else {
                    JOptionPane.showMessageDialog(ManageEmployersUI.this,
                        "Failed to delete employer.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}