package com.mesi.jobai.ui;

import com.mesi.jobai.controller.RecommendationController;
import com.mesi.jobai.model.Skill;
import com.mesi.jobai.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkillsUI {
    private VBox view;
    private User currentUser;
    private RecommendationController recommendationController;
    private TableView<Skill> table;

    // Hardcoded common tech keywords for the simple internal AI Parser
    private static final String[] AI_KEYWORDS = {
        "Java", "Python", "C++", "C#", "JavaScript", "TypeScript", "React", "Angular", "Vue", 
        "Node", "Spring", "SQL", "MySQL", "PostgreSQL", "MongoDB", "AWS", "Azure", "GCP", 
        "Docker", "Kubernetes", "Agile", "Scrum", "Management", "Leadership", "Git"
    };

    public SkillsUI(User currentUser) {
        this.currentUser = currentUser;
        this.recommendationController = new RecommendationController();
        view = new VBox(25);
        view.getStyleClass().add("content-area");

        Label sectionTitle = new Label("My Skills Profile");
        sectionTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: 800; -fx-text-fill: -text-light;");

        VBox cardBox = new VBox(20);
        cardBox.getStyleClass().add("card");
        cardBox.setPadding(new Insets(25));
        cardBox.setMaxWidth(800);

        Label subTitle = new Label("Add a new skill to match better jobs:");
        subTitle.setStyle("-fx-font-size: 14px; -fx-text-fill: -text-muted; -fx-font-weight: bold;");

        // Form to add a new skill manually
        HBox addBox = new HBox(15);
        TextField skillNameField = new TextField();
        skillNameField.setPromptText("Skill (e.g. Java, React)");
        skillNameField.setPrefWidth(250);
        skillNameField.setPrefHeight(45);
        
        ComboBox<String> proficiencyBox = new ComboBox<>();
        proficiencyBox.getItems().addAll("Beginner", "Intermediate", "Advanced", "Expert");
        proficiencyBox.setPromptText("Proficiency Level");
        proficiencyBox.setPrefWidth(200);
        proficiencyBox.setPrefHeight(45);

        Button addBtn = new Button("Add Skill");
        addBtn.getStyleClass().add("btn-accent");
        addBtn.setPrefHeight(45);
        
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-weight: bold;");

        addBtn.setOnAction(e -> {
            String name = skillNameField.getText();
            String prof = proficiencyBox.getValue();
            
            if (name.isEmpty() || prof == null) {
                statusLabel.setText("Please enter a skill and proficiency.");
                statusLabel.setStyle("-fx-text-fill: -error-color;");
                return;
            }
            
            Skill newSkill = new Skill(0, currentUser.getId(), name, prof);
            if (recommendationController.addUserSkill(newSkill)) {
                statusLabel.setText("Skill added.");
                statusLabel.setStyle("-fx-text-fill: -success-color;");
                skillNameField.clear();
                proficiencyBox.setValue(null);
                refreshTable();
            } else {
                statusLabel.setText("Failed to add skill.");
                statusLabel.setStyle("-fx-text-fill: -error-color;");
            }
        });

        addBox.getChildren().addAll(skillNameField, proficiencyBox, addBtn, statusLabel);

        // ==== AI Resume Parser Section ==== 
        VBox aiBox = new VBox(10);
        aiBox.setStyle("-fx-border-color: #2a2e3f; -fx-border-radius: 8; -fx-padding: 15; -fx-background-color: #1a1b26;");
        Label aiTitle = new Label("✨ AI Resume Auto-Parser");
        aiTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: -accent-color;");
        
        TextArea resumeArea = new TextArea();
        resumeArea.setPromptText("Paste your resume text here...");
        resumeArea.setPrefHeight(80);
        resumeArea.setWrapText(true);
        
        Button parseBtn = new Button("Parse Skills");
        parseBtn.getStyleClass().add("btn-primary");
        parseBtn.setPrefHeight(40);
        
        Label aiStatus = new Label();
        aiStatus.setStyle("-fx-font-weight: bold; -fx-text-fill: -success-color;");

        parseBtn.setOnAction(e -> {
            String text = resumeArea.getText().toLowerCase();
            if (text.trim().isEmpty()) {
                aiStatus.setText("Please paste some text first.");
                aiStatus.setStyle("-fx-text-fill: -error-color;");
                return;
            }
            
            int addedCount = 0;
            // Existing skills to avoid duplicates
            List<Skill> existingSkills = recommendationController.getUserSkills(currentUser.getId());
            
            for (String keyword : AI_KEYWORDS) {
                // Check if keyword is in the resume via regex boundaries
                Pattern pattern = Pattern.compile("\\b" + Pattern.quote(keyword.toLowerCase()) + "\\b");
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    boolean alreadyExists = existingSkills.stream()
                        .anyMatch(s -> s.getSkillName().equalsIgnoreCase(keyword));
                    
                    if (!alreadyExists) {
                        Skill parsedSkill = new Skill(0, currentUser.getId(), keyword, "Intermediate");
                        if (recommendationController.addUserSkill(parsedSkill)) {
                            addedCount++;
                        }
                    }
                }
            }
            
            if (addedCount > 0) {
                aiStatus.setText("AI Parser added " + addedCount + " new skills!");
                aiStatus.setStyle("-fx-text-fill: -success-color;");
                resumeArea.clear();
                refreshTable();
            } else {
                aiStatus.setText("No new keywords found.");
                aiStatus.setStyle("-fx-text-fill: -text-muted;");
            }
        });
        
        HBox aiActions = new HBox(15, parseBtn, aiStatus);
        aiActions.setStyle("-fx-alignment: center-left;");
        aiBox.getChildren().addAll(aiTitle, resumeArea, aiActions);
        // ==================================

        // Table to list skills
        table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setPrefHeight(250);

        TableColumn<Skill, String> nameCol = new TableColumn<>("Skill");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSkillName()));
        nameCol.setPrefWidth(300);

        TableColumn<Skill, String> profCol = new TableColumn<>("Proficiency");
        profCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProficiency()));
        profCol.setPrefWidth(200);

        // Optional delete column
        TableColumn<Skill, String> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(120);
        actionCol.setStyle("-fx-alignment: CENTER;");
        actionCol.setCellFactory(param -> new TableCell<Skill, String>() {
            private final Button deleteBtn = new Button("Remove");
            {
                deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: -error-color; -fx-cursor: hand; -fx-font-weight: bold;");
                deleteBtn.setOnAction(e -> {
                    Skill skill = getTableView().getItems().get(getIndex());
                    if (recommendationController.removeUserSkill(skill.getId())) {
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

        cardBox.getChildren().addAll(subTitle, addBox, aiBox, table);
        view.getChildren().addAll(sectionTitle, cardBox);
    }

    private void refreshTable() {
        List<Skill> dbSkills = recommendationController.getUserSkills(currentUser.getId());
        ObservableList<Skill> data = FXCollections.observableArrayList(dbSkills);
        table.setItems(data);
    }

    public VBox getView() {
        return view;
    }
}
