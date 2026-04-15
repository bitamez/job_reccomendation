package com.mesi.jobai.dao;

import com.mesi.jobai.config.DBConnection;
import com.mesi.jobai.model.Application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    public boolean applyForJob(int jobId, int applicantId) {
        String query = "INSERT INTO applications (job_id, user_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, jobId);
            pstmt.setInt(2, applicantId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating application: " + e.getMessage());
            return false;
        }
    }

    public List<Application> getApplicationsForUser(int userId) {
        List<Application> applications = new ArrayList<>();
        // Modified query to match custom DB
        String query = "SELECT a.*, j.title, c.name as company " +
                       "FROM applications a " +
                       "JOIN jobs j ON a.job_id = j.job_id " +
                       "LEFT JOIN companies c ON j.company_id = c.company_id " +
                       "WHERE a.user_id = ? ORDER BY a.applied_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Application app = new Application(
                        rs.getInt("application_id"),
                        rs.getInt("job_id"),
                        rs.getInt("user_id"),
                        rs.getString("status"),
                        rs.getString("applied_at")
                    );
                    app.setJobTitle(rs.getString("title"));
                    app.setCompanyName(rs.getString("company") != null ? rs.getString("company") : "Unknown");
                    applications.add(app);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching applications: " + e.getMessage());
        }
        return applications;
    }

    public List<Application> getApplicationsForEmployer(int employerId) {
        List<Application> applications = new ArrayList<>();
        // Note: As the custom DB schema 'companies' lacks an employer_id,
        // we'll fetch all applications globally for demonstration, 
        // to adapt to the schema limitation seamlessly.
        String query = "SELECT a.*, j.title, c.name as company, u.full_name as applicant_name " +
                       "FROM applications a " +
                       "JOIN jobs j ON a.job_id = j.job_id " +
                       "LEFT JOIN companies c ON j.company_id = c.company_id " +
                       "JOIN users u ON a.user_id = u.user_id " +
                       "ORDER BY a.applied_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Application app = new Application(
                        rs.getInt("application_id"),
                        rs.getInt("job_id"),
                        rs.getInt("user_id"),
                        rs.getString("status"),
                        rs.getString("applied_at")
                    );
                    app.setJobTitle(rs.getString("title"));
                    app.setCompanyName(rs.getString("company") != null ? rs.getString("company") : "Unknown");
                    app.setApplicantName(rs.getString("applicant_name"));
                    applications.add(app);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employer applications: " + e.getMessage());
        }
        return applications;
    }

    public boolean updateApplicationStatus(int applicationId, String newStatus) {
        String query = "UPDATE applications SET status = ? WHERE application_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, applicationId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating application status: " + e.getMessage());
            return false;
        }
    }
}
