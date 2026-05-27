package com.mesi.jobai.ui;

import com.mesi.jobai.controller.AuthController;
import com.mesi.jobai.model.User;
import javax.swing.*;
import java.awt.*;

public class UserProfileUI {
    private JPanel view;
    private User currentUser;
    private AuthController authController;

    public UserProfileUI(User currentUser) {
        this.currentUser = currentUser;
        this.authController = new AuthController();
        view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        view.setBackground(SystemColors.BACKGROUND);
        view.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel sectionTitle = new JLabel("My Profile");
        sectionTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        sectionTitle.setForeground(SystemColors.TEXT_PRIMARY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(SystemColors.SURFACE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        formCard.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleLabel = new JLabel("Account Type: " + currentUser.getRole());
        roleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        roleLabel.setForeground(SystemColors.PRIMARY);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblName = new JLabel("Full Name");
        lblName.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        lblName.setForeground(SystemColors.TEXT_SECONDARY);
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField nameField = new JTextField(currentUser.getName());
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        nameField.setPreferredSize(new Dimension(450, 45));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        nameField.setBackground(SystemColors.BACKGROUND);
        nameField.setForeground(SystemColors.TEXT_PRIMARY);
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblEmail = new JLabel("Email Address");
        lblEmail.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        lblEmail.setForeground(SystemColors.TEXT_SECONDARY);
        lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField emailField = new JTextField(currentUser.getEmail());
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        emailField.setPreferredSize(new Dimension(450, 45));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        emailField.setBackground(SystemColors.BACKGROUND);
        emailField.setForeground(SystemColors.TEXT_PRIMARY);
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel statusLabel = new JLabel();
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton updateBtn = new JButton("Save Changes");
        updateBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        updateBtn.setPreferredSize(new Dimension(450, 45));
        updateBtn.setBackground(SystemColors.PRIMARY);
        updateBtn.setForeground(Color.BLACK);
        updateBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        updateBtn.setFocusPainted(false);
        updateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        updateBtn.addActionListener(e -> {
            String newName = nameField.getText();
            String newEmail = emailField.getText();
            
            if (newName.isEmpty() || newEmail.isEmpty()) {
                statusLabel.setText("Fields cannot be empty.");
                statusLabel.setForeground(Color.RED);
                return;
            }
            
            currentUser.setName(newName);
            currentUser.setEmail(newEmail);
            
            if (authController.updateProfile(currentUser)) {
                statusLabel.setText("Profile updated successfully!");
                statusLabel.setForeground(new Color(34, 139, 34));
            } else {
                statusLabel.setText("Failed to update profile. Email might be in use.");
                statusLabel.setForeground(Color.RED);
            }
        });

        formCard.add(roleLabel);
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(lblName);
        formCard.add(Box.createVerticalStrut(5));
        formCard.add(nameField);
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(lblEmail);
        formCard.add(Box.createVerticalStrut(5));
        formCard.add(emailField);
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(statusLabel);
        formCard.add(Box.createVerticalStrut(15));
        formCard.add(updateBtn);

        view.add(sectionTitle);
        view.add(Box.createVerticalStrut(25));
        view.add(formCard);
        view.add(Box.createVerticalGlue());
    }

    public JPanel getView() {
        return view;
    }
}
