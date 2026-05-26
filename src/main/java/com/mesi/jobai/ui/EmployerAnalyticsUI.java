package com.mesi.jobai.ui;

import com.mesi.jobai.dao.ApplicationDAO;
import com.mesi.jobai.model.Application;
import com.mesi.jobai.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;

public class EmployerAnalyticsUI {
    private VBox view;
    private User currentUser;
    private ApplicationDAO applicationDAO;

    public EmployerAnalyticsUI(User currentUser) {
        this.currentUser = currentUser;
        this.applicationDAO = new ApplicationDAO();
        
        view = new VBox(25);
        view.getStyleClass().add("content-area");

        Label sectionTitle = new Label("Employer Analytics Dashboard");
        sectionTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: 800; -fx-text-fill: -text-light;");
        
        List<Application> allApps = applicationDAO.getApplicationsForEmployer(currentUser.getId());
        
        int total = allApps.size();
        int pending = 0;
        int reviewing = 0;
        int interviewing = 0;
        int hired = 0;
        int rejected = 0;
        
        for (Application app : allApps) {
            String stat = app.getStatus();
            if (stat == null) continue;
            switch(stat.toUpperCase()) {
                case "PENDING": pending++; break;
                case "REVIEWING": reviewing++; break;
                case "INTERVIEW": interviewing++; break;
                case "HIRED": hired++; break;
                case "REJECTED": rejected++; break;
                default: pending++; break;
            }
        }

        HBox statsBox = new HBox(20);
        statsBox.getChildren().addAll(
            createStatCard("Total Applications", total, "-accent-color"),
            createStatCard("Pending/Reviewing", pending + reviewing, "#f39c12"),
            createStatCard("Interviews", interviewing, "#3498db"),
            createStatCard("Hired", hired, "-success-color")
        );

        VBox chartCard = new VBox(15);
        chartCard.getStyleClass().add("card");
        chartCard.setPadding(new Insets(20));
        chartCard.setMaxWidth(850);
        chartCard.setAlignment(Pos.CENTER);

        Label chartTitle = new Label("Application Status Distribution");
        chartTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: -text-muted;");

        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(
            new PieChart.Data("Pending", pending),
            new PieChart.Data("Reviewing", reviewing),
            new PieChart.Data("Interviewing", interviewing),
            new PieChart.Data("Hired", hired),
            new PieChart.Data("Rejected", rejected)
        );
        pieChart.setPrefSize(500, 350);
        pieChart.setLegendVisible(true);
        // Force pie chart labels to be visible on dark background via CSS directly or inline
        pieChart.setStyle("-fx-text-fill: -text-light; -fx-pie-label-visible: true;");

        chartCard.getChildren().addAll(chartTitle, pieChart);
        
        view.getChildren().addAll(sectionTitle, statsBox, chartCard);
    }

    private VBox createStatCard(String title, int count, String colorHex) {
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(20, 30, 20, 30));
        card.setAlignment(Pos.CENTER);
        card.setMinWidth(150);

        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: -text-muted;");
        
        Label lblCount = new Label(String.valueOf(count));
        
        // Handle CSS variables or exact hexes
        String fillVal = colorHex.startsWith("-") ? colorHex : colorHex;
        lblCount.setStyle("-fx-font-size: 36px; -fx-font-weight: 800; -fx-text-fill: " + fillVal + ";");

        card.getChildren().addAll(lblTitle, lblCount);
        return card;
    }

    public VBox getView() {
        return view;
    }
}
