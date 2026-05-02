package com.mesi.jobai.model;

public class WorkExperience {
    private String jobTitle;
    private String companyName;
    private String duration;
    private String description;

    // Constructor with all fields
    public WorkExperience(String jobTitle, String companyName, String duration, String description) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.duration = duration;
        this.description = description;
    }

    // Default constructor
    public WorkExperience() {
    }

    // Getters and setters
    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
