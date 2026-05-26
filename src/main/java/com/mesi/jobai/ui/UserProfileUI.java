package com.mesi.jobai.ui;

import com.mesi.jobai.controller.AuthController;
import com.mesi.jobai.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UserProfileUI {
    private VBox view;
    private User currentUser;
    private AuthController authController;

    public UserProfileUI(User currentUser) {
        this.currentUser = currentUser;
        this.authController = new AuthController();
        view = new VBox(25);
        view.getStyleClass().add("content-area");
        view.setAlignment(Pos.TOP_LEFT);

        Label sectionTitle = new Label("My Profile");
        sectionTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: 800; -fx-text-fill: -text-light;");

        VBox formCard = new VBox(15);
        formCard.getStyleClass().add("card");
        formCard.setPadding(new Insets(30));
        formCard.setMaxWidth(500);

        Label roleLabel = new Label("Account Type: " + currentUser.getRole());
        roleLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: -accent-color; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");

        TextField nameField = new TextField(currentUser.getName());
        nameField.setPromptText("Full Name");
        nameField.setMaxWidth(Double.MAX_VALUE);
        nameField.setPrefHeight(45);

        TextField emailField = new TextField(currentUser.getEmail());
        emailField.setPromptText("Email Address");
        emailField.setMaxWidth(Double.MAX_VALUE);
        emailField.setPrefHeight(45);
        // We often disable email editing to keep it simple, but let's allow it for now if they want to update

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-weight: bold;");

        Button updateBtn = new Button("Save Changes");
        updateBtn.getStyleClass().add("btn-accent");
        updateBtn.setMaxWidth(Double.MAX_VALUE);
        updateBtn.setPrefHeight(45);
        
        updateBtn.setOnAction(e -> {
            String newName = nameField.getText();
            String newEmail = emailField.getText();
            
            if (newName.isEmpty() || newEmail.isEmpty()) {
                statusLabel.setText("Fields cannot be empty.");
                statusLabel.setStyle("-fx-text-fill: -error-color;");
                return;
            }
            
            currentUser.setName(newName);
            currentUser.setEmail(newEmail);
            
            if (authController.updateProfile(currentUser)) {
                statusLabel.setText("Profile updated successfully!");
                statusLabel.setStyle("-fx-text-fill: -success-color;");
            } else {
                statusLabel.setText("Failed to update profile. Email might be in use.");
                statusLabel.setStyle("-fx-text-fill: -error-color;");
            }
        });

        Label lblName = new Label("Full Name");
        lblName.setStyle("-fx-text-fill: -text-muted; -fx-font-weight: bold;");
        Label lblEmail = new Label("Email Address");
        lblEmail.setStyle("-fx-text-fill: -text-muted; -fx-font-weight: bold;");

        formCard.getChildren().addAll(roleLabel, lblName, nameField, lblEmail, emailField, statusLabel, updateBtn);

        view.getChildren().addAll(sectionTitle, formCard);
    }

    public VBox getView() {
        return view;
    }
}
