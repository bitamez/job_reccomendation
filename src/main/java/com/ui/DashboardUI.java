package com.mesi.jobai.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mesi.jobai.model.User;

public class DashboardUI {
    private JPanel mainLayout;
    private JPanel sidebar;
    private JPanel header;

    private JobListUI jobList;
    private ApplicationsUI applications;
    private PostJobUI postJobUI;
    private UserProfileUI userProfileUI;
    private SkillsUI skillsUI;
    private EmployerApplicationsUI employerApplicationsUI;
    private EmployerAnalyticsUI employerAnalyticsUI;
    private User currentUser;
    private JFrame parentFrame;

    public DashboardUI(User user, JFrame parentFrame) {
        this.currentUser = user;
        this.parentFrame = parentFrame;
        mainLayout = new JPanel(new BorderLayout());
        mainLayout.setBackground(SystemColors.BACKGROUND);

        createHeader();
        createSidebar();

        this.jobList = new JobListUI(this);
        this.applications = new ApplicationsUI(currentUser);
        this.postJobUI = new PostJobUI(currentUser);
        this.userProfileUI = new UserProfileUI(currentUser);
        this.skillsUI = new SkillsUI(currentUser);
        this.employerApplicationsUI = new EmployerApplicationsUI(currentUser);
        this.employerAnalyticsUI = new EmployerAnalyticsUI(currentUser);

        mainLayout.add(header, BorderLayout.NORTH);
        mainLayout.add(sidebar, BorderLayout.WEST);
        
        // Initial center view logic based on role
        if (currentUser.getRole().equals("EMPLOYER")) {
            mainLayout.add(employerAnalyticsUI.getView(), BorderLayout.CENTER);
        } else {
            mainLayout.add(jobList.getView(), BorderLayout.CENTER);
        }
    }

    private void createHeader() {
        header = new JPanel(new BorderLayout());
        header.setBackground(SystemColors.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        header.setPreferredSize(new Dimension(0, 70));

        JLabel titleLabel = new JLabel("AI Job Recommendation System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        JLabel userIcon = new JLabel("👤 " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        userIcon.setForeground(Color.WHITE);
        userIcon.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(0, 0, 0, 0));
        logoutBtn.setForeground(Color.BLACK);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            parentFrame.dispose();
            SwingUtilities.invokeLater(() -> {
                new UnifiedLoginUI().setVisible(true);
            });
        });

