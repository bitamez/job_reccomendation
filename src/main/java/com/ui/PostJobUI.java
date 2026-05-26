package com.mesi.jobai.ui;

import com.mesi.jobai.controller.JobController;
import com.mesi.jobai.model.Job;
import com.mesi.jobai.model.User;
import javax.swing.*;
import java.awt.*;

public class PostJobUI {
    private JPanel view;
    private User currentUser;
    private JobController jobController;

    private static final String[] KEYWORDS = {
        "Java", "Python", "React", "Angular", "Vue", "AWS", "Docker", "SQL", "Management"
    };

    public PostJobUI(User currentUser) {
        this.currentUser = currentUser;
        this.jobController = new JobController();
        
        view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        view.setBackground(SystemColors.BACKGROUND);
        view.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel sectionTitle = new JLabel("Post a New Job");
        sectionTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        sectionTitle.setForeground(SystemColors.TEXT_PRIMARY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(SystemColors.SURFACE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        formCard.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("Title Details");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(new Color(33, 37, 41));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField titleField = new JTextField();
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        titleField.setPreferredSize(new Dimension(550, 45));
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        titleField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        titleField.setBackground(SystemColors.BACKGROUND);
        titleField.setForeground(new Color(33, 37, 41));
        titleField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField companyField = new JTextField();
        companyField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        companyField.setPreferredSize(new Dimension(550, 45));
        companyField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        companyField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        companyField.setBackground(SystemColors.BACKGROUND);
        companyField.setForeground(new Color(33, 37, 41));
        companyField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel("Job Description");
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblDesc.setForeground(new Color(33, 37, 41));
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea descArea = new JTextArea(4, 50);
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        descArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        descArea.setBackground(SystemColors.BACKGROUND);
        descArea.setForeground(new Color(33, 37, 41));
        JScrollPane descScrollPane = new JScrollPane(descArea);
        descScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        descScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel reqHeader = new JPanel(new BorderLayout());
        reqHeader.setBackground(SystemColors.SURFACE);
        reqHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        reqHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblReq = new JLabel("Requirements");
        lblReq.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblReq.setForeground(new Color(33, 37, 41));

        JButton aiGenBtn = new JButton("✨ Auto-Generate with AI");
        aiGenBtn.setBackground(SystemColors.PRIMARY);
        aiGenBtn.setForeground(Color.BLACK);
        aiGenBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        aiGenBtn.setFocusPainted(false);
        aiGenBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        aiGenBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        reqHeader.add(lblReq, BorderLayout.WEST);
        reqHeader.add(aiGenBtn, BorderLayout.EAST);

        JTextArea reqArea = new JTextArea(4, 50);
        reqArea.setWrapStyleWord(true);
        reqArea.setLineWrap(true);
        reqArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        reqArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        reqArea.setBackground(SystemColors.BACKGROUND);
        reqArea.setForeground(new Color(33, 37, 41));
        JScrollPane reqScrollPane = new JScrollPane(reqArea);
        reqScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        reqScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        aiGenBtn.addActionListener(e -> {
            String combinedData = (titleField.getText() + " " + descArea.getText()).toLowerCase();
            StringBuilder genReq = new StringBuilder("Auto-Generated Requirements:\n");
            
            boolean found = false;
            for (String kw : KEYWORDS) {
                if (combinedData.contains(kw.toLowerCase())) {
                    genReq.append("• Proven experience working with ").append(kw).append(".\n");
                    found = true;
                }
            }
            if (combinedData.contains("senior")) {
                genReq.append("• 5+ years of software development experience.\n");
                found = true;
            } else if (combinedData.contains("junior")) {
                genReq.append("• 1-2 years of software development experience.\n");
                found = true;
            }
            
            if (!found) {
                genReq.append("• Bachelor's degree in Computer Science or related field.\n");
                genReq.append("• Strong communication and problem-solving skills.\n");
            }
            reqArea.setText(genReq.toString());
        });

        JLabel statusLabel = new JLabel();
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton postBtn = new JButton("Post Job Listing");
        postBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        postBtn.setPreferredSize(new Dimension(550, 45));
        postBtn.setBackground(SystemColors.PRIMARY);
        postBtn.setForeground(Color.BLACK);
        postBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        postBtn.setFocusPainted(false);
        postBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        postBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        postBtn.addActionListener(e -> {
            String title = titleField.getText();
            String comp = companyField.getText();
            String desc = descArea.getText();
            String req = reqArea.getText();

            if(title.isEmpty() || comp.isEmpty()) {
                statusLabel.setText("Title and Company are required.");
                statusLabel.setForeground(Color.RED);
                return;
            }

            if (jobController.createJob(currentUser.getId(), title, comp, desc, req)) {
                statusLabel.setText("Job posted successfully!");
                statusLabel.setForeground(new Color(34, 139, 34));
                titleField.setText("");
                companyField.setText("");
                descArea.setText("");
                reqArea.setText("");
            } else {
                statusLabel.setText("Failed to post job.");
                statusLabel.setForeground(Color.RED);
            }
        });

        // Add placeholder labels
        JLabel titlePlaceholder = new JLabel("Job Title (e.g. Senior Java Developer)");
        titlePlaceholder.setForeground(new Color(108, 117, 125));
        titlePlaceholder.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        titlePlaceholder.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel companyPlaceholder = new JLabel("Company Name");
        companyPlaceholder.setForeground(new Color(108, 117, 125));
        companyPlaceholder.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        companyPlaceholder.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descPlaceholder = new JLabel("Job Description (Overview, Role Responsibilities)");
        descPlaceholder.setForeground(new Color(108, 117, 125));
        descPlaceholder.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        descPlaceholder.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel reqPlaceholder = new JLabel("Requirements (e.g. 5+ years experience...)");
        reqPlaceholder.setForeground(new Color(108, 117, 125));
        reqPlaceholder.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        reqPlaceholder.setAlignmentX(Component.LEFT_ALIGNMENT);

        formCard.add(lblTitle);
        formCard.add(Box.createVerticalStrut(5));
        formCard.add(titlePlaceholder);
        formCard.add(Box.createVerticalStrut(3));
        formCard.add(titleField);
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(companyPlaceholder);
        formCard.add(Box.createVerticalStrut(3));
        formCard.add(companyField);
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(lblDesc);
        formCard.add(Box.createVerticalStrut(5));
        formCard.add(descPlaceholder);
        formCard.add(Box.createVerticalStrut(3));
        formCard.add(descScrollPane);
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(reqHeader);
        formCard.add(Box.createVerticalStrut(5));
        formCard.add(reqPlaceholder);
        formCard.add(Box.createVerticalStrut(3));
        formCard.add(reqScrollPane);
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(statusLabel);
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(postBtn);
        
        view.add(sectionTitle);
        view.add(Box.createVerticalStrut(20));
        view.add(formCard);
        view.add(Box.createVerticalGlue());
    }

    public JPanel getView() {
        return view;
    }
}
