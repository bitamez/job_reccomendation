package com.mesi.jobai.ui;

import com.mesi.jobai.controller.ApplicationController;
import com.mesi.jobai.model.Application;
import com.mesi.jobai.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.List;

public class EmployerApplicationsUI {
    private VBox view;
    private User currentUser;
    private ApplicationController applicationController;

    public EmployerApplicationsUI(User currentUser) {
        this.currentUser = currentUser;
        this.applicationController = new ApplicationController();
        
        view = new VBox(25);
        view.getStyleClass().add("content-area");

        Label sectionTitle = new Label("Review Job Applicants");
        sectionTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: 800; -fx-text-fill: -text-light;");
        view.getChildren().add(sectionTitle);

        VBox cardBox = new VBox(15);
        cardBox.getStyleClass().add("card");
        cardBox.setPadding(new Insets(20));
        cardBox.setMaxWidth(850);

        TableView<Application> table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setPrefHeight(400);

        TableColumn<Application, String> applicantCol = new TableColumn<>("Applicant Name");
        applicantCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getApplicantName()));
        applicantCol.setPrefWidth(200);

        TableColumn<Application, String> titleCol = new TableColumn<>("Applied For");
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJobTitle()));
        titleCol.setPrefWidth(250);

        TableColumn<Application, String> statusCol = new TableColumn<>("Update Status");
        statusCol.setCellFactory(param -> new TableCell<Application, String>() {
            private final ComboBox<String> statusBox = new ComboBox<>();
            {
                statusBox.getItems().addAll("PENDING", "REVIEWING", "INTERVIEW", "REJECTED", "HIRED");
                statusBox.setPrefWidth(140);
                statusBox.setOnAction(e -> {
                    Application app = getTableView().getItems().get(getIndex());
                    String newStatus = statusBox.getValue();
                    if (newStatus != null && !newStatus.equals(app.getStatus())) {
                        if (applicationController.updateApplicationStatus(app.getId(), newStatus)) {
                            app.setStatus(newStatus); // Update local model
                        }
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Application app = getTableView().getItems().get(getIndex());
                    statusBox.setValue(app.getStatus());
                    setGraphic(statusBox);
                }
            }
        });
        statusCol.setPrefWidth(160);

        TableColumn<Application, String> dateCol = new TableColumn<>("Applied On");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAppliedAt()));
        dateCol.setPrefWidth(200);

        table.getColumns().addAll(applicantCol, titleCol, statusCol, dateCol);

        List<Application> dbApps = applicationController.getApplicationsForEmployer(currentUser.getId());
        ObservableList<Application> data = FXCollections.observableArrayList(dbApps);
        table.setItems(data);

        cardBox.getChildren().add(table);
        view.getChildren().add(cardBox);
    }

    public VBox getView() {
        return view;
    }
}
