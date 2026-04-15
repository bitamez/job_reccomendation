package com.mesi.jobai.ui;

import com.mesi.jobai.dao.ApplicationDAO;
import com.mesi.jobai.model.Job;
import com.mesi.jobai.model.User;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class JobDetailsUI {
    private VBox view;
    private ApplicationDAO applicationDAO;

    public JobDetailsUI(DashboardUI dashboard, Job job, String matchScore, User currentUser) {
        this.applicationDAO = new ApplicationDAO();
        view = new VBox(20);
        view.getStyleClass().add("content-area");

        Label sectionTitle = new Label("Job Details");
        sectionTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label jobTitle = new Label(job.getTitle() + " at " + job.getCompany());
        jobTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label scoreLabel = new Label("AI Match Score: " + matchScore);
        scoreLabel.getStyleClass().add("match-score");
        scoreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label descTitle = new Label("Job Description");
        descTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 0 5 0;");

        Label description = new Label(
            "Description:\n" + job.getDescription() + "\n\n" +
            "Requirements:\n" + job.getRequirements()
        );
        description.setWrapText(true);
        description.setStyle("-fx-text-fill: #555555; -fx-font-size: 13px;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button btnApply = new Button("Apply Now");
        btnApply.getStyleClass().add("btn-accent");
        
        btnApply.setOnAction(e -> {
            if (applicationDAO.applyForJob(job.getId(), currentUser.getId())) {
                statusLabel.setText("Successfully Applied!");
                statusLabel.setStyle("-fx-text-fill: green;");
                btnApply.setDisable(true);
            } else {
                statusLabel.setText("Failed to apply. You may have already applied.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        Button btnBack = new Button("← Back to Jobs");
        btnBack.getStyleClass().add("btn-primary");
        btnBack.setOnAction(e -> dashboard.showJobList());

        buttonBox.getChildren().addAll(btnBack, btnApply, statusLabel);

        view.getChildren().addAll(sectionTitle, jobTitle, scoreLabel, descTitle, description, buttonBox);
    }

    public VBox getView() {
        return view;
    }
}
