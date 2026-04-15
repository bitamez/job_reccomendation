package com.mesi.jobai.ui;

import com.mesi.jobai.dao.UserDAO;
import com.mesi.jobai.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterUI {
    private VBox view;
    private Stage stage;
    private UserDAO userDAO;

    public RegisterUI(Stage stage) {
        this.stage = stage;
        this.userDAO = new UserDAO();
        view = new VBox(15);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(40));
        view.getStyleClass().add("content-area");

        Label title = new Label("Join the AI Job Network");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        nameField.setMaxWidth(300);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("APPLICANT", "EMPLOYER");
        roleBox.setValue("APPLICANT");
        roleBox.setMaxWidth(300);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        Label successLabel = new Label();
        successLabel.setStyle("-fx-text-fill: green;");

        Button registerBtn = new Button("Register");
        registerBtn.getStyleClass().add("btn-accent");
        registerBtn.setMaxWidth(300);

        Button loginLink = new Button("Already have an account? Login here");
        loginLink.setStyle("-fx-background-color: transparent; -fx-text-fill: #007bff; -fx-cursor: hand;");

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

            User newUser = new User(0, name, email, password, role);
            boolean success = userDAO.registerUser(newUser);

            if (success) {
                successLabel.setText("Registration successful! You can now login.");
                errorLabel.setText("");
            } else {
                errorLabel.setText("Registration failed. Email might already exist.");
                successLabel.setText("");
            }
        });

        loginLink.setOnAction(e -> {
            LoginUI loginUI = new LoginUI(stage);
            Scene loginScene = new Scene(loginUI.getView(), 1000, 650);
            try {
                loginScene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            } catch (Exception ex) {}
            stage.setScene(loginScene);
        });

        view.getChildren().addAll(title, nameField, emailField, passwordField, roleBox, errorLabel, successLabel, registerBtn, loginLink);
    }

    public VBox getView() {
        return view;
    }
}
