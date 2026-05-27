package com.mesi.jobai.ui;

import com.mesi.jobai.dao.AdminDAO;
import com.mesi.jobai.model.Admin;
import com.mesi.jobai.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManageUsersUI extends JFrame {
    private Admin currentAdmin;
    private AdminDAO adminDAO;
    private JTable usersTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, emailField;
    private JComboBox<String> roleComboBox;

    public ManageUsersUI(Admin admin) {
        this.currentAdmin = admin;
        this.adminDAO = new AdminDAO();
        initializeComponents();
        loadUsers();
    }

    private void initializeComponents() {
        setTitle("Manage Users - Admin Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(AdminColors.BACKGROUND);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(AdminColors.PRIMARY_DARK);
        JLabel titleLabel = new JLabel("Manage Users", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(AdminColors.TEXT_WHITE);
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        headerPanel.add(titleLabel);

        // Table Panel
        JPanel tablePanel = createTablePanel();

        // Form Panel
        JPanel formPanel = createFormPanel();

        // Button Panel
        JPanel buttonPanel = createButtonPanel();

        // Main Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        contentPanel.add(formPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AdminColors.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AdminColors.BORDER, 1),
            new EmptyBorder(20, 20, 10, 20)
        ));

        JLabel titleLabel = new JLabel("Users List");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));

        String[] columnNames = {"ID", "Name", "Email", "Role"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        usersTable = new JTable(tableModel);
        usersTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usersTable.setRowHeight(35);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        usersTable.setBackground(AdminColors.SURFACE);
        usersTable.setForeground(new Color(33, 37, 41)); // Very dark text
        usersTable.setSelectionBackground(AdminColors.SELECTED);
        usersTable.setSelectionForeground(new Color(33, 37, 41)); // Dark text on selection
        usersTable.setGridColor(AdminColors.BORDER);
        usersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        usersTable.getTableHeader().setBackground(AdminColors.BACKGROUND);
        usersTable.getTableHeader().setForeground(new Color(33, 37, 41)); // Very dark text
        usersTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AdminColors.BORDER));
        
        usersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelection();
            }
        });

        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(AdminColors.BORDER, 1));
        scrollPane.setPreferredSize(new Dimension(0, 300));

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
                "User Details",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(33, 37, 41)
            ),
            new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(new Color(33, 37, 41));
        panel.add(nameLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        nameField = new JTextField(20);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setForeground(new Color(33, 37, 41));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AdminColors.BORDER, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        panel.add(nameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        emailLabel.setForeground(new Color(33, 37, 41));
        panel.add(emailLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        emailField = new JTextField(20);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setForeground(new Color(33, 37, 41));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AdminColors.BORDER, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        panel.add(emailField, gbc);

        // Role
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roleLabel.setForeground(new Color(33, 37, 41));
        panel.add(roleLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        roleComboBox = new JComboBox<>(new String[]{"APPLICANT", "EMPLOYER"});
        roleComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleComboBox.setBackground(AdminColors.SURFACE);
        roleComboBox.setForeground(new Color(33, 37, 41));
        panel.add(roleComboBox, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setBackground(AdminColors.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 25, 20));

        JButton updateButton = new JButton("Update User");
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        updateButton.setBackground(AdminColors.PRIMARY);
        updateButton.setForeground(Color.BLACK);  // Black text
        updateButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        updateButton.setFocusPainted(false);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.addActionListener(new UpdateUserActionListener());

        JButton deleteButton = new JButton("Delete User");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteButton.setBackground(AdminColors.DANGER);
        deleteButton.setForeground(Color.BLACK);  // Black text
        deleteButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(new DeleteUserActionListener());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshButton.setBackground(AdminColors.SUCCESS);
        refreshButton.setForeground(Color.BLACK);  // Black text
        refreshButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> loadUsers());

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        closeButton.setBackground(AdminColors.SURFACE);
        closeButton.setForeground(Color.BLACK);  // Black text
        closeButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AdminColors.BORDER, 1),
            new EmptyBorder(10, 20, 10, 20)
        ));
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());

        // Add hover effects
        addButtonHoverEffect(updateButton, AdminColors.PRIMARY, AdminColors.PRIMARY_LIGHT);
        addButtonHoverEffect(deleteButton, AdminColors.DANGER, new Color(220, 53, 69));
        addButtonHoverEffect(refreshButton, AdminColors.SUCCESS, new Color(25, 135, 84));
        addButtonHoverEffect(closeButton, AdminColors.SURFACE, AdminColors.HOVER);

        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(refreshButton);
        panel.add(closeButton);

        return panel;
    }

    private void addButtonHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
            }
        });
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = adminDAO.getAllUsers();
        for (User user : users) {
            Object[] row = {
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
            };
            tableModel.addRow(row);
        }
    }

    private void populateFormFromSelection() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow >= 0) {
            nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            emailField.setText((String) tableModel.getValueAt(selectedRow, 2));
            roleComboBox.setSelectedItem((String) tableModel.getValueAt(selectedRow, 3));
        }
    }

    private void clearForm() {
        nameField.setText("");
        emailField.setText("");
        roleComboBox.setSelectedIndex(0);
    }

    private class UpdateUserActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = usersTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(ManageUsersUI.this,
                    "Please select a user to update.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String role = (String) roleComboBox.getSelectedItem();

            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(ManageUsersUI.this,
                    "Please fill in all fields.",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
            User user = new User(userId, name, email, "", role);

            if (adminDAO.updateUser(user)) {
                JOptionPane.showMessageDialog(ManageUsersUI.this,
                    "User updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUsers();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(ManageUsersUI.this,
                    "Failed to update user.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DeleteUserActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = usersTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(ManageUsersUI.this,
                    "Please select a user to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
            String userName = (String) tableModel.getValueAt(selectedRow, 1);

            int option = JOptionPane.showConfirmDialog(ManageUsersUI.this,
                "Are you sure you want to delete user '" + userName + "'?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                if (adminDAO.deleteUser(userId)) {
                    JOptionPane.showMessageDialog(ManageUsersUI.this,
                        "User deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUsers();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(ManageUsersUI.this,
                        "Failed to delete user.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}