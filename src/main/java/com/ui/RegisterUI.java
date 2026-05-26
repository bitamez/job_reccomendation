package com.mesi.jobai.ui;

import com.mesi.jobai.controller.AuthController;
import com.mesi.jobai.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterUI {
    private JPanel view;
    private JFrame parentFrame;
    private AuthController authController;

    public RegisterUI(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.authController = new AuthController();
        view = new JPanel(new BorderLayout());
        view.setBackground(SystemColors.BACKGROUND);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(SystemColors.SURFACE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(40, 40, 40, 40),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel title = new JLabel("Join AI Job Network");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        title.setForeground(SystemColors.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Create a new account");
        subtitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        subtitle.setForeground(SystemColors.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(350, 45));
        nameField.setPreferredSize(new Dimension(350, 45));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        nameField.setBackground(SystemColors.BACKGROUND);
        nameField.setForeground(SystemColors.TEXT_PRIMARY);

        JTextField emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(350, 45));
        emailField.setPreferredSize(new Dimension(350, 45));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        emailField.setBackground(SystemColors.BACKGROUND);
        emailField.setForeground(SystemColors.TEXT_PRIMARY);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(350, 45));
        passwordField.setPreferredSize(new Dimension(350, 45));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        passwordField.setBackground(SystemColors.BACKGROUND);
        passwordField.setForeground(SystemColors.TEXT_PRIMARY);

        JComboBox<String> roleBox = new JComboBox<>(new String[]{"APPLICANT", "EMPLOYER"});
        roleBox.setSelectedItem("APPLICANT");
        roleBox.setMaximumSize(new Dimension(350, 45));
        roleBox.setPreferredSize(new Dimension(350, 45));
        roleBox.setBackground(SystemColors.BACKGROUND);
        roleBox.setForeground(SystemColors.TEXT_PRIMARY);

        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel successLabel = new JLabel();
        successLabel.setForeground(new Color(34, 139, 34));
        successLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton registerBtn = new JButton("Register Account");
        registerBtn.setMaximumSize(new Dimension(350, 45));
        registerBtn.setPreferredSize(new Dimension(350, 45));
        registerBtn.setBackground(SystemColors.PRIMARY);
        registerBtn.setForeground(Color.BLACK);
        registerBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton loginLink = new JButton("Already have an account? Sign in");
        loginLink.setBackground(new Color(0, 0, 0, 0));
        loginLink.setForeground(SystemColors.PRIMARY);
        loginLink.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loginLink.setFocusPainted(false);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerBtn.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleBox.getSelectedItem();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill all fields.");
                successLabel.setText("");
                return;
            }

            boolean success = authController.register(name, email, password, role);

            if (success) {
                successLabel.setText("Registration successful! You can now sign in.");
                errorLabel.setText("");
                nameField.setText("");
                emailField.setText("");
                passwordField.setText("");
            } else {
                errorLabel.setText("Registration failed. Email might already exist.");
                successLabel.setText("");
            }
        });

        loginLink.addActionListener(e -> {
            parentFrame.dispose();
            SwingUtilities.invokeLater(() -> {
                new UnifiedLoginUI().setVisible(true);
            });
        });

        // Add placeholder labels
        JLabel namePlaceholder = new JLabel("Full Name");
        namePlaceholder.setForeground(SystemColors.TEXT_SECONDARY);
        namePlaceholder.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        namePlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailPlaceholder = new JLabel("Email Address");
        emailPlaceholder.setForeground(SystemColors.TEXT_SECONDARY);
        emailPlaceholder.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        emailPlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passwordPlaceholder = new JLabel("Password");
        passwordPlaceholder.setForeground(SystemColors.TEXT_SECONDARY);
        passwordPlaceholder.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        passwordPlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(subtitle);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(namePlaceholder);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(emailPlaceholder);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passwordPlaceholder);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(roleBox);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(errorLabel);
        formPanel.add(successLabel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(registerBtn);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(loginLink);
        formPanel.add(Box.createVerticalStrut(20));

        // Center the form panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(SystemColors.BACKGROUND);
        centerPanel.add(formPanel);

        view.add(centerPanel, BorderLayout.CENTER);
    }

    public JPanel getView() {
        return view;
    }
}
