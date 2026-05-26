package com.mesi.jobai.ui;

import com.mesi.jobai.dao.AdminDAO;
import com.mesi.jobai.model.Admin;
import com.mesi.jobai.model.Job;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManageJobsUI extends JFrame {
    private Admin currentAdmin;
    private AdminDAO adminDAO;
    private JTable jobsTable;
    private DefaultTableModel tableModel;
    private JTextField titleField, companyField, employerIdField;
    private JTextArea descriptionArea, requirementsArea;

    public ManageJobsUI(Admin admin) {
        this.currentAdmin = admin;
        this.adminDAO = new AdminDAO();
        initializeComponents();
        loadJobs();
    }

    private void initializeComponents() {
        setTitle("Manage Jobs - Admin Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(AdminColors.BACKGROUND);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(AdminColors.PRIMARY_DARK);
        JLabel titleLabel = new JLabel("Manage Jobs", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(AdminColors.TEXT_WHITE);
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        headerPanel.add(titleLabel);

        // Table Panel
        JPanel tablePanel = createTablePanel();

        // Form Panel
        JPanel formPanel = createFormPanel();

        // Button Panel
        JPanel buttonPanel = createButtonPanel();

        // Main Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(AdminColors.BACKGROUND);
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        contentPanel.add(formPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        String[] columnNames = {"ID", "Employer ID", "Title", "Company", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jobsTable = new JTable(tableModel);
        jobsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jobsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelection();
            }
        });

        JScrollPane scrollPane = new JScrollPane(jobsTable);
        scrollPane.setPreferredSize(new Dimension(0, 250));

        panel.add(new JLabel("Jobs List:"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Job Details"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Employer ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Employer ID:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        employerIdField = new JTextField(20);
        panel.add(employerIdField, gbc);

        // Title
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        titleField = new JTextField(20);
        panel.add(titleField, gbc);

        // Company
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel("Company:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        companyField = new JTextField(20);
        panel.add(companyField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 0.5;
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        panel.add(descScrollPane, gbc);

        // Requirements
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Requirements:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 0.5;
        requirementsArea = new JTextArea(3, 20);
        requirementsArea.setLineWrap(true);
        requirementsArea.setWrapStyleWord(true);
        JScrollPane reqScrollPane = new JScrollPane(requirementsArea);
        panel.add(reqScrollPane, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton addButton = new JButton("Add Job");
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.BLACK);  // Black text
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(new AddJobActionListener());

        JButton updateButton = new JButton("Update Job");
        updateButton.setBackground(new Color(52, 152, 219));
        updateButton.setForeground(Color.BLACK);  // Black text
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateButton.setFocusPainted(false);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.addActionListener(new UpdateJobActionListener());

        JButton deleteButton = new JButton("Delete Job");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.BLACK);  // Black text
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(new DeleteJobActionListener());

        JButton clearButton = new JButton("Clear Form");
        clearButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        clearButton.setForeground(Color.BLACK);  // Black text
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.addActionListener(e -> clearForm());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshButton.setForeground(Color.BLACK);  // Black text
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> loadJobs());

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        closeButton.setForeground(Color.BLACK);  // Black text
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        panel.add(refreshButton);
        panel.add(closeButton);

        return panel;
    }

    private void loadJobs() {
        tableModel.setRowCount(0);
        List<Job> jobs = adminDAO.getAllJobs();
        for (Job job : jobs) {
            Object[] row = {
                job.getId(),
                job.getEmployerId(),
                job.getTitle(),
                job.getCompany(),
                job.getDescription().length() > 50 ? 
                    job.getDescription().substring(0, 50) + "..." : job.getDescription()
            };
            tableModel.addRow(row);
        }
    }

    private void populateFormFromSelection() {
        int selectedRow = jobsTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Get full job details
            int jobId = (Integer) tableModel.getValueAt(selectedRow, 0);
            List<Job> jobs = adminDAO.getAllJobs();
            Job selectedJob = jobs.stream()
                .filter(job -> job.getId() == jobId)
                .findFirst()
                .orElse(null);

            if (selectedJob != null) {
                employerIdField.setText(String.valueOf(selectedJob.getEmployerId()));
                titleField.setText(selectedJob.getTitle());
                companyField.setText(selectedJob.getCompany());
                descriptionArea.setText(selectedJob.getDescription());
                requirementsArea.setText(selectedJob.getRequirements());
            }
        }
    }

    private void clearForm() {
        employerIdField.setText("");
        titleField.setText("");
        companyField.setText("");
        descriptionArea.setText("");
        requirementsArea.setText("");
    }

    private boolean validateForm() {
        return !employerIdField.getText().trim().isEmpty() &&
               !titleField.getText().trim().isEmpty() &&
               !companyField.getText().trim().isEmpty() &&
               !descriptionArea.getText().trim().isEmpty() &&
               !requirementsArea.getText().trim().isEmpty();
    }

    private class AddJobActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!validateForm()) {
                JOptionPane.showMessageDialog(ManageJobsUI.this,
                    "Please fill in all fields.",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int employerId = Integer.parseInt(employerIdField.getText().trim());
                Job job = new Job(0, employerId, titleField.getText().trim(),
                    companyField.getText().trim(), descriptionArea.getText().trim(),
                    requirementsArea.getText().trim());

                if (adminDAO.addJob(job)) {
                    JOptionPane.showMessageDialog(ManageJobsUI.this,
                        "Job added successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadJobs();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(ManageJobsUI.this,
                        "Failed to add job.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ManageJobsUI.this,
                    "Please enter a valid Employer ID.",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private class UpdateJobActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = jobsTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(ManageJobsUI.this,
                    "Please select a job to update.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!validateForm()) {
                JOptionPane.showMessageDialog(ManageJobsUI.this,
                    "Please fill in all fields.",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int jobId = (Integer) tableModel.getValueAt(selectedRow, 0);
                int employerId = Integer.parseInt(employerIdField.getText().trim());
                Job job = new Job(jobId, employerId, titleField.getText().trim(),
                    companyField.getText().trim(), descriptionArea.getText().trim(),
                    requirementsArea.getText().trim());

                if (adminDAO.updateJob(job)) {
                    JOptionPane.showMessageDialog(ManageJobsUI.this,
                        "Job updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadJobs();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(ManageJobsUI.this,
                        "Failed to update job.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ManageJobsUI.this,
                    "Please enter a valid Employer ID.",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private class DeleteJobActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = jobsTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(ManageJobsUI.this,
                    "Please select a job to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int jobId = (Integer) tableModel.getValueAt(selectedRow, 0);
            String jobTitle = (String) tableModel.getValueAt(selectedRow, 2);

            int option = JOptionPane.showConfirmDialog(ManageJobsUI.this,
                "Are you sure you want to delete job '" + jobTitle + "'?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                if (adminDAO.deleteJob(jobId)) {
                    JOptionPane.showMessageDialog(ManageJobsUI.this,
                        "Job deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadJobs();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(ManageJobsUI.this,
                        "Failed to delete job.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}