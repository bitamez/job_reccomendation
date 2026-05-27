package com.mesi.jobai.ui;

import com.mesi.jobai.controller.AuthController;
import com.mesi.jobai.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI {
    private JPanel view;
    private JFrame parentFrame;
    private AuthController authController;

    public LoginUI(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.authController = new AuthController();
        view = new JPanel(new BorderLayout());
        view.setBackground(SystemColors.BACKGROUND);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(SystemColors.SURFACE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(50, 40, 50, 40),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel title = new JLabel("AI Job Recommendation");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        title.setForeground(SystemColors.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sign in to your account");
        subtitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        subtitle.setForeground(SystemColors.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginBtn = new JButton("Login");
        loginBtn.setMaximumSize(new Dimension(350, 45));
        loginBtn.setPreferredSize(new Dimension(350, 45));
        loginBtn.setBackground(SystemColors.PRIMARY);
        loginBtn.setForeground(Color.BLACK);
        loginBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton registerLink = new JButton("Don't have an account? Register");
        registerLink.setBackground(new Color(0, 0, 0, 0));
        registerLink.setForeground(SystemColors.PRIMARY);
        registerLink.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        registerLink.setFocusPainted(false);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            
            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill all fields.");
                return;
            }
            
            User user = authController.login(email, password);
            if (user != null) {
                parentFrame.dispose();
                SwingUtilities.invokeLater(() -> {
                    JFrame dashboardFrame = new JFrame("Dashboard - AI Job Recommendation System");
                    DashboardUI dashboard = new DashboardUI(user, dashboardFrame);
                    dashboardFrame.setContentPane(dashboard.getView());
                    dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    dashboardFrame.setSize(1050, 700);
                    dashboardFrame.setLocationRelativeTo(null);
                    dashboardFrame.setVisible(true);
                });
            } else {
                errorLabel.setText("Invalid email or password.");
            }
        });

        registerLink.addActionListener(e -> {
            parentFrame.dispose();
            SwingUtilities.invokeLater(() -> {
                JFrame registerFrame = new JFrame("Register - AI Job Recommendation System");
                RegisterUI registerUI = new RegisterUI(registerFrame);
                registerFrame.setContentPane(registerUI.getView());
                registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                registerFrame.setSize(1050, 700);
                registerFrame.setLocationRelativeTo(null);
                registerFrame.setVisible(true);
            });
        });

        // Add placeholder labels
        JLabel emailPlaceholder = new JLabel("Email Address");
        emailPlaceholder.setForeground(SystemColors.TEXT_SECONDARY);
        emailPlaceholder.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        emailPlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passwordPlaceholder = new JLabel("Password");
        passwordPlaceholder.setForeground(SystemColors.TEXT_SECONDARY);
        passwordPlaceholder.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        passwordPlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(subtitle);
        formPanel.add(Box.createVerticalStrut(40));
        formPanel.add(emailPlaceholder);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(passwordPlaceholder);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(errorLabel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(loginBtn);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(registerLink);
        formPanel.add(Box.createVerticalStrut(30));

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
