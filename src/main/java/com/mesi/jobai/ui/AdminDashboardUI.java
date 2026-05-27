package com.mesi.jobai.ui;

import com.mesi.jobai.dao.AdminDAO;
import com.mesi.jobai.model.Admin;
import com.mesi.jobai.model.User;
import com.mesi.jobai.model.Job;
import com.mesi.jobai.model.Application;
import com.mesi.jobai.model.Skill;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AdminDashboardUI extends JFrame {
    private Admin currentAdmin;
    private AdminDAO adminDAO;
    private JPanel mainContent;
    private JPanel sidebar;
    private JButton activeButton;

    public AdminDashboardUI(Admin admin) {
        this.currentAdmin = admin;
        this.adminDAO = new AdminDAO();
        initializeComponents();
        showDashboardOverview();
    }

    private void initializeComponents() {
        setTitle("Admin Dashboard - AI Job Recommendation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Apply modern colors
        getContentPane().setBackground(AdminColors.CONTENT_BG);

        // Create header
        createHeader();
        
        // Create sidebar
        createSidebar();
        
        // Create main content area
        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(AdminColors.CONTENT_BG);
        
        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
    }

    private void createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AdminColors.HEADER_BG);
        header.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel titleLabel = new JLabel("Admin Dashboard - AI Job Recommendation System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(AdminColors.HEADER_TEXT);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setBackground(AdminColors.HEADER_BG);

        JLabel adminIcon = new JLabel("👤 " + currentAdmin.getFullName() + " (Administrator)");
        adminIcon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        adminIcon.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Logout");
        styleLogoutButton(logoutBtn);
        logoutBtn.addActionListener(e -> logout());

        rightPanel.add(adminIcon);
        rightPanel.add(logoutBtn);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    private void styleLogoutButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(231, 76, 60)); // Professional red
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(192, 57, 43)); // Darker red on hover
                button.setForeground(Color.BLACK);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(231, 76, 60));
                button.setForeground(Color.BLACK);
            }
        });
    }

    private void createSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AdminColors.SIDEBAR_BG);
        sidebar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)),  // Right border
            new EmptyBorder(24, 20, 24, 20)
        ));
        sidebar.setPreferredSize(new Dimension(260, 0));

        JButton btnDashboard = createSidebarButton("📊 Dashboard", true);
        JButton btnUsers = createSidebarButton("👥 Manage Users", false);
        JButton btnEmployers = createSidebarButton("🏢 Manage Employers", false);
        JButton btnJobs = createSidebarButton("💼 Manage Jobs", false);
        JButton btnApplications = createSidebarButton("📋 View Applications", false);
        JButton btnSkills = createSidebarButton("🛠️ Manage Skills", false);
        JButton btnReports = createSidebarButton("📈 Reports", false);

        btnDashboard.addActionListener(e -> {
            showDashboardOverview();
            setActiveButton(btnDashboard);
        });

        btnUsers.addActionListener(e -> {
            showUserManagement();
            setActiveButton(btnUsers);
        });

        btnEmployers.addActionListener(e -> {
            showEmployerManagement();
            setActiveButton(btnEmployers);
        });

        btnJobs.addActionListener(e -> {
            showJobManagement();
            setActiveButton(btnJobs);
        });

        btnApplications.addActionListener(e -> {
            showApplicationManagement();
            setActiveButton(btnApplications);
        });

        btnSkills.addActionListener(e -> {
            showSkillManagement();
            setActiveButton(btnSkills);
        });

        btnReports.addActionListener(e -> {
            showReports();
            setActiveButton(btnReports);
        });

        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnUsers);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnEmployers);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnJobs);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnApplications);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnSkills);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnReports);
        sidebar.add(Box.createVerticalGlue());

        activeButton = btnDashboard;
    }

    private JButton createSidebarButton(String text, boolean active) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setPreferredSize(new Dimension(220, 45));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 16, 12, 16));
        button.setOpaque(true);
        
        if (active) {
            button.setBackground(AdminColors.SIDEBAR_ACTIVE);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        } else {
            button.setBackground(AdminColors.SIDEBAR_BG);
            button.setForeground(AdminColors.SIDEBAR_TEXT);  // Black text
        }
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(AdminColors.SIDEBAR_HOVER);
                    button.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(AdminColors.SIDEBAR_BG);
                    button.setForeground(AdminColors.SIDEBAR_TEXT);  // Black text
                }
            }
        });
        
        return button;
    }

    private void setActiveButton(JButton button) {
        // Reset previous active button
        if (activeButton != null) {
            activeButton.setBackground(AdminColors.SIDEBAR_BG);  // White background
            activeButton.setForeground(AdminColors.SIDEBAR_TEXT);  // Black text
            activeButton.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        }
        
        // Set new active button
        activeButton = button;
        button.setBackground(AdminColors.SIDEBAR_ACTIVE);  // Professional blue
        button.setForeground(Color.WHITE);  // White text on blue
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
    }

    private void showDashboardOverview() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(AdminColors.CONTENT_BG);
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Admin Dashboard Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(44, 62, 80));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Load real stats from database
        int totalUsers = adminDAO.getTotalUsers();
        int totalEmployers = adminDAO.getTotalEmployers();
        int totalJobs = adminDAO.getTotalJobs();
        int totalApplications = adminDAO.getAllApplications().size();

        // Professional stats cards
        JPanel statsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        statsRow.setBackground(AdminColors.CONTENT_BG);
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        statsRow.add(createModernStatCard("👥 Total Users", String.valueOf(totalUsers), AdminColors.CARD_USERS));
        statsRow.add(createModernStatCard("🏢 Employers", String.valueOf(totalEmployers), AdminColors.CARD_EMPLOYERS));
        statsRow.add(createModernStatCard("💼 Jobs Posted", String.valueOf(totalJobs), AdminColors.CARD_JOBS));
        statsRow.add(createModernStatCard("📋 Applications", String.valueOf(totalApplications), AdminColors.CARD_APPLICATIONS));

        JLabel welcomeMsg = new JLabel("Welcome to the Admin Dashboard, System Administrator!");
        welcomeMsg.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        welcomeMsg.setForeground(new Color(127, 140, 141));
        welcomeMsg.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Activity section
        JLabel activityTitle = new JLabel("Recent Activity");
        activityTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        activityTitle.setForeground(new Color(44, 62, 80));
        activityTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        activityTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel activityBox = new JPanel();
        activityBox.setLayout(new BoxLayout(activityBox, BoxLayout.Y_AXIS));
        activityBox.setBackground(Color.WHITE);
        activityBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AdminColors.BORDER, 1),
            new EmptyBorder(24, 24, 24, 24)
        ));
        activityBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        activityBox.add(createActivityItem("• " + totalUsers + " users registered in the system"));
        activityBox.add(Box.createVerticalStrut(12));
        activityBox.add(createActivityItem("• " + totalEmployers + " employers are actively posting jobs"));
        activityBox.add(Box.createVerticalStrut(12));
        activityBox.add(createActivityItem("• " + totalJobs + " job opportunities available"));
        activityBox.add(Box.createVerticalStrut(12));
        activityBox.add(createActivityItem("• " + totalApplications + " applications submitted"));

        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(welcomeMsg);
        content.add(Box.createVerticalStrut(30));
        content.add(statsRow);
        content.add(Box.createVerticalStrut(25));
        content.add(activityTitle);
        content.add(activityBox);

        mainContent.removeAll();
        mainContent.add(content, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private JPanel createModernStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setPreferredSize(new Dimension(210, 130));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 20), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JLabel createActivityItem(String text) {
        JLabel item = new JLabel(text);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        item.setForeground(new Color(71, 85, 105));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        return item;
    }

    private void showUserManagement() {
        ManageUsersUI usersUI = new ManageUsersUI(currentAdmin);
        usersUI.setVisible(true);
    }

    // Management section methods - Open dedicated UI windows
    private void showEmployerManagement() {
        ManageEmployersUI employersUI = new ManageEmployersUI(currentAdmin);
        employersUI.setVisible(true);
    }

    private void showJobManagement() {
        ManageJobsUI jobsUI = new ManageJobsUI(currentAdmin);
        jobsUI.setVisible(true);
    }

    private void showApplicationManagement() {
        ViewApplicationsUI applicationsUI = new ViewApplicationsUI(currentAdmin);
        applicationsUI.setVisible(true);
    }

    private void showSkillManagement() {
        ManageSkillsUI skillsUI = new ManageSkillsUI(currentAdmin);
        skillsUI.setVisible(true);
    }

    private void showReports() {
        AdminReportsUI reportsUI = new AdminReportsUI(currentAdmin);
        reportsUI.setVisible(true);
    }

    private void showPlaceholder(String title, String description) {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(SystemColors.BG_COLOR);
        content.setBorder(new EmptyBorder(50, 50, 50, 50));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(SystemColors.BG_COLOR);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(SystemColors.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descLabel.setForeground(SystemColors.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel comingSoonLabel = new JLabel("Coming Soon...");
        comingSoonLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        comingSoonLabel.setForeground(SystemColors.TEXT_MUTED);
        comingSoonLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(descLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(comingSoonLabel);

        content.add(centerPanel, BorderLayout.CENTER);

        mainContent.removeAll();
        mainContent.add(content, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void logout() {
        int option = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout", JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            dispose();
            new UnifiedLoginUI().setVisible(true);
        }
    }
}