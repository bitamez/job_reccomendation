package com.mesi.jobai.ui;

import com.mesi.jobai.dao.SkillDAO;
import com.mesi.jobai.model.Skill;
import com.mesi.jobai.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class SkillsUI {
    private VBox view;
    private User currentUser;
    private SkillDAO skillDAO;
    private TableView<Skill> table;

    public SkillsUI(User currentUser) {
        this.currentUser = currentUser;
        this.skillDAO = new SkillDAO();
        view = new VBox(20);
        view.getStyleClass().add("content-area");

        Label sectionTitle = new Label("My Skills");
        sectionTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Form to add a new skill
        HBox addBox = new HBox(10);
        TextField skillNameField = new TextField();
        skillNameField.setPromptText("Skill (e.g. Java, Python)");
        
        ComboBox<String> proficiencyBox = new ComboBox<>();
        proficiencyBox.getItems().addAll("Beginner", "Intermediate", "Advanced", "Expert");
        proficiencyBox.setPromptText("Proficiency");

        Button addBtn = new Button("Add Skill");
        addBtn.getStyleClass().add("btn-accent");
        
        Label statusLabel = new Label();

        addBtn.setOnAction(e -> {
            String name = skillNameField.getText();
            String prof = proficiencyBox.getValue();
            
            if (name.isEmpty() || prof == null) {
                statusLabel.setText("Please enter a skill and proficiency.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            Skill newSkill = new Skill(0, currentUser.getId(), name, prof);
            if (skillDAO.addSkill(newSkill)) {
                statusLabel.setText("Skill added.");
                statusLabel.setStyle("-fx-text-fill: green;");
                skillNameField.clear();
                proficiencyBox.setValue(null);
                refreshTable();
            } else {
                statusLabel.setText("Failed to add skill.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        addBox.getChildren().addAll(skillNameField, proficiencyBox, addBtn, statusLabel);

        // Table to list skills
        table = new TableView<>();
        table.getStyleClass().add("table-view");

        TableColumn<Skill, String> nameCol = new TableColumn<>("Skill");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSkillName()));
        nameCol.setPrefWidth(200);

        TableColumn<Skill, String> profCol = new TableColumn<>("Proficiency");
        profCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProficiency()));
        profCol.setPrefWidth(150);

        // Optional delete column
        TableColumn<Skill, String> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(param -> new TableCell<Skill, String>() {
            private final Button deleteBtn = new Button("Delete");
            {
                deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-cursor: hand;");
                deleteBtn.setOnAction(e -> {
                    Skill skill = getTableView().getItems().get(getIndex());
                    if (skillDAO.removeSkill(skill.getId())) {
                        refreshTable();
                    }
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteBtn);
                }
            }
        });

        table.getColumns().addAll(nameCol, profCol, actionCol);
        refreshTable();

        view.getChildren().addAll(sectionTitle, addBox, table);
    }

    private void refreshTable() {
        List<Skill> dbSkills = skillDAO.getSkillsForUser(currentUser.getId());
        ObservableList<Skill> data = FXCollections.observableArrayList(dbSkills);
        table.setItems(data);
    }

    public VBox getView() {
        return view;
    }
}
