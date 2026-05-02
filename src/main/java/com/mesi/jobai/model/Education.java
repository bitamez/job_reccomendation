package com.mesi.jobai.model;

public class Education {
    private String degree;
    private String institution;
    private String graduationYear;

    // Constructor with all fields
    public Education(String degree, String institution, String graduationYear) {
        this.degree = degree;
        this.institution = institution;
        this.graduationYear = graduationYear;
    }

    // Default constructor
    public Education() {
    }

    // Getters and setters
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(String graduationYear) {
        this.graduationYear = graduationYear;
    }
}
