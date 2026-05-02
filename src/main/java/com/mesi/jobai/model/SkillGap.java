package com.mesi.jobai.model;

public class SkillGap {
    private String skillName;
    private int frequency; // How many jobs required this skill

    // Constructor with all fields
    public SkillGap(String skillName, int frequency) {
        this.skillName = skillName;
        this.frequency = frequency;
    }

    // Default constructor
    public SkillGap() {
    }

    // Getters and setters
    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
