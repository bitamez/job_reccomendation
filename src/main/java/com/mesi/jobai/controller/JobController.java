package com.mesi.jobai.controller;

import com.mesi.jobai.dao.JobDAO;
import com.mesi.jobai.model.Job;
import java.util.List;

public class JobController {
    private final JobDAO jobDAO;

    public JobController() {
        this.jobDAO = new JobDAO();
    }

    /**
     * Creates and posts a new job listing.
     * @param employerId the ID of the employer posting the job
     * @param title the title of the job
     * @param company the name of the company
     * @param description the description of the job
     * @param requirements the requirements for the job
     * @return true if creation succeeded, false otherwise
     */
    public boolean createJob(int employerId, String title, String company, String description, String requirements) {
        if (title == null || title.trim().isEmpty() ||
            company == null || company.trim().isEmpty()) {
            return false;
        }
        Job job = new Job(0, employerId, title, company, description, requirements);
        return jobDAO.createJob(job);
    }

    /**
     * Retrieves all job listings from the system.
     * @return a list of all Jobs
     */
    public List<Job> getAllJobs() {
        return jobDAO.getAllJobs();
    }
}
