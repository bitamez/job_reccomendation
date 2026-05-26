package com.mesi.jobai.ui;

import com.mesi.jobai.controller.JobController;
import com.mesi.jobai.model.Job;
import com.mesi.jobai.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class PostJobUI {
    private VBox view;
    private User currentUser;
    private JobController jobController;

    private static final String[] KEYWORDS = {
        "Java", "Python", "React", "Angular", "Vue", "AWS", "Docker", "SQL", "Management"
    };

    public PostJobUI(User currentUser) {
        this.currentUser = currentUser;
        this.jobController = new JobController();
        
        view = new VBox(20);
        view.getStyleClass().add("content-area");
        view.setAlignment(Pos.TOP_LEFT);

        Label sectionTitle = new Label("Post a New Job");
        sectionTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: 800; -fx-text-fill: -text-light;");

        VBox formCard = new VBox(15);
        formCard.getStyleClass().add("card");
        formCard.setPadding(new Insets(30));
        formCard.setMaxWidth(600);

        TextField titleField = new TextField();
        titleField.setPromptText("Job Title (e.g. Senior Java Developer)");
        titleField.setMaxWidth(Double.MAX_VALUE);
        titleField.setPrefHeight(45);

        TextField companyField = new TextField();
        companyField.setPromptText("Company Name");
        companyField.setMaxWidth(Double.MAX_VALUE);
        companyField.setPrefHeight(45);

        TextArea descArea = new TextArea();
        descArea.setPromptText("Job Description (Overview, Role Responsibilities)");
        descArea.setMaxWidth(Double.MAX_VALUE);
        descArea.setPrefRowCount(4);
        descArea.setStyle("-fx-background-color: #1f2335; -fx-text-fill: -text-dark; -fx-border-color: rgba(255,255,255,0.1); -fx-border-radius: 8; -fx-control-inner-background: #1f2335;");

        HBox reqHeader = new HBox();
        reqHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label lblReq = new Label("Requirements");
        lblReq.setStyle("-fx-text-fill: -text-muted; -fx-font-weight: bold;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button aiGenBtn = new Button("✨ Auto-Generate with AI");
        aiGenBtn.getStyleClass().add("btn-primary");
        aiGenBtn.setStyle("-fx-font-size: 12px; -fx-padding: 5 10 5 10;");

        reqHeader.getChildren().addAll(lblReq, spacer, aiGenBtn);

        TextArea reqArea = new TextArea();
        reqArea.setPromptText("Requirements (e.g. 5+ years experience...)");
        reqArea.setMaxWidth(Double.MAX_VALUE);
        reqArea.setPrefRowCount(4);
        reqArea.setStyle("-fx-background-color: #1f2335; -fx-text-fill: -text-dark; -fx-border-color: rgba(255,255,255,0.1); -fx-border-radius: 8; -fx-control-inner-background: #1f2335;");

        aiGenBtn.setOnAction(e -> {
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

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-weight: bold;");

        Button postBtn = new Button("Post Job Listing");
        postBtn.getStyleClass().add("btn-accent");
        postBtn.setMaxWidth(Double.MAX_VALUE);
        postBtn.setPrefHeight(45);
        
        postBtn.setOnAction(e -> {
            String title = titleField.getText();
            String comp = companyField.getText();
            String desc = descArea.getText();
            String req = reqArea.getText();

            if(title.isEmpty() || comp.isEmpty()) {
                statusLabel.setText("Title and Company are required.");
                statusLabel.setStyle("-fx-text-fill: -error-color; -fx-font-weight: bold;");
                return;
            }

            if (jobController.createJob(currentUser.getId(), title, comp, desc, req)) {
                statusLabel.setText("Job posted successfully!");
                statusLabel.setStyle("-fx-text-fill: -success-color; -fx-font-weight: bold;");
                titleField.clear();
                companyField.clear();
                descArea.clear();
                reqArea.clear();
            } else {
                statusLabel.setText("Failed to post job.");
                statusLabel.setStyle("-fx-text-fill: -error-color; -fx-font-weight: bold;");
            }
        });

        Label lblTitle = new Label("Title Details");
        lblTitle.setStyle("-fx-text-fill: -text-muted; -fx-font-weight: bold;");
        Label lblDesc = new Label("Job Description");
        lblDesc.setStyle("-fx-text-fill: -text-muted; -fx-font-weight: bold;");

        formCard.getChildren().addAll(lblTitle, titleField, companyField, lblDesc, descArea, reqHeader, reqArea, statusLabel, postBtn);
        
        view.getChildren().addAll(sectionTitle, formCard);
    }

    public VBox getView() {
        return view;
    }
}
