package com.mesi.jobai.ui;

import com.mesi.jobai.controller.AuthController;
import com.mesi.jobai.model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterUI {
    private JPanel view;
    private JFrame parentFrame;
    private AuthController authController;

    public RegisterUI(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.authController = new AuthController();
        initializeUI();
    }

    private void initializeUI() {
        view = new JPanel(new BorderLayout());
        view.setBackground(new Color(240, 242, 245)); // Light gray background
        
        // Create form panel
        JPanel formPanel = createFormPanel();
        
        // Center the form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 242, 245));
        centerPanel.add(formPanel);
        
        view.add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel formBox = new JPanel();
        formBox.setLayout(new BoxLayout(formBox, BoxLayout.Y_AXIS));
        formBox.setBackground(Color.WHITE);
        formBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(30, 40, 30, 40)
        ));
        formBox.setPreferredSize(new Dimension(420, 520));

        // Title
        JLabel title = new JLabel("Join AI Job Network");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(30, 30, 30));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Create a new account");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create form fields panel with GridBagLayout
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Create form fields
        JTextField nameField = createStyledTextField();
        JTextField emailField = createStyledTextField();
        JPasswordField passwordField = createStyledPasswordField();
        
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"APPLICANT", "EMPLOYER"});
        roleBox.setSelectedItem("APPLICANT");
        roleBox.setPreferredSize(new Dimension(320, 38));
        roleBox.setBackground(new Color(250, 250, 250));
        roleBox.setForeground(new Color(30, 30, 30));
        roleBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleBox.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // Create labels
        JLabel nameLabel = createFieldLabel("Full Name");
        JLabel emailLabel = createFieldLabel("Email Address");
        JLabel passwordLabel = createFieldLabel("Password");
        JLabel roleLabel = createFieldLabel("Account Type");

        // Layout fields
        int row = 0;
        addFieldToPanel(fieldsPanel, nameLabel, nameField, gbc, row++);
        addFieldToPanel(fieldsPanel, emailLabel, emailField, gbc, row++);
        addFieldToPanel(fieldsPanel, passwordLabel, passwordField, gbc, row++);
        addFieldToPanel(fieldsPanel, roleLabel, roleBox, gbc, row++);

        // Error and success labels
        JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(new Color(220, 53, 69));
        errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel successLabel = new JLabel(" ");
        successLabel.setForeground(new Color(34, 139, 34));
        successLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Register button
        JButton registerBtn = new JButton("Register Account");
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerBtn.setBackground(new Color(52, 152, 219));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.setPreferredSize(new Dimension(320, 42));
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add hover effect
        registerBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerBtn.setBackground(new Color(41, 128, 185));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                registerBtn.setBackground(new Color(52, 152, 219));
            }
        });

        // Login link
        JButton loginLink = new JButton("Already have an account? Sign in");
        styleLoginLink(loginLink);
        loginLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add action listeners
        registerBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleBox.getSelectedItem();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill all fields.");
                successLabel.setText(" ");
                return;
            }

            boolean success = authController.register(name, email, password, role);

            if (success) {
                successLabel.setText("Registration successful! You can now sign in.");
                errorLabel.setText(" ");
                nameField.setText("");
                emailField.setText("");
                passwordField.setText("");
            } else {
                errorLabel.setText("Registration failed. Email might already exist.");
                successLabel.setText(" ");
            }
        });

        loginLink.addActionListener(e -> {
            parentFrame.dispose();
            SwingUtilities.invokeLater(() -> {
                new UnifiedLoginUI().setVisible(true);
            });
        });

        // Add components with compact spacing
        formBox.add(title);
        formBox.add(Box.createVerticalStrut(6));
        formBox.add(subtitle);
        formBox.add(Box.createVerticalStrut(25));
        formBox.add(fieldsPanel);
        formBox.add(Box.createVerticalStrut(8));
        formBox.add(errorLabel);
        formBox.add(successLabel);
        formBox.add(Box.createVerticalStrut(20));
        formBox.add(registerBtn);
        formBox.add(Box.createVerticalStrut(15));
        formBox.add(loginLink);

        return formBox;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setBackground(new Color(250, 250, 250));
        field.setForeground(new Color(30, 30, 30));
        field.setPreferredSize(new Dimension(320, 38));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setBackground(new Color(250, 250, 250));
        field.setForeground(new Color(30, 30, 30));
        field.setPreferredSize(new Dimension(320, 38));
        return field;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(80, 80, 80));
        return label;
    }

    private void addFieldToPanel(JPanel panel, JLabel label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row * 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.fill = GridBagConstraints.NONE;
        panel.add(label, gbc);
        
        gbc.gridy = row * 2 + 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private void styleLoginLink(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(new Color(52, 152, 219));
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

    public JPanel getView() {
        return view;
    }
}
