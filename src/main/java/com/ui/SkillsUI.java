package com.mesi.jobai.ui;

import com.mesi.jobai.controller.RecommendationController;
import com.mesi.jobai.model.Skill;
import com.mesi.jobai.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkillsUI {
    private JPanel view;
    private User currentUser;
    private RecommendationController recommendationController;
    private JTable table;
    private DefaultTableModel tableModel;

    // Hardcoded common tech keywords for the simple internal AI Parser
    private static final String[] AI_KEYWORDS = {
        "Java", "Python", "C++", "C#", "JavaScript", "TypeScript", "React", "Angular", "Vue", 
        "Node", "Spring", "SQL", "MySQL", "PostgreSQL", "MongoDB", "AWS", "Azure", "GCP", 
        "Docker", "Kubernetes", "Agile", "Scrum", "Management", "Leadership", "Git"
    };

    public SkillsUI(User currentUser) {
        this.currentUser = currentUser;
        this.recommendationController = new RecommendationController();
        view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        view.setBackground(SystemColors.BACKGROUND);
        view.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel sectionTitle = new JLabel("My Skills Profile");
        sectionTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        sectionTitle.setForeground(SystemColors.TEXT_PRIMARY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(SystemColors.SURFACE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        cardPanel.setMaximumSize(new Dimension(800, Integer.MAX_VALUE));
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subTitle = new JLabel("Add a new skill to match better jobs:");
        subTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        subTitle.setForeground(SystemColors.TEXT_SECONDARY);
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Form to add a new skill manually
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        addPanel.setBackground(SystemColors.SURFACE);
        addPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField skillNameField = new JTextField();
        skillNameField.setPreferredSize(new Dimension(250, 45));
        skillNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        skillNameField.setBackground(SystemColors.BACKGROUND);
        skillNameField.setForeground(SystemColors.TEXT_PRIMARY);
        
        JComboBox<String> proficiencyBox = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advanced", "Expert"});
        proficiencyBox.setPreferredSize(new Dimension(200, 45));
        proficiencyBox.setBackground(SystemColors.BACKGROUND);
        proficiencyBox.setForeground(SystemColors.TEXT_PRIMARY);

        JButton addBtn = new JButton("Add Skill");
        addBtn.setPreferredSize(new Dimension(100, 45));
        addBtn.setBackground(SystemColors.PRIMARY);
        addBtn.setForeground(Color.BLACK);
        addBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addBtn.setFocusPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel statusLabel = new JLabel();
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        addBtn.addActionListener(e -> {
            String name = skillNameField.getText();
            String prof = (String) proficiencyBox.getSelectedItem();
            
            if (name.isEmpty() || prof == null) {
                statusLabel.setText("Please enter a skill and proficiency.");
                statusLabel.setForeground(Color.RED);
                return;
            }
            
            Skill newSkill = new Skill(0, currentUser.getId(), name, prof);
            if (recommendationController.addUserSkill(newSkill)) {
                statusLabel.setText("Skill added.");
                statusLabel.setForeground(new Color(34, 139, 34));
                skillNameField.setText("");
                proficiencyBox.setSelectedIndex(-1);
                refreshTable();
            } else {
                statusLabel.setText("Failed to add skill.");
                statusLabel.setForeground(Color.RED);
            }
        });

        addPanel.add(skillNameField);
        addPanel.add(proficiencyBox);
        addPanel.add(addBtn);
        addPanel.add(statusLabel);

        // ==== AI Resume Parser Section ==== 
        JPanel aiPanel = new JPanel();
        aiPanel.setLayout(new BoxLayout(aiPanel, BoxLayout.Y_AXIS));
        aiPanel.setBackground(SystemColors.BACKGROUND);
        aiPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        aiPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel aiTitle = new JLabel("✨ AI Resume Auto-Parser");
        aiTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        aiTitle.setForeground(SystemColors.PRIMARY);
        aiTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea resumeArea = new JTextArea(4, 50);
        resumeArea.setWrapStyleWord(true);
        resumeArea.setLineWrap(true);
        resumeArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        resumeArea.setBackground(SystemColors.BACKGROUND);
        resumeArea.setForeground(SystemColors.TEXT_PRIMARY);
        JScrollPane resumeScrollPane = new JScrollPane(resumeArea);
        resumeScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        resumeScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton parseBtn = new JButton("Parse Skills");
        parseBtn.setPreferredSize(new Dimension(120, 40));
        parseBtn.setBackground(SystemColors.PRIMARY);
        parseBtn.setForeground(Color.BLACK);
        parseBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        parseBtn.setFocusPainted(false);
        parseBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel aiStatus = new JLabel();
        aiStatus.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        aiStatus.setForeground(new Color(34, 139, 34));

        parseBtn.addActionListener(e -> {
            String text = resumeArea.getText().toLowerCase();
            if (text.trim().isEmpty()) {
                aiStatus.setText("Please paste some text first.");
                aiStatus.setForeground(Color.RED);
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
                aiStatus.setForeground(new Color(34, 139, 34));
                resumeArea.setText("");
                refreshTable();
            } else {
                aiStatus.setText("No new keywords found.");
                aiStatus.setForeground(SystemColors.TEXT_SECONDARY);
            }
        });
        
        JPanel aiActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        aiActions.setBackground(SystemColors.BACKGROUND);
        aiActions.add(parseBtn);
        aiActions.add(aiStatus);
        aiActions.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel resumePlaceholder = new JLabel("Paste your resume text here...");
        resumePlaceholder.setForeground(SystemColors.TEXT_SECONDARY);
        resumePlaceholder.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 11));
        resumePlaceholder.setAlignmentX(Component.LEFT_ALIGNMENT);

        aiPanel.add(aiTitle);
        aiPanel.add(Box.createVerticalStrut(10));
        aiPanel.add(resumePlaceholder);
        aiPanel.add(Box.createVerticalStrut(3));
        aiPanel.add(resumeScrollPane);
        aiPanel.add(Box.createVerticalStrut(10));
        aiPanel.add(aiActions);
        // ==================================

        // Table to list skills
        String[] columnNames = {"Skill", "Proficiency", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only action column is editable
            }
        };

        table = new JTable(tableModel);
        table.setBackground(SystemColors.BACKGROUND);
        table.setForeground(SystemColors.TEXT_PRIMARY);
        table.setGridColor(SystemColors.BORDER);
        table.setSelectionBackground(SystemColors.PRIMARY);
        table.setSelectionForeground(Color.WHITE);
        table.setRowHeight(40);
        table.getTableHeader().setBackground(SystemColors.SURFACE);
        table.getTableHeader().setForeground(SystemColors.TEXT_PRIMARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, SystemColors.BORDER));

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(300);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);

        // Custom renderer and editor for action column
        table.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(750, 250));
        tableScrollPane.setBorder(BorderFactory.createLineBorder(SystemColors.BORDER, 1));
        tableScrollPane.getViewport().setBackground(SystemColors.BACKGROUND);
        tableScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        refreshTable();

        // Add placeholder labels
        JLabel skillPlaceholder = new JLabel("Skill (e.g. Java, React)");
        skillPlaceholder.setForeground(SystemColors.TEXT_SECONDARY);
        skillPlaceholder.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 11));
        skillPlaceholder.setAlignmentX(Component.LEFT_ALIGNMENT);

        cardPanel.add(subTitle);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(skillPlaceholder);
        cardPanel.add(Box.createVerticalStrut(3));
        cardPanel.add(addPanel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(aiPanel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(tableScrollPane);

        view.add(sectionTitle);
        view.add(Box.createVerticalStrut(25));
        view.add(cardPanel);
        view.add(Box.createVerticalGlue());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Skill> dbSkills = recommendationController.getUserSkills(currentUser.getId());
        for (Skill skill : dbSkills) {
            Object[] rowData = {skill.getSkillName(), skill.getProficiency(), "Remove"};
            tableModel.addRow(rowData);
        }
    }

    // Custom button renderer for table
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Remove");
            setBackground(new Color(0, 0, 0, 0));
            setForeground(Color.RED);
            setBorder(BorderFactory.createEmptyBorder());
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            return this;
        }
    }

    // Custom button editor for table
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = "Remove";
            button.setText(label);
            button.setBackground(new Color(0, 0, 0, 0));
            button.setForeground(Color.RED);
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    List<Skill> dbSkills = recommendationController.getUserSkills(currentUser.getId());
                    if (row < dbSkills.size()) {
                        Skill skill = dbSkills.get(row);
                        if (recommendationController.removeUserSkill(skill.getId())) {
                            refreshTable();
                        }
                    }
                }
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    public JPanel getView() {
        return view;
    }
}
