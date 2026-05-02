package com.mesi.jobai.model;

public class SuggestedSkill {
    private String skillName;
    private int confidenceScore; // 0-100

    // Constructor with all fields
    public SuggestedSkill(String skillName, int confidenceScore) {
        this.skillName = skillName;
        this.confidenceScore = confidenceScore;
    }

    // Default constructor
    public SuggestedSkill() {
    }

    // Getters and setters
    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(int confidenceScore) {
        this.confidenceScore = confidenceScore;
    }
}
