package com.mesi.jobai.ui;

import com.mesi.jobai.dao.UserDAO;
import com.mesi.jobai.model.User;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UserProfileUI {
    private VBox view;
    private User currentUser;
    private UserDAO userDAO;

    public UserProfileUI(User currentUser) {
        this.currentUser = currentUser;
        this.userDAO = new UserDAO();
        view = new VBox(20);
        view.getStyleClass().add("content-area");

        Label sectionTitle = new Label("My Profile");
        sectionTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label roleLabel = new Label("Account Type: " + currentUser.getRole());
        roleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555; -fx-font-style: italic;");

        TextField nameField = new TextField(currentUser.getName());
        nameField.setPromptText("Full Name");
        nameField.setMaxWidth(300);

        TextField emailField = new TextField(currentUser.getEmail());
        emailField.setPromptText("Email Address");
        emailField.setMaxWidth(300);
        // We often disable email editing to keep it simple, but let's allow it for now if they want to update

        Label statusLabel = new Label();

        Button updateBtn = new Button("Save Changes");
        updateBtn.getStyleClass().add("btn-accent");
        
        updateBtn.setOnAction(e -> {
            String newName = nameField.getText();
            String newEmail = emailField.getText();
            
            if (newName.isEmpty() || newEmail.isEmpty()) {
                statusLabel.setText("Fields cannot be empty.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            currentUser.setName(newName);
            currentUser.setEmail(newEmail);
            
            if (userDAO.updateUser(currentUser)) {
                statusLabel.setText("Profile updated successfully!");
                statusLabel.setStyle("-fx-text-fill: green;");
            } else {
                statusLabel.setText("Failed to update profile. Email might be in use.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        view.getChildren().addAll(sectionTitle, roleLabel, 
            new Label("Name:"), nameField, 
            new Label("Email:"), emailField, 
            statusLabel, updateBtn);
    }

    public VBox getView() {
        return view;
    }
}
