package com.mesi.jobai.model;

import java.util.ArrayList;
import java.util.List;

public class ParsedResume {
    private String fullName;
    private String email;
    private String location;
    private List<String> skills;
    private List<WorkExperience> workExperience;
    private List<Education> education;

    // Constructor with all fields
    public ParsedResume(String fullName, String email, String location,
                       List<String> skills, List<WorkExperience> workExperience,
                       List<Education> education) {
        this.fullName = fullName;
        this.email = email;
        this.location = location;
        this.skills = skills != null ? skills : new ArrayList<>();
        this.workExperience = workExperience != null ? workExperience : new ArrayList<>();
        this.education = education != null ? education : new ArrayList<>();
    }

    // Default constructor
    public ParsedResume() {
        this.skills = new ArrayList<>();
        this.workExperience = new ArrayList<>();
        this.education = new ArrayList<>();
    }

    // Getters and setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills != null ? skills : new ArrayList<>();
    }

    public List<WorkExperience> getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(List<WorkExperience> workExperience) {
        this.workExperience = workExperience != null ? workExperience : new ArrayList<>();
    }

    public List<Education> getEducation() {
        return education;
    }

    public void setEducation(List<Education> education) {
        this.education = education != null ? education : new ArrayList<>();
    }
}
