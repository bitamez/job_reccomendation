package com.mesi.jobai.model;

import java.time.LocalTime;

public class NotificationPreferences {
    private int userId;
    private boolean pushEnabled;
    private boolean emailEnabled;
    private int matchThreshold;
    private LocalTime quietHoursStart;
    private LocalTime quietHoursEnd;

    // Constructor with all fields
    public NotificationPreferences(int userId, boolean pushEnabled, boolean emailEnabled,
                                  int matchThreshold, LocalTime quietHoursStart, LocalTime quietHoursEnd) {
        this.userId = userId;
        this.pushEnabled = pushEnabled;
        this.emailEnabled = emailEnabled;
        this.matchThreshold = matchThreshold;
        this.quietHoursStart = quietHoursStart;
        this.quietHoursEnd = quietHoursEnd;
    }

    // Default constructor
    public NotificationPreferences() {
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isPushEnabled() {
        return pushEnabled;
    }

    public void setPushEnabled(boolean pushEnabled) {
        this.pushEnabled = pushEnabled;
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public int getMatchThreshold() {
        return matchThreshold;
    }

    public void setMatchThreshold(int matchThreshold) {
        this.matchThreshold = matchThreshold;
    }

    public LocalTime getQuietHoursStart() {
        return quietHoursStart;
    }

    public void setQuietHoursStart(LocalTime quietHoursStart) {
        this.quietHoursStart = quietHoursStart;
    }

    public LocalTime getQuietHoursEnd() {
        return quietHoursEnd;
    }

    public void setQuietHoursEnd(LocalTime quietHoursEnd) {
        this.quietHoursEnd = quietHoursEnd;
    }
}
