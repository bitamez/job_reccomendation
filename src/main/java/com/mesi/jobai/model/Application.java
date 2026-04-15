package com.mesi.jobai.model;

public class Application {
    private int id;
    private int jobId;
    private int applicantId;
    private String status;
    private String appliedAt;
    
    // Virtual fields for joining data in UI
    private String jobTitle;
    private String companyName;
    private String applicantName;

    public Application(int id, int jobId, int applicantId, String status, String appliedAt) {
        this.id = id;
        this.jobId = jobId;
        this.applicantId = applicantId;
        this.status = status;
        this.appliedAt = appliedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public int getApplicantId() { return applicantId; }
    public void setApplicantId(int applicantId) { this.applicantId = applicantId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAppliedAt() { return appliedAt; }
    public void setAppliedAt(String appliedAt) { this.appliedAt = appliedAt; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }
}
