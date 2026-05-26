package com.mesi.jobai.ui;

import com.mesi.jobai.dao.ApplicationDAO;
import com.mesi.jobai.model.Application;
import com.mesi.jobai.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.List;

public class ApplicationsUI {
    private VBox view;
    private User currentUser;
    private ApplicationDAO applicationDAO;

    public ApplicationsUI(User currentUser) {
        this.currentUser = currentUser;
        this.applicationDAO = new ApplicationDAO();
        
        view = new VBox(25);
        view.getStyleClass().add("content-area");

        Label sectionTitle = new Label("My Applications");
        sectionTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: 800; -fx-text-fill: -text-light;");
        view.getChildren().add(sectionTitle);

        VBox cardBox = new VBox(15);
        cardBox.getStyleClass().add("card");
        cardBox.setPadding(new Insets(20));
        cardBox.setMaxWidth(850);

        TableView<Application> table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setPrefHeight(400);

        TableColumn<Application, String> titleCol = new TableColumn<>("Job Title");
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJobTitle()));
        titleCol.setPrefWidth(250);

        TableColumn<Application, String> companyCol = new TableColumn<>("Company");
        companyCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCompanyName()));
        companyCol.setPrefWidth(200);

        TableColumn<Application, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        statusCol.setPrefWidth(150);

        TableColumn<Application, String> dateCol = new TableColumn<>("Applied On");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAppliedAt()));
        dateCol.setPrefWidth(200);

        table.getColumns().addAll(titleCol, companyCol, statusCol, dateCol);

        List<Application> dbApps = applicationDAO.getApplicationsForUser(currentUser.getId());
        ObservableList<Application> data = FXCollections.observableArrayList(dbApps);
        table.setItems(data);

        cardBox.getChildren().add(table);
        view.getChildren().add(cardBox);
    }

    public VBox getView() {
        return view;
    }
}
