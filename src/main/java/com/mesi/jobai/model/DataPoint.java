package com.mesi.jobai.model;

public class DataPoint {
    private String label;
    private double value;

    // Constructor with all fields
    public DataPoint(String label, double value) {
        this.label = label;
        this.value = value;
    }

    // Default constructor
    public DataPoint() {
    }

    // Getters and setters
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