        rightPanel.add(userIcon);
        rightPanel.add(logoutBtn);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
    }

    private void createSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SystemColors.SURFACE);
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebar.setPreferredSize(new Dimension(200, 0));

        JButton btnDashboard = createSidebarButton("Dashboard", true);
        JButton btnApplications = createSidebarButton(currentUser.getRole().equals("EMPLOYER") ? "Post a Job" : "My Applications", false);
        JButton btnSkills = createSidebarButton("My Skills", false);
        JButton btnProfile = createSidebarButton("My Profile", false);
        JButton btnViewApplicants = createSidebarButton("Review Applicants", false);

        btnDashboard.addActionListener(e -> {
             mainLayout.removeAll();
             mainLayout.add(header, BorderLayout.NORTH);
             mainLayout.add(sidebar, BorderLayout.WEST);
             if (currentUser.getRole().equals("EMPLOYER")) {
                 mainLayout.add(employerAnalyticsUI.getView(), BorderLayout.CENTER);
             } else {
                 mainLayout.add(jobList.getView(), BorderLayout.CENTER);
             }
             mainLayout.revalidate();
             mainLayout.repaint();
             setActive(btnDashboard, btnApplications, btnSkills, btnProfile, btnViewApplicants);
        });

        btnApplications.addActionListener(e -> {
             mainLayout.removeAll();
             mainLayout.add(header, BorderLayout.NORTH);
             mainLayout.add(sidebar, BorderLayout.WEST);
             if (currentUser.getRole().equals("EMPLOYER")) {
                 mainLayout.add(postJobUI.getView(), BorderLayout.CENTER);
             } else {
                 mainLayout.add(applications.getView(), BorderLayout.CENTER);
             }
             mainLayout.revalidate();
             mainLayout.repaint();
             setActive(btnApplications, btnDashboard, btnSkills, btnProfile, btnViewApplicants);
        });

        btnSkills.addActionListener(e -> {
             mainLayout.removeAll();
             mainLayout.add(header, BorderLayout.NORTH);
             mainLayout.add(sidebar, BorderLayout.WEST);
             mainLayout.add(skillsUI.getView(), BorderLayout.CENTER);
             mainLayout.revalidate();
             mainLayout.repaint();
             setActive(btnSkills, btnDashboard, btnApplications, btnProfile, btnViewApplicants);
        });

        btnProfile.addActionListener(e -> {
             mainLayout.removeAll();
             mainLayout.add(header, BorderLayout.NORTH);
             mainLayout.add(sidebar, BorderLayout.WEST);
             mainLayout.add(userProfileUI.getView(), BorderLayout.CENTER);
             mainLayout.revalidate();
             mainLayout.repaint();
             setActive(btnProfile, btnDashboard, btnApplications, btnSkills, btnViewApplicants);
        });

        btnViewApplicants.addActionListener(e -> {
             mainLayout.removeAll();
             mainLayout.add(header, BorderLayout.NORTH);
             mainLayout.add(sidebar, BorderLayout.WEST);
             mainLayout.add(employerApplicationsUI.getView(), BorderLayout.CENTER);
             mainLayout.revalidate();
             mainLayout.repaint();
             setActive(btnViewApplicants, btnDashboard, btnApplications, btnProfile);
        });

        if (currentUser.getRole().equals("EMPLOYER")) {
            sidebar.add(btnDashboard);
            sidebar.add(Box.createVerticalStrut(5));
            sidebar.add(btnApplications);
            sidebar.add(Box.createVerticalStrut(5));
            sidebar.add(btnViewApplicants);
            sidebar.add(Box.createVerticalStrut(5));
            sidebar.add(btnProfile);
        } else {
            sidebar.add(btnDashboard);
            sidebar.add(Box.createVerticalStrut(5));
            sidebar.add(btnApplications);
            sidebar.add(Box.createVerticalStrut(5));
            sidebar.add(btnSkills);
            sidebar.add(Box.createVerticalStrut(5));
            sidebar.add(btnProfile);
        }
        sidebar.add(Box.createVerticalGlue());
    }

    private JButton createSidebarButton(String text, boolean active) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setPreferredSize(new Dimension(180, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (active) {
            button.setBackground(SystemColors.PRIMARY);
            button.setForeground(Color.BLACK);
            button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        } else {
            button.setBackground(SystemColors.SURFACE);
            button.setForeground(new Color(33, 37, 41));
        }
        
        return button;
    }

    private void setActive(JButton activeBtn, JButton... others) {
        activeBtn.setBackground(SystemColors.PRIMARY);
        activeBtn.setForeground(Color.BLACK);
        activeBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        for (JButton btn : others) {
            btn.setBackground(SystemColors.SURFACE);
            btn.setForeground(new Color(33, 37, 41));
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        }
    }

    public void showJobDetails(com.mesi.jobai.model.Job job, String matchScore) {
        JobDetailsUI details = new JobDetailsUI(this, job, matchScore, currentUser);
        mainLayout.removeAll();
        mainLayout.add(header, BorderLayout.NORTH);
        mainLayout.add(sidebar, BorderLayout.WEST);
        mainLayout.add(details.getView(), BorderLayout.CENTER);
        mainLayout.revalidate();
        mainLayout.repaint();
    }

    public void showJobList() {
        mainLayout.removeAll();
        mainLayout.add(header, BorderLayout.NORTH);
        mainLayout.add(sidebar, BorderLayout.WEST);
        mainLayout.add(jobList.getView(), BorderLayout.CENTER);
        mainLayout.revalidate();
        mainLayout.repaint();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public JPanel getView() {
        return mainLayout;
    }
}
