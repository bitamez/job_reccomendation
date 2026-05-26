package com.mesi.jobai.model;

public class Recommendation {
    private int jobId;
    private int userId;
    private double matchScore;

    public Recommendation(int jobId, int userId, double matchScore) {
        this.jobId = jobId;
        this.userId = userId;
        this.matchScore = matchScore;
    }

    // Getters and Setters
    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }
}
