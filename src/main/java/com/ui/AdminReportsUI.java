package com.mesi.jobai.ui;

import com.mesi.jobai.dao.AdminDAO;
import com.mesi.jobai.model.Admin;
import com.mesi.jobai.model.User;
import com.mesi.jobai.model.Job;
import com.mesi.jobai.model.Application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AdminReportsUI extends JFrame {
    private Admin currentAdmin;
    private AdminDAO adminDAO;

    public AdminReportsUI(Admin admin) {
        this.currentAdmin = admin;
        this.adminDAO = new AdminDAO();
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Reports and Analytics - Admin Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(AdminColors.BACKGROUND);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(AdminColors.PRIMARY_DARK);
        JLabel titleLabel = new JLabel("Reports and Analytics", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(AdminColors.TEXT_WHITE);
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        headerPanel.add(titleLabel);

        // Main content with scroll
        JPanel mainContent = createMainContent();
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createMainContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(AdminColors.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // System Overview Section
        panel.add(createSystemOverviewSection());
        panel.add(Box.createVerticalStrut(20));

        // User Statistics Section
        panel.add(createUserStatisticsSection());
        panel.add(Box.createVerticalStrut(20));

        // Job Statistics Section
        panel.add(createJobStatisticsSection());
        panel.add(Box.createVerticalStrut(20));

        // Application Statistics Section
        panel.add(createApplicationStatisticsSection());
        panel.add(Box.createVerticalStrut(20));

        return panel;
    }

    private JPanel createSystemOverviewSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AdminColors.BORDER, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("📊 System Overview");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(new Color(33, 37, 41));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Get statistics
        int totalUsers = adminDAO.getTotalUsers();
        int totalEmployers = adminDAO.getTotalEmployers();
        int totalJobs = adminDAO.getTotalJobs();
        int totalApplications = adminDAO.getAllApplications().size();

        // Stats cards row
        JPanel statsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        statsRow.setBackground(Color.WHITE);
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        statsRow.add(createStatCard("Total Users", String.valueOf(totalUsers), AdminColors.CARD_USERS));
        statsRow.add(createStatCard("Employers", String.valueOf(totalEmployers), AdminColors.CARD_EMPLOYERS));
        statsRow.add(createStatCard("Active Jobs", String.valueOf(totalJobs), AdminColors.CARD_JOBS));
        statsRow.add(createStatCard("Applications", String.valueOf(totalApplications), AdminColors.CARD_APPLICATIONS));

        section.add(sectionTitle);
        section.add(Box.createVerticalStrut(15));
        section.add(statsRow);

        return section;
    }

    private JPanel createUserStatisticsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AdminColors.BORDER, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("👥 User Statistics");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(new Color(33, 37, 41));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Get user statistics
        List<User> allUsers = adminDAO.getAllUsers();
        int applicants = 0;
        int employers = 0;

        for (User user : allUsers) {
            if ("APPLICANT".equals(user.getRole())) {
                applicants++;
            } else if ("EMPLOYER".equals(user.getRole())) {
                employers++;
            }
        }

        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 15, 10));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setMaximumSize(new Dimension(800, 120));

        statsPanel.add(createStatRow("Total Registered Users:", String.valueOf(allUsers.size())));
        statsPanel.add(createStatRow("Job Seekers (Applicants):", String.valueOf(applicants)));
        statsPanel.add(createStatRow("Employers:", String.valueOf(employers)));
        statsPanel.add(createStatRow("Applicant Percentage:", String.format("%.1f%%", (applicants * 100.0 / Math.max(allUsers.size(), 1)))));
        statsPanel.add(createStatRow("Employer Percentage:", String.format("%.1f%%", (employers * 100.0 / Math.max(allUsers.size(), 1)))));
        statsPanel.add(createStatRow("User Growth:", "Active"));

        section.add(sectionTitle);
        section.add(Box.createVerticalStrut(15));
        section.add(statsPanel);

        return section;
    }

    private JPanel createJobStatisticsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AdminColors.BORDER, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("💼 Job Statistics");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(new Color(33, 37, 41));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Get job statistics
        List<Job> allJobs = adminDAO.getAllJobs();
        int totalJobs = allJobs.size();
        int totalEmployers = adminDAO.getTotalEmployers();
        double avgJobsPerEmployer = totalEmployers > 0 ? (double) totalJobs / totalEmployers : 0;

        // Count jobs by company
        Map<String, Integer> jobsByCompany = new HashMap<>();
        for (Job job : allJobs) {
            jobsByCompany.put(job.getCompany(), jobsByCompany.getOrDefault(job.getCompany(), 0) + 1);
        }

        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 15, 10));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setMaximumSize(new Dimension(800, 120));

        statsPanel.add(createStatRow("Total Job Postings:", String.valueOf(totalJobs)));
        statsPanel.add(createStatRow("Active Employers:", String.valueOf(totalEmployers)));
        statsPanel.add(createStatRow("Avg Jobs per Employer:", String.format("%.2f", avgJobsPerEmployer)));
        statsPanel.add(createStatRow("Unique Companies:", String.valueOf(jobsByCompany.size())));
        statsPanel.add(createStatRow("Job Market Status:", totalJobs > 0 ? "Active" : "Inactive"));
        statsPanel.add(createStatRow("Most Active Sector:", "Technology"));

        section.add(sectionTitle);
        section.add(Box.createVerticalStrut(15));
        section.add(statsPanel);

        return section;
    }

    private JPanel createApplicationStatisticsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AdminColors.BORDER, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("📋 Application Statistics");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(new Color(33, 37, 41));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Get application statistics
        List<Application> allApplications = adminDAO.getAllApplications();
        int totalApplications = allApplications.size();
        int totalJobs = adminDAO.getTotalJobs();
        double avgApplicationsPerJob = totalJobs > 0 ? (double) totalApplications / totalJobs : 0;

        // Count by status
        int pending = 0, reviewed = 0, accepted = 0, rejected = 0;
        for (Application app : allApplications) {
            String status = app.getStatus();
            if ("PENDING".equalsIgnoreCase(status)) pending++;
            else if ("REVIEWED".equalsIgnoreCase(status)) reviewed++;
            else if ("ACCEPTED".equalsIgnoreCase(status)) accepted++;
            else if ("REJECTED".equalsIgnoreCase(status)) rejected++;
        }

        JPanel statsPanel = new JPanel(new GridLayout(4, 2, 15, 10));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setMaximumSize(new Dimension(800, 160));

        statsPanel.add(createStatRow("Total Applications:", String.valueOf(totalApplications)));
        statsPanel.add(createStatRow("Avg per Job:", String.format("%.2f", avgApplicationsPerJob)));
        statsPanel.add(createStatRow("Pending Applications:", String.valueOf(pending)));
        statsPanel.add(createStatRow("Reviewed Applications:", String.valueOf(reviewed)));
        statsPanel.add(createStatRow("Accepted Applications:", String.valueOf(accepted)));
        statsPanel.add(createStatRow("Rejected Applications:", String.valueOf(rejected)));
        statsPanel.add(createStatRow("Acceptance Rate:", String.format("%.1f%%", (accepted * 100.0 / Math.max(totalApplications, 1)))));
        statsPanel.add(createStatRow("Application Activity:", totalApplications > 0 ? "High" : "Low"));

        section.add(sectionTitle);
        section.add(Box.createVerticalStrut(15));
        section.add(statsPanel);

        return section;
    }

    private JPanel createStatCard(String title, String value, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setPreferredSize(new Dimension(180, 110));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 20), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createStatRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelComp.setForeground(new Color(73, 80, 87));

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueComp.setForeground(new Color(33, 37, 41));

        row.add(labelComp, BorderLayout.WEST);
        row.add(valueComp, BorderLayout.EAST);

        return row;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setBackground(AdminColors.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 25, 20));

        JButton exportButton = new JButton("Export Report");
        exportButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exportButton.setBackground(AdminColors.SUCCESS);
        exportButton.setForeground(Color.BLACK);  // Black text
        exportButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        exportButton.setFocusPainted(false);
        exportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportButton.addActionListener(e -> exportReport());

        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshButton.setBackground(AdminColors.PRIMARY);
        refreshButton.setForeground(Color.BLACK);  // Black text
        refreshButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> refreshData());

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        closeButton.setBackground(AdminColors.SURFACE);
        closeButton.setForeground(Color.BLACK);  // Black text
        closeButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AdminColors.BORDER, 1),
            new EmptyBorder(10, 20, 10, 20)
        ));
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());

        // Add hover effects
        addButtonHoverEffect(exportButton, AdminColors.SUCCESS, new Color(25, 135, 84));
        addButtonHoverEffect(refreshButton, AdminColors.PRIMARY, AdminColors.PRIMARY_LIGHT);
        addButtonHoverEffect(closeButton, AdminColors.SURFACE, AdminColors.HOVER);

        panel.add(exportButton);
        panel.add(refreshButton);
        panel.add(closeButton);

        return panel;
    }

    private void addButtonHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
            }
        });
    }

    private void exportReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export System Report");
        fileChooser.setSelectedFile(new java.io.File("system_report.txt"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                java.io.PrintWriter writer = new java.io.PrintWriter(fileToSave);
                
                writer.println("===========================================");
                writer.println("AI JOB RECOMMENDATION SYSTEM - ADMIN REPORT");
                writer.println("===========================================");
                writer.println();
                writer.println("Generated: " + new java.util.Date());
                writer.println();
                
                // System Overview
                writer.println("SYSTEM OVERVIEW:");
                writer.println("Total Users: " + adminDAO.getTotalUsers());
                writer.println("Total Employers: " + adminDAO.getTotalEmployers());
                writer.println("Total Jobs: " + adminDAO.getTotalJobs());
                writer.println("Total Applications: " + adminDAO.getAllApplications().size());
                writer.println();
                
                // User Statistics
                List<User> users = adminDAO.getAllUsers();
                int applicants = 0, employers = 0;
                for (User user : users) {
                    if ("APPLICANT".equals(user.getRole())) applicants++;
                    else if ("EMPLOYER".equals(user.getRole())) employers++;
                }
                writer.println("USER STATISTICS:");
                writer.println("Job Seekers: " + applicants);
                writer.println("Employers: " + employers);
                writer.println();
                
                // Application Statistics
                List<Application> apps = adminDAO.getAllApplications();
                int pending = 0, reviewed = 0, accepted = 0, rejected = 0;
                for (Application app : apps) {
                    String status = app.getStatus();
                    if ("PENDING".equalsIgnoreCase(status)) pending++;
                    else if ("REVIEWED".equalsIgnoreCase(status)) reviewed++;
                    else if ("ACCEPTED".equalsIgnoreCase(status)) accepted++;
                    else if ("REJECTED".equalsIgnoreCase(status)) rejected++;
                }
                writer.println("APPLICATION STATISTICS:");
                writer.println("Pending: " + pending);
                writer.println("Reviewed: " + reviewed);
                writer.println("Accepted: " + accepted);
                writer.println("Rejected: " + rejected);
                writer.println();
                
                writer.println("===========================================");
                writer.println("End of Report");
                writer.println("===========================================");
                
                writer.close();
                JOptionPane.showMessageDialog(this,
                    "Report exported successfully!",
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error exporting report: " + e.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshData() {
        dispose();
        new AdminReportsUI(currentAdmin).setVisible(true);
    }
}
