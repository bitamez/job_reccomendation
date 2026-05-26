package com.mesi.jobai.controller;

import com.mesi.jobai.dao.AdminDAO;
import com.mesi.jobai.model.Admin;
import com.mesi.jobai.model.User;
import com.mesi.jobai.model.Job;
import com.mesi.jobai.model.Application;
import com.mesi.jobai.model.Skill;

import java.util.List;

/**
 * Controller class for admin operations in the Job AI System
 */
public class AdminController {
    private final AdminDAO adminDAO;

    public AdminController() {
        this.adminDAO = new AdminDAO();
    }

    // Admin Authentication
    public Admin loginAdmin(String username, String password) {
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return null;
        }
        return adminDAO.loginAdmin(username, password);
    }

    // User Management
    public List<User> getAllUsers() {
        return adminDAO.getAllUsers();
    }

    public boolean deleteUser(int userId) {
        return adminDAO.deleteUser(userId);
    }

    public boolean updateUser(User user) {
        if (user == null || user.getName() == null || user.getName().trim().isEmpty() ||
            user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return false;
        }
        return adminDAO.updateUser(user);
    }

    // Employer Management
    public List<User> getAllEmployers() {
        return adminDAO.getAllEmployers();
    }

    public boolean approveEmployer(int employerId) {
        return adminDAO.approveEmployer(employerId);
    }

    // Job Management
    public List<Job> getAllJobs() {
        return adminDAO.getAllJobs();
    }

    public boolean addJob(Job job) {
        if (job == null || job.getTitle() == null || job.getTitle().trim().isEmpty() ||
            job.getCompany() == null || job.getCompany().trim().isEmpty()) {
            return false;
        }
        return adminDAO.addJob(job);
    }

    public boolean updateJob(Job job) {
        if (job == null || job.getTitle() == null || job.getTitle().trim().isEmpty() ||
            job.getCompany() == null || job.getCompany().trim().isEmpty()) {
            return false;
        }
        return adminDAO.updateJob(job);
    }

    public boolean deleteJob(int jobId) {
        return adminDAO.deleteJob(jobId);
    }

    // Application Management
    public List<Application> getAllApplications() {
        return adminDAO.getAllApplications();
    }

    // Skills Management
    public List<Skill> getAllSkills() {
        return adminDAO.getAllSkills();
    }

    public boolean addSkill(String skillName, String category) {
        if (skillName == null || skillName.trim().isEmpty() ||
            category == null || category.trim().isEmpty()) {
            return false;
        }
        return adminDAO.addSkill(skillName, category);
    }

    public boolean deleteSkill(int skillId) {
        return adminDAO.deleteSkill(skillId);
    }

    // Dashboard Statistics
    public int getTotalUsers() {
        return adminDAO.getTotalUsers();
    }

    public int getTotalEmployers() {
        return adminDAO.getTotalEmployers();
    }

    public int getTotalJobs() {
        return adminDAO.getTotalJobs();
    }
}