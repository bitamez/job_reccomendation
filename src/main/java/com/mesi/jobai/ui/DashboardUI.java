package com.mesi.jobai.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import com.mesi.jobai.model.User;
import javafx.stage.Stage;

public class DashboardUI {
    private BorderPane mainLayout;
    private VBox sidebar;
    private HBox header;

    private JobListUI jobList;
    private ApplicationsUI applications;
    private PostJobUI postJobUI;
    private UserProfileUI userProfileUI;
    private SkillsUI skillsUI;
    private EmployerApplicationsUI employerApplicationsUI;
    private EmployerAnalyticsUI employerAnalyticsUI;
    private User currentUser;
    private Stage stage;

    public DashboardUI(User user, Stage stage) {
        this.currentUser = user;
        this.stage = stage;
        mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("root");

        createHeader();
        createSidebar();

        this.jobList = new JobListUI(this);
        this.applications = new ApplicationsUI(currentUser);
        this.postJobUI = new PostJobUI(currentUser);
        this.userProfileUI = new UserProfileUI(currentUser);
        this.skillsUI = new SkillsUI(currentUser);
        this.employerApplicationsUI = new EmployerApplicationsUI(currentUser);
        this.employerAnalyticsUI = new EmployerAnalyticsUI(currentUser);

        mainLayout.setTop(header);
        mainLayout.setLeft(sidebar);
        
        // Initial center view logic based on role
        if (currentUser.getRole().equals("EMPLOYER")) {
            mainLayout.setCenter(employerAnalyticsUI.getView());
        } else {
            mainLayout.setCenter(jobList.getView());
        }
    }

    private void createHeader() {
        header = new HBox();
        header.getStyleClass().add("top-header");
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("AI Job Recommendation System");
        titleLabel.getStyleClass().add("header-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userIcon = new Label("👤 " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        userIcon.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ff6b6b; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> {
            LoginUI loginUI = new LoginUI(stage);
            javafx.scene.Scene scene = new javafx.scene.Scene(loginUI.getView(), 1000, 650);
            try {
                scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            } catch (Exception ex) {}
            stage.setScene(scene);
        });

        header.getChildren().addAll(titleLabel, spacer, userIcon, logoutBtn);
    }

    private void createSidebar() {
        sidebar = new VBox(10);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(200);

        Button btnDashboard = new Button("Dashboard");
        btnDashboard.getStyleClass().addAll("sidebar-btn", "active");
        btnDashboard.setMaxWidth(Double.MAX_VALUE);

        Button btnApplications = new Button(currentUser.getRole().equals("EMPLOYER") ? "Post a Job" : "My Applications");
        btnApplications.getStyleClass().add("sidebar-btn");
        btnApplications.setMaxWidth(Double.MAX_VALUE);

        Button btnSkills = new Button("My Skills");
        btnSkills.getStyleClass().add("sidebar-btn");
        btnSkills.setMaxWidth(Double.MAX_VALUE);

        Button btnProfile = new Button("My Profile");
        btnProfile.getStyleClass().add("sidebar-btn");
        btnProfile.setMaxWidth(Double.MAX_VALUE);

        Button btnViewApplicants = new Button("Review Applicants");
        btnViewApplicants.getStyleClass().add("sidebar-btn");
        btnViewApplicants.setMaxWidth(Double.MAX_VALUE);

        btnDashboard.setOnAction(e -> {
             if (currentUser.getRole().equals("EMPLOYER")) {
                 mainLayout.setCenter(employerAnalyticsUI.getView());
             } else {
                 mainLayout.setCenter(jobList.getView());
             }
             setActive(btnDashboard, btnApplications, btnSkills, btnProfile, btnViewApplicants);
        });

        btnApplications.setOnAction(e -> {
             if (currentUser.getRole().equals("EMPLOYER")) {
                 mainLayout.setCenter(postJobUI.getView());
             } else {
                 mainLayout.setCenter(applications.getView());
             }
             setActive(btnApplications, btnDashboard, btnSkills, btnProfile, btnViewApplicants);
        });

        btnSkills.setOnAction(e -> {
             mainLayout.setCenter(skillsUI.getView());
             setActive(btnSkills, btnDashboard, btnApplications, btnProfile, btnViewApplicants);
        });

        btnProfile.setOnAction(e -> {
             mainLayout.setCenter(userProfileUI.getView());
             setActive(btnProfile, btnDashboard, btnApplications, btnSkills, btnViewApplicants);
        });

        btnViewApplicants.setOnAction(e -> {
             mainLayout.setCenter(employerApplicationsUI.getView());
             setActive(btnViewApplicants, btnDashboard, btnApplications, btnProfile);
        });

        if (currentUser.getRole().equals("EMPLOYER")) {
            sidebar.getChildren().addAll(btnDashboard, btnApplications, btnViewApplicants, btnProfile);
        } else {
            sidebar.getChildren().addAll(btnDashboard, btnApplications, btnSkills, btnProfile);
        }
    }

    private void setActive(Button activeBtn, Button... others) {
        activeBtn.getStyleClass().add("active");
        for (Button btn : others) {
            btn.getStyleClass().remove("active");
        }
    }

    public void showJobDetails(com.mesi.jobai.model.Job job, String matchScore) {
        JobDetailsUI details = new JobDetailsUI(this, job, matchScore, currentUser);
        mainLayout.setCenter(details.getView());
    }

    public void showJobList() {
        mainLayout.setCenter(jobList.getView());
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public BorderPane getView() {
        return mainLayout;
    }
}
