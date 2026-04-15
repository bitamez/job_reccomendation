package com.mesi.jobai.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import com.mesi.jobai.dao.JobDAO;
import com.mesi.jobai.dao.SkillDAO;
import com.mesi.jobai.model.Job;
import com.mesi.jobai.model.Skill;
import com.mesi.jobai.service.AIService;
import java.util.List;

public class JobListUI {
    private VBox view;
    private DashboardUI dashboard;

    public JobListUI(DashboardUI dashboard) {
        this.dashboard = dashboard;
        view = new VBox(20);
        view.getStyleClass().add("content-area");

        Label sectionTitle = new Label("Welcome, Mesi!");
        sectionTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label recommendedTitle = new Label("Recommended Jobs for You");
        recommendedTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 20 0 10 0;");

        view.getChildren().addAll(sectionTitle, recommendedTitle);

        JobDAO jobDAO = new JobDAO();
        List<Job> jobs = jobDAO.getAllJobs();
        
        SkillDAO skillDAO = new SkillDAO();
        List<Skill> userSkills = skillDAO.getSkillsForUser(dashboard.getCurrentUser().getId());

        if (jobs.isEmpty()) {
            view.getChildren().add(new Label("No jobs available right now. Check back later!"));
        } else {
            for (Job job : jobs) {
                // Calculate score dynamically matching User skills to Job description
                int score = AIService.calculateMatchScore(job, userSkills); 
                view.getChildren().add(createJobCard(job, score + "% Match"));
            }
        }
    }

    private HBox createJobCard(Job job, String matchPercentage) {
        HBox card = new HBox(15);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.CENTER_LEFT);

        VBox textLayout = new VBox(5);
        Label lblTitle = new Label(job.getTitle());
        lblTitle.getStyleClass().add("card-title");
        Label lblSub = new Label(job.getCompany());
        lblSub.getStyleClass().add("card-subtitle");
        textLayout.getChildren().addAll(lblTitle, lblSub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblScore = new Label(matchPercentage);
        lblScore.getStyleClass().add("match-score");

        Button btnViewDetails = new Button("View Details ▸");
        btnViewDetails.getStyleClass().add("btn-primary");
        btnViewDetails.setOnAction(e -> dashboard.showJobDetails(job, matchPercentage));

        card.getChildren().addAll(textLayout, spacer, lblScore, btnViewDetails);
        return card;
    }

    public VBox getView() {
        return view;
    }
}
