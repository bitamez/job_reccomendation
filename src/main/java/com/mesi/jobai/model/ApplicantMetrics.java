package com.mesi.jobai.model;

import java.util.List;
import java.util.Map;

public class ApplicantMetrics {
    private int totalApplications;
    private double successRate;
    private List<SkillGap> topSkillGaps;
    private List<DataPoint> matchScoreTrends;
    private Map<String, Integer> applicationsByStatus;
    private List<SkillTrend> marketTrends;

    // Constructor with all fields
    public ApplicantMetrics(int totalApplications, double successRate, List<SkillGap> topSkillGaps,
                           List<DataPoint> matchScoreTrends, Map<String, Integer> applicationsByStatus,
                           List<SkillTrend> marketTrends) {
        this.totalApplications = totalApplications;
        this.successRate = successRate;
        this.topSkillGaps = topSkillGaps;
        this.matchScoreTrends = matchScoreTrends;
        this.applicationsByStatus = applicationsByStatus;
        this.marketTrends = marketTrends;
    }

    // Default constructor
    public ApplicantMetrics() {
    }

    // Getters and setters
    public int getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(int totalApplications) {
        this.totalApplications = totalApplications;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    public List<SkillGap> getTopSkillGaps() {
        return topSkillGaps;
    }

    public void setTopSkillGaps(List<SkillGap> topSkillGaps) {
        this.topSkillGaps = topSkillGaps;
    }

    public List<DataPoint> getMatchScoreTrends() {
        return matchScoreTrends;
    }

    public void setMatchScoreTrends(List<DataPoint> matchScoreTrends) {
        this.matchScoreTrends = matchScoreTrends;
    }

    public Map<String, Integer> getApplicationsByStatus() {
        return applicationsByStatus;
    }

    public void setApplicationsByStatus(Map<String, Integer> applicationsByStatus) {
        this.applicationsByStatus = applicationsByStatus;
    }

    public List<SkillTrend> getMarketTrends() {
        return marketTrends;
    }

    public void setMarketTrends(List<SkillTrend> marketTrends) {
        this.marketTrends = marketTrends;
    }
}
