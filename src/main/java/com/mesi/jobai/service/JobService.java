package com.mesi.jobai.service;

import com.mesi.jobai.dao.JobDAO;
import com.mesi.jobai.model.Job;
import java.util.Collections;
import java.util.List;

/**
 * JobService handles business logic for job listings.
 * It sits between JobController and JobDAO.
 */
public class JobService {
    private final JobDAO jobDAO;

    public JobService() {
        this.jobDAO = new JobDAO();
    }

    /**
     * Validates and creates a new job listing.
     * Rules:
     *  - Title and Company are mandatory fields
     *  - EmployerId must be a valid positive number
     */
    public boolean createJob(int employerId, String title, String company, String description, String requirements) {
        if (employerId <= 0) return false;
        if (title == null || title.trim().isEmpty()) return false;
        if (company == null || company.trim().isEmpty()) return false;

        Job job = new Job(0, employerId, title.trim(), company.trim(), description, requirements);
        return jobDAO.createJob(job);
    }

    /**
     * Fetches all available job listings.
     * Returns an empty list if none are found.
     */
    public List<Job> getAllJobs() {
        List<Job> jobs = jobDAO.getAllJobs();
        return jobs != null ? jobs : Collections.emptyList();
    }
}
