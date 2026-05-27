package com.mesi.jobai.ui;

import com.mesi.jobai.controller.ApplicationController;
import com.mesi.jobai.model.Job;
import com.mesi.jobai.model.User;
import javax.swing.*;
import java.awt.*;

public class JobDetailsUI {
    private JPanel view;
    private ApplicationController applicationController;

    public JobDetailsUI(DashboardUI dashboard, Job job, String matchScore, User currentUser) {
        this.applicationController = new ApplicationController();
        view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        view.setBackground(SystemColors.BACKGROUND);
        view.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel sectionTitle = new JLabel("Job Details");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        sectionTitle.setForeground(new Color(33, 37, 41));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(SystemColors.SURFACE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(35, 35, 35, 35)
        ));
        cardPanel.setMaximumSize(new Dimension(800, Integer.MAX_VALUE));
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel jobTitle = new JLabel(job.getTitle() + " @ " + job.getCompany());
        jobTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        jobTitle.setForeground(new Color(33, 37, 41));
        jobTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel scoreLabel = new JLabel("AI Match Score: " + matchScore);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        scoreLabel.setForeground(new Color(0, 120, 215));
        scoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descTitle = new JLabel("Job Description");
        descTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        descTitle.setForeground(new Color(73, 80, 87));
        descTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea description = new JTextArea(job.getDescription());
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setOpaque(false);
        description.setForeground(new Color(33, 37, 41));
        description.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        description.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JLabel reqTitle = new JLabel("Requirements");
        reqTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        reqTitle.setForeground(new Color(73, 80, 87));
        reqTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea reqDesc = new JTextArea(job.getRequirements());
        reqDesc.setWrapStyleWord(true);
        reqDesc.setLineWrap(true);
        reqDesc.setEditable(false);
        reqDesc.setOpaque(false);
        reqDesc.setForeground(new Color(33, 37, 41));
        reqDesc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        reqDesc.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setBackground(SystemColors.SURFACE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel statusLabel = new JLabel();
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JButton btnApply = new JButton("Apply Now");
        btnApply.setPreferredSize(new Dimension(160, 50));
        btnApply.setBackground(new Color(0, 120, 215));  // Explicit blue color
        btnApply.setForeground(Color.BLACK);
        btnApply.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnApply.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 120, 215), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        btnApply.setFocusPainted(false);
        btnApply.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnApply.setOpaque(true);
        
        // Add hover effect
        btnApply.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btnApply.isEnabled()) {
                    btnApply.setBackground(new Color(0, 100, 195));  // Darker blue on hover
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btnApply.isEnabled()) {
                    btnApply.setBackground(new Color(0, 120, 215));  // Original blue
                }
            }
        });
        
        btnApply.addActionListener(e -> {
            if (applicationController.applyForJob(job.getId(), currentUser.getId())) {
                // Show success alert dialog
                JOptionPane.showMessageDialog(
                    view,
                    "Your application has been submitted successfully!\n\n" +
                    "Job: " + job.getTitle() + "\n" +
                    "Company: " + job.getCompany() + "\n\n" +
                    "The employer will review your application soon.",
                    "Application Successful",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                statusLabel.setText("Successfully Applied!");
                statusLabel.setForeground(new Color(16, 124, 16));
                btnApply.setEnabled(false);
                btnApply.setBackground(new Color(180, 180, 180));  // Gray when disabled
            } else {
                // Show error alert dialog
                JOptionPane.showMessageDialog(
                    view,
                    "Unable to submit your application.\n\n" +
                    "You may have already applied for this position.\n" +
                    "Please check your applications page.",
                    "Application Failed",
                    JOptionPane.ERROR_MESSAGE
                );
                
                statusLabel.setText("Failed to apply. You may have already applied.");
                statusLabel.setForeground(new Color(196, 43, 28));
            }
        });

        JButton btnBack = new JButton("← Back to Jobs");
        btnBack.setPreferredSize(new Dimension(160, 50));
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(Color.BLACK);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 120, 215), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setOpaque(true);
        
        // Add hover effect
        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBack.setBackground(new Color(240, 248, 255));  // Light blue on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBack.setBackground(Color.WHITE);  // White background
            }
        });
        
        btnBack.addActionListener(e -> dashboard.showJobList());

        buttonPanel.add(btnBack);
        buttonPanel.add(btnApply);
        buttonPanel.add(statusLabel);

        cardPanel.add(jobTitle);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(scoreLabel);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(descTitle);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(description);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(reqTitle);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(reqDesc);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(buttonPanel);

        view.add(sectionTitle);
        view.add(Box.createVerticalStrut(25));
        view.add(cardPanel);
        view.add(Box.createVerticalGlue());
    }

    public JPanel getView() {
        return view;
    }
}
