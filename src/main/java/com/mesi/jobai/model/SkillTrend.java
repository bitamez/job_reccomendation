package com.mesi.jobai.model;

public class SkillTrend {
    private String skillName;
    private int demandCount;

    // Constructor with all fields
    public SkillTrend(String skillName, int demandCount) {
        this.skillName = skillName;
        this.demandCount = demandCount;
    }

    // Default constructor
    public SkillTrend() {
    }

    // Getters and setters
    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getDemandCount() {
        return demandCount;
    }

    public void setDemandCount(int demandCount) {
        this.demandCount = demandCount;
    }
}
