package com.mesi.jobai.ui;

import com.mesi.jobai.dao.UserDAO;
import com.mesi.jobai.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginUI {
    private VBox view;
    private Stage stage;
    private UserDAO userDAO;

    public LoginUI(Stage stage) {
        this.stage = stage;
        this.userDAO = new UserDAO();
        view = new VBox(15);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(40));
        view.getStyleClass().add("content-area");

        Label title = new Label("AI Job Recommendation System");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitle = new Label("Login to your account");
        subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: #555;");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button loginBtn = new Button("Login");
        loginBtn.getStyleClass().add("btn-accent");
        loginBtn.setMaxWidth(300);
        
        Button registerLink = new Button("Don't have an account? Register here");
        registerLink.setStyle("-fx-background-color: transparent; -fx-text-fill: #007bff; -fx-cursor: hand;");
        
        loginBtn.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            
            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill all fields.");
                return;
            }
            
            User user = userDAO.loginUser(email, password);
            if (user != null) {
                // Determine if Applicant or Employer
                DashboardUI dashboard = new DashboardUI(user, stage);
                Scene scene = new Scene(dashboard.getView(), 1000, 650);
                try {
                    scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
                } catch (Exception ex) {}
                stage.setScene(scene);
            } else {
                errorLabel.setText("Invalid email or password.");
            }
        });

        registerLink.setOnAction(e -> {
            RegisterUI registerUI = new RegisterUI(stage);
            Scene registerScene = new Scene(registerUI.getView(), 1000, 650);
            try {
                registerScene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            } catch (Exception ex) {}
            stage.setScene(registerScene);
        });

        view.getChildren().addAll(title, subtitle, emailField, passwordField, errorLabel, loginBtn, registerLink);
    }

    public VBox getView() {
        return view;
    }
}
