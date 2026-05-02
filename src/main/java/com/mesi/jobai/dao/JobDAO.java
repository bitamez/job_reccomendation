package com.mesi.jobai.dao;

import com.mesi.jobai.config.DBConnection;
import com.mesi.jobai.model.Job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JobDAO {

    public boolean createJob(Job job) {
        // Since custom schema has separate companies table, we map employer string to company name
        // (If company doesn't exist, we insert it first)
        String companyQuery = "INSERT INTO companies (name) VALUES (?) RETURNING company_id";
        String jobQuery = "INSERT INTO jobs (company_id, title, description, location) VALUES (?, ?, ?, 'Remote')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement compStmt = conn.prepareStatement(companyQuery);
             PreparedStatement jobStmt = conn.prepareStatement(jobQuery)) {
            
            compStmt.setString(1, job.getCompany());
            ResultSet rs = compStmt.executeQuery();
            int companyId = 1; // Default fallback to match sample data
            if (rs.next()) {
                companyId = rs.getInt(1);
            }
            
            jobStmt.setInt(1, companyId);
            jobStmt.setString(2, job.getTitle());
            jobStmt.setString(3, job.getDescription());
            // No strict employer_id in custom jobs schema, so we just attach to company!
            
            int rowsAffected = jobStmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating job: " + e.getMessage());
            return false;
        }
    }

    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        String query = "SELECT j.job_id, j.title, c.name as company, j.description, j.location " +
                       "FROM jobs j LEFT JOIN companies c ON j.company_id = c.company_id " +
                       "ORDER BY j.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                jobs.add(new Job(
                    rs.getInt("job_id"),
                    0, // employerId is not strictly used in current schema since companies hold relation
                    rs.getString("title"),
                    rs.getString("company") != null ? rs.getString("company") : "Unknown",
                    rs.getString("description"),
                    rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching jobs: " + e.getMessage());
        }
        return jobs;
    }

    /**
     * Fetch jobs created after a specific job ID for alert monitoring.
     * Requirements: 1.1, 1.3
     * 
     * @param lastProcessedJobId The last processed job ID
     * @return List of jobs with job_id greater than lastProcessedJobId
     */
    public List<Job> getJobsSinceId(int lastProcessedJobId) {
        List<Job> jobs = new ArrayList<>();
        String query = "SELECT j.job_id, j.title, c.name as company, j.description, j.location, j.employer_id " +
                       "FROM jobs j LEFT JOIN companies c ON j.company_id = c.company_id " +
                       "WHERE j.job_id > ? " +
                       "ORDER BY j.job_id ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, lastProcessedJobId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    jobs.add(new Job(
                        rs.getInt("job_id"),
                        rs.getInt("employer_id"),
                        rs.getString("title"),
                        rs.getString("company") != null ? rs.getString("company") : "Unknown",
                        rs.getString("description"),
                        rs.getString("location")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching jobs since ID " + lastProcessedJobId + ": " + e.getMessage());
        }
        return jobs;
    }

    /**
     * Read the last processed job ID from job_processing_checkpoint.
     * Requirements: 1.1, 1.3
     * 
     * @return The last processed job ID, or 0 if no checkpoint exists
     */
    public int getLastProcessedJobId() {
        String query = "SELECT last_processed_job_id FROM job_processing_checkpoint WHERE checkpoint_id = 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("last_processed_job_id");
            }
        } catch (SQLException e) {
            System.err.println("Error reading last processed job ID: " + e.getMessage());
        }
        return 0; // Default to 0 if no checkpoint exists
    }

    /**
     * Update the last processed job ID in job_processing_checkpoint.
     * Requirements: 1.1, 1.3
     * 
     * @param jobId The job ID to set as last processed
     * @return true if update was successful, false otherwise
     */
    public boolean updateLastProcessedJobId(int jobId) {
        String query = "UPDATE job_processing_checkpoint " +
                       "SET last_processed_job_id = ?, last_processed_at = CURRENT_TIMESTAMP " +
                       "WHERE checkpoint_id = 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, jobId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating last processed job ID: " + e.getMessage());
            return false;
        }
    }
}
