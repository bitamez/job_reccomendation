package com.mesi.jobai.model;

public class Job {
    private int id;
    private int employerId;
    private String title;
    private String company;
    private String description;
    private String requirements;

    public Job(int id, int employerId, String title, String company, String description, String requirements) {
        this.id = id;
        this.employerId = employerId;
        this.title = title;
        this.company = company;
        this.description = description;
        this.requirements = requirements;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getEmployerId() { return employerId; }
    public void setEmployerId(int employerId) { this.employerId = employerId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
}
