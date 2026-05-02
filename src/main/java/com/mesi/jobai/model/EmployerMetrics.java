package com.mesi.jobai.model;

import java.util.List;
import java.util.Map;

public class EmployerMetrics {
    private Map<Integer, Integer> applicantCountsByJob;
    private List<FunnelStage> applicantFunnel;
    private Double averageTimeToHire;
    private List<DataPoint> matchScoreDistribution;
    private List<String> topInterviewSkills;

    // Constructor with all fields
    public EmployerMetrics(Map<Integer, Integer> applicantCountsByJob, List<FunnelStage> applicantFunnel,
                          Double averageTimeToHire, List<DataPoint> matchScoreDistribution,
                          List<String> topInterviewSkills) {
        this.applicantCountsByJob = applicantCountsByJob;
        this.applicantFunnel = applicantFunnel;
        this.averageTimeToHire = averageTimeToHire;
        this.matchScoreDistribution = matchScoreDistribution;
        this.topInterviewSkills = topInterviewSkills;
    }

    // Default constructor
    public EmployerMetrics() {
    }

    // Getters and setters
    public Map<Integer, Integer> getApplicantCountsByJob() {
        return applicantCountsByJob;
    }

    public void setApplicantCountsByJob(Map<Integer, Integer> applicantCountsByJob) {
        this.applicantCountsByJob = applicantCountsByJob;
    }

    public List<FunnelStage> getApplicantFunnel() {
        return applicantFunnel;
    }

    public void setApplicantFunnel(List<FunnelStage> applicantFunnel) {
        this.applicantFunnel = applicantFunnel;
    }

    public Double getAverageTimeToHire() {
        return averageTimeToHire;
    }

    public void setAverageTimeToHire(Double averageTimeToHire) {
        this.averageTimeToHire = averageTimeToHire;
    }

    public List<DataPoint> getMatchScoreDistribution() {
        return matchScoreDistribution;
    }

    public void setMatchScoreDistribution(List<DataPoint> matchScoreDistribution) {
        this.matchScoreDistribution = matchScoreDistribution;
    }

    public List<String> getTopInterviewSkills() {
        return topInterviewSkills;
    }

    public void setTopInterviewSkills(List<String> topInterviewSkills) {
        this.topInterviewSkills = topInterviewSkills;
    }
}
