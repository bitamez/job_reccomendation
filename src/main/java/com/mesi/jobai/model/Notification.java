package com.mesi.jobai.model;

public class Notification {
    private int notificationId;
    private int userId;
    private String type; // job_alert, status_change, system
    private String title;
    private String message;
    private Integer relatedJobId;
    private Integer relatedApplicationId;
    private boolean isRead;
    private String createdAt;

    // Constructor with all fields
    public Notification(int notificationId, int userId, String type, String title, String message,
                       Integer relatedJobId, Integer relatedApplicationId, boolean isRead, String createdAt) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.relatedJobId = relatedJobId;
        this.relatedApplicationId = relatedApplicationId;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    // Constructor without notificationId (for creating new notifications)
    public Notification(int userId, String type, String title, String message,
                       Integer relatedJobId, Integer relatedApplicationId, boolean isRead, String createdAt) {
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.relatedJobId = relatedJobId;
        this.relatedApplicationId = relatedApplicationId;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    // Default constructor
    public Notification() {
    }

    // Getters and setters
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getRelatedJobId() {
        return relatedJobId;
    }

    public void setRelatedJobId(Integer relatedJobId) {
        this.relatedJobId = relatedJobId;
    }

    public Integer getRelatedApplicationId() {
        return relatedApplicationId;
    }

    public void setRelatedApplicationId(Integer relatedApplicationId) {
        this.relatedApplicationId = relatedApplicationId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
