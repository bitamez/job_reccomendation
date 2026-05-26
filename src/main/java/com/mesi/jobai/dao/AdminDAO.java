package com.mesi.jobai.dao;

import com.mesi.jobai.config.DBConnection;
import com.mesi.jobai.model.Admin;
import com.mesi.jobai.model.User;
import com.mesi.jobai.model.Job;
import com.mesi.jobai.model.Application;
import com.mesi.jobai.model.Skill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    // Admin authentication
    public Admin loginAdmin(String email, String password) {
        String query = "SELECT * FROM admins WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Admin(
                        rs.getInt("admin_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("full_name")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error logging in admin: " + e.getMessage());
        }
        return null;
    }

    // User Management
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(new User(
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
        }
        return users;
    }

    public boolean deleteUser(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUser(User user) {
        String query = "UPDATE users SET full_name = ?, email = ?, role = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getRole());
            pstmt.setInt(4, user.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    // Employer Management
    public List<User> getAllEmployers() {
        List<User> employers = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'EMPLOYER'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                employers.add(new User(
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting employers: " + e.getMessage());
        }
        return employers;
    }

    public boolean approveEmployer(int employerId) {
        String query = "UPDATE users SET role = 'EMPLOYER' WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, employerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error approving employer: " + e.getMessage());
            return false;
        }
    }

    // Job Management
    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        String query = "SELECT j.job_id, j.company_id as employer_id, j.title, c.name as company, j.description, '' as requirements " +
                      "FROM jobs j LEFT JOIN companies c ON j.company_id = c.company_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                jobs.add(new Job(
                    rs.getInt("job_id"),
                    rs.getInt("employer_id"),
                    rs.getString("title"),
                    rs.getString("company"),
                    rs.getString("description"),
                    rs.getString("requirements")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all jobs: " + e.getMessage());
        }
        return jobs;
    }

    public boolean addJob(Job job) {
        String query = "INSERT INTO jobs (company_id, title, description) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, job.getEmployerId());
            pstmt.setString(2, job.getTitle());
            pstmt.setString(3, job.getDescription());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding job: " + e.getMessage());
            return false;
        }
    }

    public boolean updateJob(Job job) {
        String query = "UPDATE jobs SET title = ?, description = ? WHERE job_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, job.getTitle());
            pstmt.setString(2, job.getDescription());
            pstmt.setInt(3, job.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating job: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteJob(int jobId) {
        String query = "DELETE FROM jobs WHERE job_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, jobId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting job: " + e.getMessage());
            return false;
        }
    }

    // Application Management
    public List<Application> getAllApplications() {
        List<Application> applications = new ArrayList<>();
        String query = "SELECT a.application_id, a.job_id, a.user_id, a.status, " +
                      "a.applied_at::text as applied_at " +
                      "FROM applications a";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Application app = new Application(
                    rs.getInt("application_id"),
                    rs.getInt("job_id"),
                    rs.getInt("user_id"),
                    rs.getString("status"),
                    rs.getString("applied_at")
                );
                applications.add(app);
            }
        } catch (SQLException e) {
            System.err.println("Error getting applications: " + e.getMessage());
        }
        return applications;
    }

    // Skills Management
    public List<Skill> getAllSkills() {
        List<Skill> skills = new ArrayList<>();
        String query = "SELECT skill_id, skill_name, 'General' as category FROM skills";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                skills.add(new Skill(
                    rs.getInt("skill_id"),
                    rs.getString("skill_name"),
                    rs.getString("category")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting skills: " + e.getMessage());
        }
        return skills;
    }

    public boolean addSkill(String skillName, String category) {
        String query = "INSERT INTO skills (skill_name) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, skillName);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding skill: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSkill(int skillId) {
        String query = "DELETE FROM skills WHERE skill_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, skillId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting skill: " + e.getMessage());
            return false;
        }
    }

    // Dashboard Statistics
    public int getTotalUsers() {
        String query = "SELECT COUNT(*) FROM users";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total users: " + e.getMessage());
        }
        return 0;
    }

    public int getTotalEmployers() {
        String query = "SELECT COUNT(*) FROM users WHERE role = 'EMPLOYER'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total employers: " + e.getMessage());
        }
        return 0;
    }

    public int getTotalJobs() {
        String query = "SELECT COUNT(*) FROM jobs";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total jobs: " + e.getMessage());
        }
        return 0;
    }
}