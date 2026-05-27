package com.mesi.jobai.ui;

import com.mesi.jobai.dao.AdminDAO;
import com.mesi.jobai.model.Admin;
import com.mesi.jobai.model.Skill;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManageSkillsUI extends JFrame {
    private Admin currentAdmin;
    private AdminDAO adminDAO;
    private JTable skillsTable;
    private DefaultTableModel tableModel;
    private JTextField skillNameField;
    private JComboBox<String> categoryComboBox;

    public ManageSkillsUI(Admin admin) {
        this.currentAdmin = admin;
        this.adminDAO = new AdminDAO();
        initializeComponents();
        loadSkills();
    }

    private void initializeComponents() {
        setTitle("Manage Skills - Admin Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(AdminColors.BACKGROUND);

        // Header Panel
        add(AdminUIUtils.createHeaderPanel("Manage Skills"), BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = createTablePanel();

        // Form Panel
        JPanel formPanel = createFormPanel();

        // Button Panel
        JPanel buttonPanel = createButtonPanel();

        // Main Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(AdminColors.BACKGROUND);
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        contentPanel.add(formPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createTablePanel() {
        JPanel panel = AdminUIUtils.createStyledPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = AdminUIUtils.createHeaderLabel("Skills List");
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));

        String[] columnNames = {"ID", "Skill Name", "Category"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        skillsTable = AdminUIUtils.createStyledTable(tableModel);
        skillsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelection();
            }
        });

        JScrollPane scrollPane = new JScrollPane(skillsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(AdminColors.BORDER, 1));
        scrollPane.setPreferredSize(new Dimension(0, 250));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(AdminColors.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(AdminColors.BORDER, 1),
                "Add New Skill",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                AdminColors.TEXT_PRIMARY
            ),
            new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Skill Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(AdminUIUtils.createFormLabel("Skill Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        skillNameField = AdminUIUtils.createStyledTextField();
        panel.add(skillNameField, gbc);

        // Category
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(AdminUIUtils.createFormLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        categoryComboBox = new JComboBox<>(new String[]{
            "Programming Languages", "Frameworks", "Databases", "Tools", 
            "Soft Skills", "Certifications", "Other"
        });
        categoryComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        categoryComboBox.setBackground(AdminColors.SURFACE);
        categoryComboBox.setForeground(AdminColors.TEXT_PRIMARY);
        categoryComboBox.setEditable(true);
        panel.add(categoryComboBox, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setBackground(AdminColors.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 25, 20));

        JButton addButton = AdminUIUtils.createSuccessButton("Add Skill");
        JButton deleteButton = AdminUIUtils.createDangerButton("Delete Skill");
        JButton clearButton = AdminUIUtils.createSecondaryButton("Clear Form");
        JButton refreshButton = AdminUIUtils.createPrimaryButton("Refresh");
        JButton closeButton = AdminUIUtils.createSecondaryButton("Close");

        // Add action listeners
        addButton.addActionListener(new AddSkillActionListener());
        deleteButton.addActionListener(new DeleteSkillActionListener());
        clearButton.addActionListener(e -> clearForm());
        refreshButton.addActionListener(e -> loadSkills());
        closeButton.addActionListener(e -> dispose());

        // Add hover effects
        AdminUIUtils.addHoverEffect(addButton, AdminColors.SUCCESS, new Color(25, 135, 84));
        AdminUIUtils.addHoverEffect(deleteButton, AdminColors.DANGER, new Color(220, 53, 69));
        AdminUIUtils.addHoverEffect(refreshButton, AdminColors.PRIMARY, AdminColors.PRIMARY_LIGHT);
        AdminUIUtils.addHoverEffect(clearButton, AdminColors.SURFACE, AdminColors.HOVER);
        AdminUIUtils.addHoverEffect(closeButton, AdminColors.SURFACE, AdminColors.HOVER);

        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        panel.add(refreshButton);
        panel.add(closeButton);

        return panel;
    }

    private void loadSkills() {
        tableModel.setRowCount(0);
        List<Skill> skills = adminDAO.getAllSkills();
        for (Skill skill : skills) {
            Object[] row = {
                skill.getId(),
                skill.getName(),
                skill.getCategory()
            };
            tableModel.addRow(row);
        }
    }

    private void populateFormFromSelection() {
        int selectedRow = skillsTable.getSelectedRow();
        if (selectedRow >= 0) {
            skillNameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            categoryComboBox.setSelectedItem((String) tableModel.getValueAt(selectedRow, 2));
        }
    }

    private void clearForm() {
        skillNameField.setText("");
        categoryComboBox.setSelectedIndex(0);
    }

    private class AddSkillActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String skillName = skillNameField.getText().trim();
            String category = (String) categoryComboBox.getSelectedItem();

            if (skillName.isEmpty() || category == null || category.trim().isEmpty()) {
                JOptionPane.showMessageDialog(ManageSkillsUI.this,
                    "Please enter both skill name and category.",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (adminDAO.addSkill(skillName, category.trim())) {
                JOptionPane.showMessageDialog(ManageSkillsUI.this,
                    "Skill added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadSkills();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(ManageSkillsUI.this,
                    "Failed to add skill. It may already exist.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DeleteSkillActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = skillsTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(ManageSkillsUI.this,
                    "Please select a skill to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int skillId = (Integer) tableModel.getValueAt(selectedRow, 0);
            String skillName = (String) tableModel.getValueAt(selectedRow, 1);

            int option = JOptionPane.showConfirmDialog(ManageSkillsUI.this,
                "Are you sure you want to delete skill '" + skillName + "'?\n" +
                "This may affect existing user profiles and job requirements.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                if (adminDAO.deleteSkill(skillId)) {
                    JOptionPane.showMessageDialog(ManageSkillsUI.this,
                        "Skill deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadSkills();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(ManageSkillsUI.this,
                        "Failed to delete skill. It may be referenced by other records.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}