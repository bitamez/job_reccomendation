package com.mesi.jobai.controller;

import com.mesi.jobai.dao.ApplicationDAO;
import com.mesi.jobai.model.Application;
import java.util.Collections;
import java.util.List;

public class ApplicationController {
    private final ApplicationDAO applicationDAO;

    public ApplicationController() {
        this.applicationDAO = new ApplicationDAO();
    }

    /**
     * Submits a job application for a given user and job.
     * @param jobId the ID of the job
     * @param applicantId the ID of the applicant user
     * @return true if successful, false otherwise
     */
    public boolean applyForJob(int jobId, int applicantId) {
        if (jobId <= 0 || applicantId <= 0) {
            return false;
        }
        return applicationDAO.applyForJob(jobId, applicantId);
    }

    /**
     * Retrieves all applications submitted by a specific user.
     * @param userId the ID of the applicant user
     * @return a list of Applications
     */
    public List<Application> getApplicationsForUser(int userId) {
        if (userId <= 0) {
            return Collections.emptyList();
        }
        return applicationDAO.getApplicationsForUser(userId);
    }

    /**
     * Retrieves all applications relevant to an employer.
     * @param employerId the ID of the employer
     * @return a list of Applications
     */
    public List<Application> getApplicationsForEmployer(int employerId) {
        return applicationDAO.getApplicationsForEmployer(employerId);
    }

    /**
     * Updates the status of a specific job application.
     * @param applicationId the ID of the application
     * @param newStatus the new status (e.g. APPROVED, REJECTED, PENDING)
     * @return true if successful, false otherwise
     */
    public boolean updateApplicationStatus(int applicationId, String newStatus) {
        if (applicationId <= 0 || newStatus == null || newStatus.trim().isEmpty()) {
            return false;
        }
        return applicationDAO.updateApplicationStatus(applicationId, newStatus);
    }
}
