package com.mesi.jobai.util;

import com.mesi.jobai.config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database initializer to ensure proper setup and test data
 */
public class DatabaseInitializer {
    
    public static void initializeDatabase() {
        System.out.println("Initializing database...");
        
        try (Connection conn = DBConnection.getConnection()) {
            // Add role column if it doesn't exist
            addRoleColumn(conn);
            
            // Create admin table and admin user if they don't exist
            createAdminTable(conn);
            createAdminUser(conn);
            
            // Create sample users if they don't exist
            createSampleUsers(conn);
            
            // Create sample skills if they don't exist
            createSampleSkills(conn);
            
            System.out.println("Database initialization completed successfully!");
            
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void addRoleColumn(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(50) DEFAULT 'APPLICANT'");
            System.out.println("Role column added/verified in users table");
        }
    }
    
    private static void createAdminTable(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String createAdminTable = """
                CREATE TABLE IF NOT EXISTS admins (
                    admin_id SERIAL PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    full_name VARCHAR(100) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
            stmt.executeUpdate(createAdminTable);
            System.out.println("Admin table created/verified");
        }
    }
    
    private static void createAdminUser(Connection conn) throws SQLException {
        // Check if admin user exists
        String checkQuery = "SELECT COUNT(*) FROM admins WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(checkQuery)) {
            pstmt.setString(1, "admin@jobai.com");
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    // Create admin user
                    String insertQuery = "INSERT INTO admins (username, password, email, full_name) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                        insertStmt.setString(1, "admin");
                        insertStmt.setString(2, "admin123");
                        insertStmt.setString(3, "admin@jobai.com");
                        insertStmt.setString(4, "System Administrator");
                        insertStmt.executeUpdate();
                        System.out.println("Admin user created successfully!");
                    }
                } else {
                    System.out.println("Admin user already exists");
                }
            }
        }
    }
    
    private static void createSampleUsers(Connection conn) throws SQLException {
        // Check if users exist
        String checkQuery = "SELECT COUNT(*) FROM users";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkQuery)) {
            
            if (rs.next() && rs.getInt(1) == 0) {
                // No users exist, create sample users
                String insertQuery = "INSERT INTO users (full_name, email, password, role) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    
                    // Sample applicant
                    pstmt.setString(1, "Jane Applicant");
                    pstmt.setString(2, "applicant@test.com");
                    pstmt.setString(3, "password123");
                    pstmt.setString(4, "APPLICANT");
                    pstmt.executeUpdate();
                    
                    // Sample employer
                    pstmt.setString(1, "John Employer");
                    pstmt.setString(2, "employer@test.com");
                    pstmt.setString(3, "password123");
                    pstmt.setString(4, "EMPLOYER");
                    pstmt.executeUpdate();
                    
                    // Another employer
                    pstmt.setString(1, "Bob Manager");
                    pstmt.setString(2, "manager@test.com");
                    pstmt.setString(3, "password123");
                    pstmt.setString(4, "EMPLOYER");
                    pstmt.executeUpdate();
                    
                    System.out.println("Sample users created successfully!");
                }
            } else {
                System.out.println("Users already exist in database");
            }
        }
    }
    
    private static void createSampleSkills(Connection conn) throws SQLException {
        // Check if skills exist
        String checkQuery = "SELECT COUNT(*) FROM skills";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkQuery)) {
            
            if (rs.next() && rs.getInt(1) == 0) {
                // No skills exist, create sample skills
                String insertQuery = "INSERT INTO skills (skill_name) VALUES (?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    
                    String[] skills = {"Java", "Python", "JavaScript", "React", "Node.js", 
                                     "PostgreSQL", "MySQL", "Git", "Docker", "AWS"};
                    
                    for (String skill : skills) {
                        pstmt.setString(1, skill);
                        pstmt.executeUpdate();
                    }
                    
                    System.out.println("Sample skills created successfully!");
                }
            } else {
                System.out.println("Skills already exist in database");
            }
        }
    }
    
    public static void printAvailableUsers() {
        System.out.println("\n=== Available Test Users ===");
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT email, role FROM users ORDER BY role, email")) {
            
            while (rs.next()) {
                System.out.println("Email: " + rs.getString("email") + " | Role: " + rs.getString("role") + " | Password: password123");
            }
            System.out.println("=============================\n");
            
        } catch (SQLException e) {
            System.err.println("Error retrieving users: " + e.getMessage());
        }
    }
}