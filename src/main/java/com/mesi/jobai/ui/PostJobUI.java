package com.mesi.jobai.ui;

import com.mesi.jobai.dao.JobDAO;
import com.mesi.jobai.model.Job;
import com.mesi.jobai.model.User;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class PostJobUI {
    private VBox view;
    private User currentUser;
    private JobDAO jobDAO;

    public PostJobUI(User currentUser) {
        this.currentUser = currentUser;
        this.jobDAO = new JobDAO();
        
        view = new VBox(20);
        view.getStyleClass().add("content-area");

        Label sectionTitle = new Label("Post a New Job");
        sectionTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField titleField = new TextField();
        titleField.setPromptText("Job Title (e.g. Senior Java Developer)");
        titleField.setMaxWidth(400);

        TextField companyField = new TextField();
        companyField.setPromptText("Company Name");
        companyField.setMaxWidth(400);

        TextArea descArea = new TextArea();
        descArea.setPromptText("Job Description");
        descArea.setMaxWidth(400);
        descArea.setPrefRowCount(4);

        TextArea reqArea = new TextArea();
        reqArea.setPromptText("Requirements (e.g. 5+ years experience...)");
        reqArea.setMaxWidth(400);
        reqArea.setPrefRowCount(4);

        Label statusLabel = new Label();

        Button postBtn = new Button("Post Job");
        postBtn.getStyleClass().add("btn-accent");
        postBtn.setOnAction(e -> {
            String title = titleField.getText();
            String comp = companyField.getText();
            String desc = descArea.getText();
            String req = reqArea.getText();

            if(title.isEmpty() || comp.isEmpty()) {
                statusLabel.setText("Title and Company are required.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            Job job = new Job(0, currentUser.getId(), title, comp, desc, req);
            if (jobDAO.createJob(job)) {
                statusLabel.setText("Job posted successfully!");
                statusLabel.setStyle("-fx-text-fill: green;");
                titleField.clear();
                companyField.clear();
                descArea.clear();
                reqArea.clear();
            } else {
                statusLabel.setText("Failed to post job.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        view.getChildren().addAll(sectionTitle, new Label("Title:"), titleField, 
            new Label("Company:"), companyField, 
            new Label("Description:"), descArea, 
            new Label("Requirements:"), reqArea, 
            statusLabel, postBtn);
    }

    public VBox getView() {
        return view;
    }
}
