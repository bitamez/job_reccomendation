package com.mesi.jobai.model;

public class Skill {
    private int id;
    private int userId;
    private String skillName;
    private String proficiency;

    public Skill(int id, int userId, String skillName, String proficiency) {
        this.id = id;
        this.userId = userId;
        this.skillName = skillName;
        this.proficiency = proficiency;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getProficiency() { return proficiency; }
    public void setProficiency(String proficiency) { this.proficiency = proficiency; }
}
