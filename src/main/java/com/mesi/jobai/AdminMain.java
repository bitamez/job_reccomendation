package com.mesi.jobai;

import com.mesi.jobai.ui.AdminLoginUI;
import com.mesi.jobai.util.DatabaseInitializer;

import javax.swing.*;

/**
 * Main class to launch the Admin Panel for Job AI System
 */
public class AdminMain {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }

        // Initialize database with sample data
        DatabaseInitializer.initializeDatabase();
        DatabaseInitializer.printAvailableUsers();

        // Launch admin login UI
        SwingUtilities.invokeLater(() -> {
            new AdminLoginUI().setVisible(true);
        });
    }
}