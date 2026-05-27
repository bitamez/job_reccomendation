package com.mesi.jobai.ui;

import com.mesi.jobai.dao.AdminDAO;
import com.mesi.jobai.model.Admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminLoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AdminDAO adminDAO;

    public AdminLoginUI() {
        adminDAO = new AdminDAO();
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Admin Login - Job AI System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // Set clean white background
        getContentPane().setBackground(AdminColors.BACKGROUND);

        // Header Panel - Simple and clean
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(AdminColors.PRIMARY_DARK);
        
        JLabel titleLabel = new JLabel("Admin Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        titleLabel.setForeground(AdminColors.TEXT_WHITE);
        headerPanel.add(titleLabel);

        // Form Panel - Clean white with system components
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(AdminColors.SURFACE);
        GridBagConstraints gbc = new GridBagConstraints();

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 20, 5, 20);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        usernameLabel.setForeground(AdminColors.TEXT_PRIMARY);
        formPanel.add(usernameLabel, gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 20, 15, 20);
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Dialog", Font.PLAIN, 12));
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 20, 5, 20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        passwordLabel.setForeground(AdminColors.TEXT_PRIMARY);
        formPanel.add(passwordLabel, gbc);

        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 20, 20, 20);
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 12));
        formPanel.add(passwordField, gbc);

        // Button Panel - System default buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(AdminColors.SURFACE);
        
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Dialog", Font.BOLD, 12));
        loginButton.addActionListener(new LoginActionListener());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        cancelButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);

        // Enter key listener
        getRootPane().setDefaultButton(loginButton);
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                showErrorMessage("Please enter both username and password.");
                return;
            }

            Admin admin = adminDAO.loginAdmin(username, password);
            if (admin != null) {
                showSuccessMessage("Login successful! Welcome " + admin.getFullName());
                
                // Open Admin Dashboard
                new AdminDashboardUI(admin).setVisible(true);
                dispose();
            } else {
                showErrorMessage("Invalid username or password.");
                passwordField.setText("");
            }
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", 
            JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new AdminLoginUI().setVisible(true);
        });
    }
}