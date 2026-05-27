package com.mesi.jobai.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import com.mesi.jobai.controller.JobController;
import com.mesi.jobai.controller.RecommendationController;
import com.mesi.jobai.model.Job;
import com.mesi.jobai.model.Skill;
import com.mesi.jobai.service.AIService;
import java.util.List;

public class JobListUI {
    private JPanel view;
    private DashboardUI dashboard;
    private JPanel jobListContainer;
    private List<Job> allJobs;
    private List<Skill> userSkills;

    public JobListUI(DashboardUI dashboard) {
        this.dashboard = dashboard;
        view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        view.setBackground(SystemColors.BACKGROUND);
        view.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel sectionTitle = new JLabel("Welcome, " + dashboard.getCurrentUser().getName().split(" ")[0] + "!");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        sectionTitle.setForeground(new Color(33, 37, 41));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel topArea = new JPanel(new BorderLayout());
        topArea.setBackground(SystemColors.BACKGROUND);
        topArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel recommendedTitle = new JLabel("Recommended Jobs");
        recommendedTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        recommendedTitle.setForeground(new Color(73, 80, 87));

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 40));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        searchField.setBackground(SystemColors.SURFACE);
        searchField.setForeground(SystemColors.TEXT_PRIMARY);

        topArea.add(recommendedTitle, BorderLayout.WEST);
        topArea.add(searchField, BorderLayout.EAST);

        view.add(sectionTitle);
        view.add(Box.createVerticalStrut(20));
        view.add(topArea);
        view.add(Box.createVerticalStrut(20));

        JobController jobController = new JobController();
        allJobs = jobController.getAllJobs();
        
        RecommendationController recommendationController = new RecommendationController();
        userSkills = recommendationController.getUserSkills(dashboard.getCurrentUser().getId());

        jobListContainer = new JPanel();
        jobListContainer.setLayout(new BoxLayout(jobListContainer, BoxLayout.Y_AXIS));
        jobListContainer.setBackground(SystemColors.BACKGROUND);
        jobListContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(jobListContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setBackground(SystemColors.BACKGROUND);
        scrollPane.getViewport().setBackground(SystemColors.BACKGROUND);

        view.add(scrollPane);

        if (allJobs.isEmpty()) {
            JLabel emptyLbl = new JLabel("No jobs available right now. Check back later!");
            emptyLbl.setForeground(new Color(73, 80, 87));
            emptyLbl.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            jobListContainer.add(emptyLbl);
        } else {
            // Sort jobs by highest AI match score (descending) initially
            allJobs.sort((j1, j2) -> {
                int score1 = AIService.calculateMatchScore(j1, userSkills);
                int score2 = AIService.calculateMatchScore(j2, userSkills);
                return Integer.compare(score2, score1);
            });

            // Populate all jobs first
            populateJobs("");

            // Setup real-time search filtering
            searchField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    populateJobs(searchField.getText());
                }
            });
        }
    }

    private void populateJobs(String filterText) {
        jobListContainer.removeAll();
        String lowerFilter = filterText.toLowerCase();

        boolean foundAny = false;
        for (Job job : allJobs) {
            if (job.getTitle().toLowerCase().contains(lowerFilter) || 
                job.getCompany().toLowerCase().contains(lowerFilter)) {
                
                int score = AIService.calculateMatchScore(job, userSkills); 
                jobListContainer.add(createJobCard(job, score + "% Match"));
                jobListContainer.add(Box.createVerticalStrut(15));
                foundAny = true;
            }
        }

        if (!foundAny) {
            JLabel emptyLbl = new JLabel("No jobs match your search: '" + filterText + "'");
            emptyLbl.setForeground(new Color(73, 80, 87));
            emptyLbl.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            jobListContainer.add(emptyLbl);
        }

        jobListContainer.revalidate();
        jobListContainer.repaint();
    }

    private JPanel createJobCard(Job job, String matchPercentage) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(SystemColors.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel textLayout = new JPanel();
        textLayout.setLayout(new BoxLayout(textLayout, BoxLayout.Y_AXIS));
        textLayout.setBackground(SystemColors.SURFACE);

        JLabel lblTitle = new JLabel(job.getTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(33, 37, 41));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel(job.getCompany());
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblSub.setForeground(SystemColors.PRIMARY);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        textLayout.add(lblTitle);
        textLayout.add(Box.createVerticalStrut(8));
        textLayout.add(lblSub);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(SystemColors.SURFACE);

        JLabel lblScore = new JLabel(matchPercentage);
        lblScore.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblScore.setForeground(new Color(73, 80, 87));

        JButton btnViewDetails = new JButton("View Details ▸");
        btnViewDetails.setPreferredSize(new Dimension(120, 40));
        btnViewDetails.setBackground(SystemColors.PRIMARY);
        btnViewDetails.setForeground(Color.BLACK);
        btnViewDetails.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnViewDetails.setFocusPainted(false);
        btnViewDetails.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnViewDetails.addActionListener(e -> dashboard.showJobDetails(job, matchPercentage));

        rightPanel.add(lblScore);
        rightPanel.add(btnViewDetails);

        card.add(textLayout, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }

    public JPanel getView() {
        return view;
    }
}
