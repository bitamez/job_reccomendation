package com.mesi.jobai.dao;

import com.mesi.jobai.config.DBConnection;
import com.mesi.jobai.model.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Notification operations.
 * Handles CRUD operations for the notifications table with pagination and filtering support.
 * Requirements: 4.1, 4.3, 4.5, 4.6
 */
public class NotificationDAO {

    /**
     * Creates a new notification in the database.
     * 
     * @param notification The notification to create (notificationId will be auto-generated)
     * @return The generated notification ID, or -1 if creation failed
     * Requirements: 4.1
     */
    public int createNotification(Notification notification) {
        String query = "INSERT INTO notifications (user_id, type, title, message, related_job_id, " +
                      "related_application_id, is_read, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, " +
                      "COALESCE(?::timestamp, CURRENT_TIMESTAMP)) RETURNING notification_id";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, notification.getUserId());
            pstmt.setString(2, notification.getType());
            pstmt.setString(3, notification.getTitle());
            pstmt.setString(4, notification.getMessage());
            
            // Handle nullable Integer fields
            if (notification.getRelatedJobId() != null) {
                pstmt.setInt(5, notification.getRelatedJobId());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            
            if (notification.getRelatedApplicationId() != null) {
                pstmt.setInt(6, notification.getRelatedApplicationId());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }
            
            pstmt.setBoolean(7, notification.isRead());
            
            // Handle created_at - use provided value or default to CURRENT_TIMESTAMP
            if (notification.getCreatedAt() != null && !notification.getCreatedAt().isEmpty()) {
                pstmt.setString(8, notification.getCreatedAt());
            } else {
                pstmt.setNull(8, java.sql.Types.TIMESTAMP);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("notification_id");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating notification: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }

    /**
     * Retrieves notifications for a specific user with pagination support.
     * Notifications are returned in reverse chronological order (newest first).
     * 
     * @param userId The user ID to fetch notifications for
     * @param limit Maximum number of notifications to return (20 items per page)
     * @param offset Number of notifications to skip (for pagination)
     * @return List of notifications for the user
     * Requirements: 4.1, 4.6
     */
    public List<Notification> getNotificationsByUser(int userId, int limit, int offset) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT notification_id, user_id, type, title, message, related_job_id, " +
                      "related_application_id, is_read, created_at FROM notifications " +
                      "WHERE user_id = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification(
                        rs.getInt("notification_id"),
                        rs.getInt("user_id"),
                        rs.getString("type"),
                        rs.getString("title"),
                        rs.getString("message"),
                        (Integer) rs.getObject("related_job_id"),
                        (Integer) rs.getObject("related_application_id"),
                        rs.getBoolean("is_read"),
                        rs.getString("created_at")
                    );
                    notifications.add(notification);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching notifications for user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return notifications;
    }

    /**
     * Marks a notification as read.
     * 
     * @param notificationId The ID of the notification to mark as read
     * @return true if the notification was successfully marked as read, false otherwise
     * Requirements: 4.3
     */
    public boolean markAsRead(int notificationId) {
        String query = "UPDATE notifications SET is_read = TRUE WHERE notification_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, notificationId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error marking notification as read: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the count of unread notifications for a specific user.
     * 
     * @param userId The user ID to count unread notifications for
     * @return The number of unread notifications
     * Requirements: 4.3
     */
    public int getUnreadCount(int userId) {
        String query = "SELECT COUNT(*) as unread_count FROM notifications " +
                      "WHERE user_id = ? AND is_read = FALSE";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("unread_count");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting unread count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }

    /**
     * Retrieves notifications for a specific user filtered by type.
     * Notifications are returned in reverse chronological order (newest first).
     * 
     * @param userId The user ID to fetch notifications for
     * @param type The notification type to filter by ('job_alert', 'status_change', 'system')
     * @return List of notifications matching the specified type
     * Requirements: 4.5
     */
    public List<Notification> getNotificationsByType(int userId, String type) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT notification_id, user_id, type, title, message, related_job_id, " +
                      "related_application_id, is_read, created_at FROM notifications " +
                      "WHERE user_id = ? AND type = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, type);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification(
                        rs.getInt("notification_id"),
                        rs.getInt("user_id"),
                        rs.getString("type"),
                        rs.getString("title"),
                        rs.getString("message"),
                        (Integer) rs.getObject("related_job_id"),
                        (Integer) rs.getObject("related_application_id"),
                        rs.getBoolean("is_read"),
                        rs.getString("created_at")
                    );
                    notifications.add(notification);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching notifications by type: " + e.getMessage());
            e.printStackTrace();
        }
        
        return notifications;
    }
}
