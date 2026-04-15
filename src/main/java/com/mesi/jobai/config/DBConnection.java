package com.mesi.jobai.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    // Mapping specifically to your job-reccomendation database!
    private static final String URL = "jdbc:postgresql://localhost:5432/job-reccomendation";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Messi@7962";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        System.out.println("Using custom schema from pgAdmin (job-reccomendation DB).");
        
        // We will dynamically add a role column to your users table just so the UI login flow doesn't break
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(50) DEFAULT 'APPLICANT'");
            System.out.println("Custom schema adaptations applied successulfully!");
        } catch (SQLException e) {
            System.err.println("Note: DB adaptations failed (safe to ignore if using strict script): " + e.getMessage());
        }
    }
}
