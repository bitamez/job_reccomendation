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

public class RegisterUI {
    private BorderPane view;
    private Stage stage;
    private AuthController authController;

    public RegisterUI(Stage stage) {
        this.stage = stage;
        this.authController = new AuthController();
        view = new BorderPane();
        view.getStyleClass().add("root");

        VBox formBox = new VBox(20);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(40, 40, 40, 40));
        formBox.setMaxWidth(450);
        formBox.setMaxHeight(550);
        formBox.getStyleClass().add("card");

        Label title = new Label("Join AI Job Network");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: 800; -fx-text-fill: -text-light;");

        Label subtitle = new Label("Create a new account");
        subtitle.setStyle("-fx-font-size: 15px; -fx-text-fill: -text-muted;");

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        nameField.setMaxWidth(Double.MAX_VALUE);
        nameField.setPrefHeight(45);

        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        emailField.setMaxWidth(Double.MAX_VALUE);
        emailField.setPrefHeight(45);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(Double.MAX_VALUE);
        passwordField.setPrefHeight(45);

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("APPLICANT", "EMPLOYER");
        roleBox.setValue("APPLICANT");
        roleBox.setMaxWidth(Double.MAX_VALUE);
        roleBox.setPrefHeight(45);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: -error-color; -fx-font-weight: bold;");
        Label successLabel = new Label();
        successLabel.setStyle("-fx-text-fill: -success-color; -fx-font-weight: bold;");

        Button registerBtn = new Button("Register Account");
        registerBtn.getStyleClass().add("btn-accent");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setPrefHeight(45);

        Button loginLink = new Button("Already have an account? Sign in");
        loginLink.setStyle("-fx-background-color: transparent; -fx-text-fill: -accent-color; -fx-cursor: hand; -fx-font-weight: bold;");

        registerBtn.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String role = roleBox.getValue();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill all fields.");
                successLabel.setText("");
                return;
            }

            boolean success = authController.register(name, email, password, role);

            if (success) {
                successLabel.setText("Registration successful! You can now sign in.");
                errorLabel.setText("");
                nameField.clear();
                emailField.clear();
                passwordField.clear();
            } else {
                errorLabel.setText("Registration failed. Email might already exist.");
                successLabel.setText("");
            }
        });

        loginLink.setOnAction(e -> {
            LoginUI loginUI = new LoginUI(stage);
            Scene loginScene = new Scene(loginUI.getView(), 1050, 700);
            try {
                loginScene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            } catch (Exception ex) {}
            stage.setScene(loginScene);
        });

        formBox.getChildren().addAll(title, subtitle, nameField, emailField, passwordField, roleBox, errorLabel, successLabel, registerBtn, loginLink);
        view.setCenter(formBox);
    }

    public BorderPane getView() {
        return view;
    }
}
