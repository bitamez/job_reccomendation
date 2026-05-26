package com.mesi.jobai.service;

import com.mesi.jobai.model.Application;
import com.mesi.jobai.model.User;
import com.mesi.jobai.model.Job;
import com.mesi.jobai.dao.ApplicationDAO;
import com.mesi.jobai.dao.UserDAO;
import com.mesi.jobai.dao.JobDAO;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportGenerationService {
    private static final String REPORTS_DIR = "reports/";
    private ApplicationDAO applicationDAO;
    private UserDAO userDAO;
    private JobDAO jobDAO;

    public ReportGenerationService() {
        this.applicationDAO = new ApplicationDAO();
        this.userDAO = new UserDAO();
        this.jobDAO = new JobDAO();
        
        // Create reports directory if it doesn't exist
        File reportsDir = new File(REPORTS_DIR);
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }
    }

    /**
     * Generate a report when application status changes
     */
    public String generateStatusChangeReport(int applicationId, String oldStatus, String newStatus, int employerId) {
        try {
            Application application = applicationDAO.getApplicationById(applicationId);
            if (application == null) {
                return null;
            }

            User applicant = userDAO.getUserById(application.getApplicantId());
            User employer = userDAO.getUserById(employerId);
            Job job = jobDAO.getJobById(application.getJobId());

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = REPORTS_DIR + "status_change_" + applicationId + "_" + timestamp + ".txt";

            PrintWriter writer = new PrintWriter(fileName);
            
            writer.println("===========================================");
            writer.println("APPLICATION STATUS CHANGE REPORT");
            writer.println("===========================================");
            writer.println();
            writer.println("Report Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            writer.println();
            
            writer.println("APPLICATION DETAILS:");
            writer.println("Application ID: " + applicationId);
            writer.println("Status Change: " + oldStatus + " → " + newStatus);
            writer.println("Applied Date: " + application.getAppliedAt());
            writer.println();
            
            writer.println("JOB DETAILS:");
            writer.println("Job ID: " + job.getId());
            writer.println("Job Title: " + job.getTitle());
            writer.println("Company: " + job.getCompany());
            writer.println("Description: " + job.getDescription());
            writer.println();
            
            writer.println("APPLICANT DETAILS:");
            writer.println("Applicant ID: " + applicant.getId());
            writer.println("Name: " + applicant.getName());
            writer.println("Email: " + applicant.getEmail());
            writer.println();
            
            writer.println("EMPLOYER DETAILS:");
            writer.println("Employer ID: " + employer.getId());
            writer.println("Name: " + employer.getName());
            writer.println("Email: " + employer.getEmail());
            writer.println();
            
            writer.println("ACTION SUMMARY:");
            if ("ACCEPTED".equalsIgnoreCase(newStatus) || "HIRED".equalsIgnoreCase(newStatus)) {
                writer.println("✓ Applicant " + applicant.getName() + " has been HIRED for " + job.getTitle());
                writer.println("✓ Employer: " + employer.getName() + " (" + job.getCompany() + ")");
            } else if ("REJECTED".equalsIgnoreCase(newStatus)) {
                writer.println("✗ Application REJECTED");
                writer.println("✗ Applicant " + applicant.getName() + " was not selected for " + job.getTitle());
            } else if ("REVIEWED".equalsIgnoreCase(newStatus)) {
                writer.println("◉ Application under REVIEW");
                writer.println("◉ Employer " + employer.getName() + " is reviewing the application");
            } else {
                writer.println("◉ Status updated to: " + newStatus);
            }
            writer.println();
            
            writer.println("===========================================");
            writer.println("End of Report");
            writer.println("===========================================");
            
            writer.close();
            
            return fileName;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generate a daily summary report
     */
    public String generateDailySummaryReport() {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String fileName = REPORTS_DIR + "daily_summary_" + timestamp + ".txt";

            PrintWriter writer = new PrintWriter(fileName);
            
            writer.println("===========================================");
            writer.println("DAILY SUMMARY REPORT");
            writer.println("===========================================");
            writer.println();
            writer.println("Report Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            writer.println();
            
            // Get statistics
            int totalApplications = applicationDAO.getAllApplications().size();
            int pendingCount = applicationDAO.getApplicationsByStatus("PENDING").size();
            int reviewedCount = applicationDAO.getApplicationsByStatus("REVIEWED").size();
            int acceptedCount = applicationDAO.getApplicationsByStatus("ACCEPTED").size();
            int rejectedCount = applicationDAO.getApplicationsByStatus("REJECTED").size();
            
            writer.println("APPLICATION STATISTICS:");
            writer.println("Total Applications: " + totalApplications);
            writer.println("Pending: " + pendingCount);
            writer.println("Under Review: " + reviewedCount);
            writer.println("Accepted/Hired: " + acceptedCount);
            writer.println("Rejected: " + rejectedCount);
            writer.println();
            
            double acceptanceRate = totalApplications > 0 ? (acceptedCount * 100.0 / totalApplications) : 0;
            double rejectionRate = totalApplications > 0 ? (rejectedCount * 100.0 / totalApplications) : 0;
            
            writer.println("CONVERSION RATES:");
            writer.println("Acceptance Rate: " + String.format("%.2f%%", acceptanceRate));
            writer.println("Rejection Rate: " + String.format("%.2f%%", rejectionRate));
            writer.println();
            
            writer.println("===========================================");
            writer.println("End of Report");
            writer.println("===========================================");
            
            writer.close();
            
            return fileName;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generate employer-specific report
     */
    public String generateEmployerReport(int employerId) {
        try {
            User employer = userDAO.getUserById(employerId);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = REPORTS_DIR + "employer_" + employerId + "_" + timestamp + ".txt";

            PrintWriter writer = new PrintWriter(fileName);
            
            writer.println("===========================================");
            writer.println("EMPLOYER ACTIVITY REPORT");
            writer.println("===========================================");
            writer.println();
            writer.println("Report Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            writer.println();
            
            writer.println("EMPLOYER DETAILS:");
            writer.println("Employer ID: " + employerId);
            writer.println("Name: " + employer.getName());
            writer.println("Email: " + employer.getEmail());
            writer.println();
            
            // Get employer's jobs and applications
            int totalJobs = jobDAO.getJobsByEmployerId(employerId).size();
            
            writer.println("HIRING STATISTICS:");
            writer.println("Total Jobs Posted: " + totalJobs);
            writer.println();
            
            writer.println("===========================================");
            writer.println("End of Report");
            writer.println("===========================================");
            
            writer.close();
            
            return fileName;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
