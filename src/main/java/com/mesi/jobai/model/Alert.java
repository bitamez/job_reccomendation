package com.mesi.jobai.model;

import java.util.List;

public class Alert {
    private int alertId;
    private int userId;
    private int jobId;
    private int matchScore;
    private List<String> topMatchingSkills;
    private String createdAt;
    
    // Virtual fields for UI display
    private String jobTitle;
    private String companyName;

    // Constructor with all fields including virtual fields
    public Alert(int alertId, int userId, int jobId, int matchScore, List<String> topMatchingSkills,
                String createdAt, String jobTitle, String companyName) {
        this.alertId = alertId;
        this.userId = userId;
        this.jobId = jobId;
        this.matchScore = matchScore;
        this.topMatchingSkills = topMatchingSkills;
        this.createdAt = createdAt;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
    }

    // Constructor without alertId (for creating new alerts)
    public Alert(int userId, int jobId, int matchScore, List<String> topMatchingSkills,
                String createdAt, String jobTitle, String companyName) {
        this.userId = userId;
        this.jobId = jobId;
        this.matchScore = matchScore;
        this.topMatchingSkills = topMatchingSkills;
        this.createdAt = createdAt;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
    }

    // Constructor without virtual fields (for database operations)
    public Alert(int alertId, int userId, int jobId, int matchScore, List<String> topMatchingSkills,
                String createdAt) {
        this.alertId = alertId;
        this.userId = userId;
        this.jobId = jobId;
        this.matchScore = matchScore;
        this.topMatchingSkills = topMatchingSkills;
        this.createdAt = createdAt;
    }

    // Default constructor
    public Alert() {
    }

    // Getters and setters
    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(int matchScore) {
        this.matchScore = matchScore;
    }

    public List<String> getTopMatchingSkills() {
        return topMatchingSkills;
    }

    public void setTopMatchingSkills(List<String> topMatchingSkills) {
        this.topMatchingSkills = topMatchingSkills;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

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
}
