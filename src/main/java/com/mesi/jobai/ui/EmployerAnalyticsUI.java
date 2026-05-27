package com.mesi.jobai.ui;

import com.mesi.jobai.controller.ApplicationController;
import com.mesi.jobai.model.Application;
import com.mesi.jobai.model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EmployerAnalyticsUI {
    private JPanel view;
    private User currentUser;
    private ApplicationController applicationController;

    public EmployerAnalyticsUI(User currentUser) {
        this.currentUser = currentUser;
        this.applicationController = new ApplicationController();
        
        view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        view.setBackground(SystemColors.BACKGROUND);
        view.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel sectionTitle = new JLabel("Employer Analytics Dashboard");
        sectionTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        sectionTitle.setForeground(SystemColors.TEXT_PRIMARY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        List<Application> allApps = applicationController.getApplicationsForEmployer(currentUser.getId());
        
        int total = allApps.size();
        int pending = 0;
        int reviewing = 0;
        int interviewing = 0;
        int hired = 0;
        int rejected = 0;
        
        for (Application app : allApps) {
            String stat = app.getStatus();
            if (stat == null) continue;
            switch(stat.toUpperCase()) {
                case "PENDING": pending++; break;
                case "REVIEWING": reviewing++; break;
                case "INTERVIEW": interviewing++; break;
                case "HIRED": hired++; break;
                case "REJECTED": rejected++; break;
                default: pending++; break;
            }
        }

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        statsPanel.setBackground(SystemColors.BACKGROUND);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        statsPanel.add(createStatCard("Total Applications", total, SystemColors.PRIMARY));
        statsPanel.add(createStatCard("Pending/Reviewing", pending + reviewing, new Color(243, 156, 18)));
        statsPanel.add(createStatCard("Interviews", interviewing, new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Hired", hired, new Color(34, 139, 34)));

        JPanel chartCard = new JPanel();
        chartCard.setLayout(new BoxLayout(chartCard, BoxLayout.Y_AXIS));
        chartCard.setBackground(SystemColors.SURFACE);
        chartCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        chartCard.setMaximumSize(new Dimension(850, Integer.MAX_VALUE));
        chartCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel chartTitle = new JLabel("Application Status Distribution");
        chartTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        chartTitle.setForeground(SystemColors.TEXT_SECONDARY);
        chartTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create a simple text-based chart since Swing doesn't have built-in pie charts
        JPanel chartPanel = new JPanel();
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        chartPanel.setBackground(SystemColors.SURFACE);
        chartPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (total > 0) {
            chartPanel.add(createChartBar("Pending", pending, total, new Color(255, 193, 7)));
            chartPanel.add(Box.createVerticalStrut(10));
            chartPanel.add(createChartBar("Reviewing", reviewing, total, new Color(243, 156, 18)));
            chartPanel.add(Box.createVerticalStrut(10));
            chartPanel.add(createChartBar("Interviewing", interviewing, total, new Color(52, 152, 219)));
            chartPanel.add(Box.createVerticalStrut(10));
            chartPanel.add(createChartBar("Hired", hired, total, new Color(34, 139, 34)));
            chartPanel.add(Box.createVerticalStrut(10));
            chartPanel.add(createChartBar("Rejected", rejected, total, new Color(220, 53, 69)));
        } else {
            JLabel noDataLabel = new JLabel("No application data available");
            noDataLabel.setForeground(SystemColors.TEXT_SECONDARY);
            noDataLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            chartPanel.add(noDataLabel);
        }

        chartCard.add(chartTitle);
        chartCard.add(Box.createVerticalStrut(15));
        chartCard.add(chartPanel);
        
        view.add(sectionTitle);
        view.add(Box.createVerticalStrut(25));
        view.add(statsPanel);
        view.add(Box.createVerticalStrut(25));
        view.add(chartCard);
        view.add(Box.createVerticalGlue());
    }

    private JPanel createStatCard(String title, int count, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(SystemColors.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        card.setPreferredSize(new Dimension(150, 100));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        lblTitle.setForeground(SystemColors.TEXT_SECONDARY);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblCount = new JLabel(String.valueOf(count));
        lblCount.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));
        lblCount.setForeground(color);
        lblCount.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(10));
        card.add(lblCount);
        
        return card;
    }

    private JPanel createChartBar(String label, int value, int total, Color color) {
        JPanel barPanel = new JPanel(new BorderLayout());
        barPanel.setBackground(SystemColors.SURFACE);
        barPanel.setMaximumSize(new Dimension(500, 30));

        JLabel labelText = new JLabel(label + " (" + value + ")");
        labelText.setForeground(SystemColors.TEXT_PRIMARY);
        labelText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        labelText.setPreferredSize(new Dimension(120, 25));

        JPanel barContainer = new JPanel(new BorderLayout());
        barContainer.setBackground(SystemColors.BACKGROUND);
        barContainer.setBorder(BorderFactory.createLineBorder(SystemColors.BORDER, 1));
        barContainer.setPreferredSize(new Dimension(300, 25));

        if (total > 0) {
            int percentage = (int) ((double) value / total * 100);
            int barWidth = (int) ((double) value / total * 298); // 298 to account for border

            JPanel bar = new JPanel();
            bar.setBackground(color);
            bar.setPreferredSize(new Dimension(barWidth, 23));

            JLabel percentageLabel = new JLabel(percentage + "%");
            percentageLabel.setForeground(SystemColors.TEXT_PRIMARY);
            percentageLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
            percentageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            barContainer.add(bar, BorderLayout.WEST);
            barContainer.add(percentageLabel, BorderLayout.CENTER);
        }

        barPanel.add(labelText, BorderLayout.WEST);
        barPanel.add(barContainer, BorderLayout.CENTER);

        return barPanel;
    }

    public JPanel getView() {
        return view;
    }
}
