package com.mesi.jobai.model;

public class FunnelStage {
    private String stage;
    private int count;

    // Constructor with all fields
    public FunnelStage(String stage, int count) {
        this.stage = stage;
        this.count = count;
    }

    // Default constructor
    public FunnelStage() {
    }

    // Getters and setters
    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
