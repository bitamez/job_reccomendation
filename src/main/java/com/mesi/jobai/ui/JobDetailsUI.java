package com.mesi.jobai.ui;

import com.mesi.jobai.controller.ApplicationController;
import com.mesi.jobai.model.Job;
import com.mesi.jobai.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class JobDetailsUI {
    private VBox view;
    private ApplicationController applicationController;

    public JobDetailsUI(DashboardUI dashboard, Job job, String matchScore, User currentUser) {
        this.applicationController = new ApplicationController();
        view = new VBox(25);
        view.getStyleClass().add("content-area");
        view.setAlignment(Pos.TOP_LEFT);

        Label sectionTitle = new Label("Job Details");
        sectionTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: 800; -fx-text-fill: -text-light;");

        VBox cardBox = new VBox(15);
        cardBox.getStyleClass().add("card");
        cardBox.setPadding(new Insets(35));
        cardBox.setMaxWidth(800);

        Label jobTitle = new Label(job.getTitle() + " @ " + job.getCompany());
        jobTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: -text-light;");

        Label scoreLabel = new Label("AI Match Score: " + matchScore);
        scoreLabel.getStyleClass().add("match-score");
        scoreLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 0 0 15 0;");

        Label descTitle = new Label("Job Description");
        descTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: -text-muted; -fx-padding: 10 0 5 0;");

        Label description = new Label(job.getDescription());
        description.setWrapText(true);
        description.setStyle("-fx-text-fill: -text-dark; -fx-font-size: 14px; -fx-line-spacing: 5px;");

        Label reqTitle = new Label("Requirements");
        reqTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: -text-muted; -fx-padding: 15 0 5 0;");

        Label reqDesc = new Label(job.getRequirements());
        reqDesc.setWrapText(true);
        reqDesc.setStyle("-fx-text-fill: -text-dark; -fx-font-size: 14px; -fx-line-spacing: 5px;");

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        Button btnApply = new Button("Apply Now");
        btnApply.getStyleClass().add("btn-accent");
        btnApply.setPrefHeight(40);
        
        btnApply.setOnAction(e -> {
            if (applicationController.applyForJob(job.getId(), currentUser.getId())) {
                statusLabel.setText("Successfully Applied!");
                statusLabel.setStyle("-fx-text-fill: -success-color;");
                btnApply.setDisable(true);
            } else {
                statusLabel.setText("Failed to apply. You may have already applied.");
                statusLabel.setStyle("-fx-text-fill: -error-color;");
            }
        });

        Button btnBack = new Button("← Back to Jobs");
        btnBack.getStyleClass().add("btn-primary");
        btnBack.setPrefHeight(40);
        btnBack.setOnAction(e -> dashboard.showJobList());

        buttonBox.getChildren().addAll(btnBack, btnApply, statusLabel);

        cardBox.getChildren().addAll(jobTitle, scoreLabel, descTitle, description, reqTitle, reqDesc, buttonBox);
        view.getChildren().addAll(sectionTitle, cardBox);
    }

    public VBox getView() {
        return view;
    }
}
