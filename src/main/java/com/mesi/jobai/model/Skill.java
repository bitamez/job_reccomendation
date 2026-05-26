package com.mesi.jobai.model;

public class Skill {
    private int id;
    private int userId;
    private String skillName;
    private String proficiency;
    private String category; // Added for admin functionality

    // Original constructor for user skills
    public Skill(int id, int userId, String skillName, String proficiency) {
        this.id = id;
        this.userId = userId;
        this.skillName = skillName;
        this.proficiency = proficiency;
    }

    // Constructor for admin skill management (skill catalog)
    public Skill(int id, String skillName, String category) {
        this.id = id;
        this.skillName = skillName;
        this.category = category;
        this.userId = 0; // Not applicable for skill catalog
        this.proficiency = null; // Not applicable for skill catalog
    }

    // Default constructor
    public Skill() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    
    // Alias method for compatibility
    public String getName() { return skillName; }
    public void setName(String name) { this.skillName = name; }

    public String getProficiency() { return proficiency; }
    public void setProficiency(String proficiency) { this.proficiency = proficiency; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
