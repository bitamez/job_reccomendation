package com.mesi.jobai.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import com.mesi.jobai.controller.JobController;
import com.mesi.jobai.controller.RecommendationController;
import com.mesi.jobai.model.Job;
import com.mesi.jobai.model.Skill;
import com.mesi.jobai.service.AIService;
import java.util.List;

public class JobListUI {
    private VBox view;
    private DashboardUI dashboard;
    private VBox jobListContainer;
    private List<Job> allJobs;
    private List<Skill> userSkills;

    public JobListUI(DashboardUI dashboard) {
        this.dashboard = dashboard;
        view = new VBox(20);
        view.getStyleClass().add("content-area");

        Label sectionTitle = new Label("Welcome, " + dashboard.getCurrentUser().getName().split(" ")[0] + "!");
        sectionTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: 800; -fx-text-fill: -text-light;");

        HBox topArea = new HBox(20);
        topArea.setAlignment(Pos.CENTER_LEFT);

        Label recommendedTitle = new Label("Recommended Jobs");
        recommendedTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: -text-muted;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        TextField searchField = new TextField();
        searchField.setPromptText("Search jobs by title or company...");
        searchField.setPrefWidth(300);
        searchField.setPrefHeight(40);

        topArea.getChildren().addAll(recommendedTitle, spacer, searchField);
        view.getChildren().addAll(sectionTitle, topArea);

        JobController jobController = new JobController();
        allJobs = jobController.getAllJobs();
        
        RecommendationController recommendationController = new RecommendationController();
        userSkills = recommendationController.getUserSkills(dashboard.getCurrentUser().getId());

        jobListContainer = new VBox(15);
        jobListContainer.setMaxWidth(850);
        view.getChildren().add(jobListContainer);

        if (allJobs.isEmpty()) {
            Label emptyLbl = new Label("No jobs available right now. Check back later!");
            emptyLbl.setStyle("-fx-text-fill: -text-muted; -fx-font-style: italic;");
            jobListContainer.getChildren().add(emptyLbl);
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
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                populateJobs(newValue);
            });
        }
    }

    private void populateJobs(String filterText) {
        jobListContainer.getChildren().clear();
        String lowerFilter = filterText.toLowerCase();

        boolean foundAny = false;
        for (Job job : allJobs) {
            if (job.getTitle().toLowerCase().contains(lowerFilter) || 
                job.getCompany().toLowerCase().contains(lowerFilter)) {
                
                int score = AIService.calculateMatchScore(job, userSkills); 
                jobListContainer.getChildren().add(createJobCard(job, score + "% Match"));
                foundAny = true;
            }
        }

        if (!foundAny) {
            Label emptyLbl = new Label("No jobs match your search: '" + filterText + "'");
            emptyLbl.setStyle("-fx-text-fill: -text-muted; -fx-font-style: italic;");
            jobListContainer.getChildren().add(emptyLbl);
        }
    }

    private HBox createJobCard(Job job, String matchPercentage) {
        HBox card = new HBox(20);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(25));

        VBox textLayout = new VBox(8);
        Label lblTitle = new Label(job.getTitle());
        lblTitle.getStyleClass().add("card-title");
        lblTitle.setStyle("-fx-font-size: 20px;");
        Label lblSub = new Label(job.getCompany());
        lblSub.getStyleClass().add("card-subtitle");
        lblSub.setStyle("-fx-font-size: 15px; -fx-text-fill: -accent-color; -fx-font-weight: bold;");
        textLayout.getChildren().addAll(lblTitle, lblSub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblScore = new Label(matchPercentage);
        lblScore.getStyleClass().add("match-score");
        lblScore.setStyle("-fx-padding: 0 20 0 0;");

        Button btnViewDetails = new Button("View Details ▸");
        btnViewDetails.getStyleClass().add("btn-primary");
        btnViewDetails.setPrefHeight(40);
        btnViewDetails.setOnAction(e -> dashboard.showJobDetails(job, matchPercentage));

        card.getChildren().addAll(textLayout, spacer, lblScore, btnViewDetails);
        return card;
    }

    public VBox getView() {
        return view;
    }
}
