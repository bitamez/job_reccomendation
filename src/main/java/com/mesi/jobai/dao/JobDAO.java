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
}
