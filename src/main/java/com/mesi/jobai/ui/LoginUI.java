package com.mesi.jobai.ui;

import com.mesi.jobai.controller.AuthController;
import com.mesi.jobai.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginUI {
    private BorderPane view;
    private Stage stage;
    private AuthController authController;

    public LoginUI(Stage stage) {
        this.stage = stage;
        this.authController = new AuthController();
        view = new BorderPane();
        view.getStyleClass().add("root");

        VBox formBox = new VBox(20);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(50, 40, 50, 40));
        formBox.setMaxWidth(450);
        formBox.setMaxHeight(450);
        formBox.getStyleClass().add("card");

        Label title = new Label("AI Job Recommendation");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: 800; -fx-text-fill: -text-light;");

        Label subtitle = new Label("Sign in to your account");
        subtitle.setStyle("-fx-font-size: 15px; -fx-text-fill: -text-muted;");

        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        emailField.setMaxWidth(Double.MAX_VALUE);
        emailField.setPrefHeight(45);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(Double.MAX_VALUE);
        passwordField.setPrefHeight(45);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: -error-color; -fx-font-weight: bold;");

        Button loginBtn = new Button("Login");
        loginBtn.getStyleClass().add("btn-accent");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(45);
        
        Button registerLink = new Button("Don't have an account? Register");
        registerLink.setStyle("-fx-background-color: transparent; -fx-text-fill: -accent-color; -fx-cursor: hand; -fx-font-weight: bold;");
        
        loginBtn.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            
            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill all fields.");
                return;
            }
            
            User user = authController.login(email, password);
            if (user != null) {
                // Determine if Applicant or Employer
                DashboardUI dashboard = new DashboardUI(user, stage);
                Scene scene = new Scene(dashboard.getView(), 1050, 700);
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
            Scene registerScene = new Scene(registerUI.getView(), 1050, 700);
            try {
                registerScene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            } catch (Exception ex) {}
            stage.setScene(registerScene);
        });

        formBox.getChildren().addAll(title, subtitle, emailField, passwordField, errorLabel, loginBtn, registerLink);
        view.setCenter(formBox);
    }

    public BorderPane getView() {
        return view;
    }
}
