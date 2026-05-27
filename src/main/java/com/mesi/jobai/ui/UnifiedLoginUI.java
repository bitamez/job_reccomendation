package com.mesi.jobai.ui;

import com.mesi.jobai.controller.UnifiedAuthController;
import com.mesi.jobai.controller.UnifiedAuthController.LoginResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UnifiedLoginUI extends JFrame {
    private JPanel view;
    private UnifiedAuthController authController;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    public UnifiedLoginUI() {
        this.authController = new UnifiedAuthController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("AI Job Recommendation System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        
        // Modern gradient background
        view = new JPanel(new BorderLayout());
        view.setBackground(new Color(240, 242, 245)); // Light gray background
        
        // Create form panel
        JPanel formPanel = createFormPanel();
        
        // Center the form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 242, 245));
        centerPanel.add(formPanel);
        
        view.add(centerPanel, BorderLayout.CENTER);
        add(view);
    }

    private JPanel createFormPanel() {
        JPanel formBox = new JPanel();
        formBox.setLayout(new BoxLayout(formBox, BoxLayout.Y_AXIS));
        formBox.setBackground(Color.WHITE);
        formBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(30, 40, 30, 40)
        ));
        formBox.setPreferredSize(new Dimension(420, 450));

        // Title with modern color
        JLabel title = new JLabel("AI Job Recommendation");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(30, 30, 30));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle with accent color
        JLabel subtitle = new JLabel("Sign in to your account");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create form fields panel with GridBagLayout for better control
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Email label and field
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emailLabel.setForeground(new Color(80, 80, 80));
        
        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        emailField.setBackground(new Color(250, 250, 250));
        emailField.setForeground(new Color(30, 30, 30));
        emailField.setPreferredSize(new Dimension(320, 38));
        
        // Password label and field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordLabel.setForeground(new Color(80, 80, 80));
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        passwordField.setBackground(new Color(250, 250, 250));
        passwordField.setForeground(new Color(30, 30, 30));
        passwordField.setPreferredSize(new Dimension(320, 38));
        
        // Layout fields
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        fieldsPanel.add(emailLabel, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(emailField, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(passwordLabel, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(passwordField, gbc);
        
        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        errorLabel.setForeground(new Color(220, 53, 69));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Login button with modern styling
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setBackground(new Color(52, 152, 219));
        loginBtn.setForeground(Color.BLACK);
        loginBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setPreferredSize(new Dimension(320, 42));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add hover effect
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginBtn.setBackground(new Color(41, 128, 185));
                loginBtn.setForeground(Color.BLACK);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                loginBtn.setBackground(new Color(52, 152, 219));
                loginBtn.setForeground(Color.BLACK);
            }
        });
        
        // Register link
        JButton registerLink = new JButton("Don't have an account? Register");
        styleRegisterLink(registerLink);
        registerLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add action listeners
        loginBtn.addActionListener(new LoginActionListener());
        registerLink.addActionListener(e -> openRegisterUI());
        
        // Add Enter key support
        getRootPane().setDefaultButton(loginBtn);
        
        // Add components with compact spacing
        formBox.add(title);
        formBox.add(Box.createVerticalStrut(6));
        formBox.add(subtitle);
        formBox.add(Box.createVerticalStrut(25));
        formBox.add(fieldsPanel);
        formBox.add(Box.createVerticalStrut(8));
        formBox.add(errorLabel);
        formBox.add(Box.createVerticalStrut(20));
        formBox.add(loginBtn);
        formBox.add(Box.createVerticalStrut(15));
        formBox.add(registerLink);
        
        return formBox;
    }

    private void styleRegisterLink(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(new Color(52, 152, 219)); // Modern blue
        button.setBackground(new Color(0, 0, 0, 0));
        button.setBorder(new EmptyBorder(8, 8, 8, 8));
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(41, 128, 185));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(52, 152, 219));
            }
        });
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill all fields.");
                return;
            }
            
            LoginResult result = authController.login(email, password);
            
            if (result.isSuccess()) {
                errorLabel.setText("");
                
                switch (result.getUserType()) {
                    case "ADMIN":
                        // Launch Swing Admin Dashboard
                        dispose();
                        new AdminDashboardUI(result.getAdmin()).setVisible(true);
                        break;
                        
                    case "EMPLOYER":
                    case "APPLICANT":
                        // Launch Swing User Dashboard
                        dispose();
                        JFrame dashboardFrame = new JFrame("Dashboard - AI Job Recommendation System");
                        DashboardUI dashboardUI = new DashboardUI(result.getUser(), dashboardFrame);
                        dashboardFrame.setContentPane(dashboardUI.getView());
                        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        dashboardFrame.setSize(1200, 800);
                        dashboardFrame.setLocationRelativeTo(null);
                        dashboardFrame.setVisible(true);
                        break;
                }
            } else {
                errorLabel.setText(result.getMessage());
            }
        }
    }

    private void openRegisterUI() {
        dispose();
        JFrame registerFrame = new JFrame("Register - AI Job Recommendation System");
        RegisterUI registerUI = new RegisterUI(registerFrame);
        registerFrame.setContentPane(registerUI.getView());
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerFrame.setSize(500, 650);
        registerFrame.setLocationRelativeTo(null);
        registerFrame.setVisible(true);
    }

    public JPanel getView() {
        return view;
    }
}