package com.mesi.jobai;

import com.mesi.jobai.config.DBConnection;
import com.mesi.jobai.ui.LoginUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize DB schema
        DBConnection.initializeDatabase();

        // Boot to Login
        LoginUI loginUI = new LoginUI(primaryStage);
        
        Scene scene = new Scene(loginUI.getView(), 1000, 650);
        
        try {
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Could not load styles/style.css");
        }

        primaryStage.setTitle("AI Job Recommendation System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
