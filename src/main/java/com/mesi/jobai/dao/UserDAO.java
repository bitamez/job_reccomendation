package com.mesi.jobai.dao;

import com.mesi.jobai.config.DBConnection;
import com.mesi.jobai.model.NotificationPreferences;
import com.mesi.jobai.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class UserDAO {

    public boolean registerUser(User user) {
        // Mapped to custom schema: full_name and user_id!
        String query = "INSERT INTO users (full_name, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    public User loginUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role") != null ? rs.getString("role") : "APPLICANT"
                    );
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error logging in: " + e.getMessage());
        }
        return null;
    }

    public boolean updateUser(User user) {
        String query = "UPDATE users SET full_name = ?, email = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setInt(3, user.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }


    /**
     * Fetch notification preferences for a user.
     * Returns default preferences if none exist.
     *
     * @param userId The user ID
     * @return NotificationPreferences object with user's settings
     */
    public NotificationPreferences getNotificationPreferences(int userId) {
        String query = "SELECT user_id, push_enabled, email_enabled, match_threshold, " +
                      "quiet_hours_start, quiet_hours_end FROM notification_preferences WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    NotificationPreferences prefs = new NotificationPreferences();
                    prefs.setUserId(rs.getInt("user_id"));
                    prefs.setPushEnabled(rs.getBoolean("push_enabled"));
                    prefs.setEmailEnabled(rs.getBoolean("email_enabled"));
                    prefs.setMatchThreshold(rs.getInt("match_threshold"));

                    // Handle nullable time fields
                    java.sql.Time quietStart = rs.getTime("quiet_hours_start");
                    if (quietStart != null) {
                        prefs.setQuietHoursStart(quietStart.toLocalTime());
                    }

                    java.sql.Time quietEnd = rs.getTime("quiet_hours_end");
                    if (quietEnd != null) {
                        prefs.setQuietHoursEnd(quietEnd.toLocalTime());
                    }

                    return prefs;
                } else {
                    // Return default preferences if none exist
                    NotificationPreferences defaultPrefs = new NotificationPreferences();
                    defaultPrefs.setUserId(userId);
                    defaultPrefs.setPushEnabled(true);
                    defaultPrefs.setEmailEnabled(true);
                    defaultPrefs.setMatchThreshold(70);
                    return defaultPrefs;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching notification preferences: " + e.getMessage());
            // Return default preferences on error
            NotificationPreferences defaultPrefs = new NotificationPreferences();
            defaultPrefs.setUserId(userId);
            defaultPrefs.setPushEnabled(true);
            defaultPrefs.setEmailEnabled(true);
            defaultPrefs.setMatchThreshold(70);
            return defaultPrefs;
        }
    }

    /**
     * Update or insert notification preferences for a user.
     * Uses UPSERT pattern (INSERT ... ON CONFLICT UPDATE).
     *
     * @param prefs The notification preferences to save
     * @return true if successful, false otherwise
     */
    public boolean updateNotificationPreferences(NotificationPreferences prefs) {
        String query = "INSERT INTO notification_preferences " +
                      "(user_id, push_enabled, email_enabled, match_threshold, quiet_hours_start, quiet_hours_end, updated_at) " +
                      "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP) " +
                      "ON CONFLICT (user_id) DO UPDATE SET " +
                      "push_enabled = EXCLUDED.push_enabled, " +
                      "email_enabled = EXCLUDED.email_enabled, " +
                      "match_threshold = EXCLUDED.match_threshold, " +
                      "quiet_hours_start = EXCLUDED.quiet_hours_start, " +
                      "quiet_hours_end = EXCLUDED.quiet_hours_end, " +
                      "updated_at = CURRENT_TIMESTAMP";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, prefs.getUserId());
            pstmt.setBoolean(2, prefs.isPushEnabled());
            pstmt.setBoolean(3, prefs.isEmailEnabled());
            pstmt.setInt(4, prefs.getMatchThreshold());

            // Handle nullable time fields
            if (prefs.getQuietHoursStart() != null) {
                pstmt.setTime(5, java.sql.Time.valueOf(prefs.getQuietHoursStart()));
            } else {
                pstmt.setNull(5, java.sql.Types.TIME);
            }

            if (prefs.getQuietHoursEnd() != null) {
                pstmt.setTime(6, java.sql.Time.valueOf(prefs.getQuietHoursEnd()));
            } else {
                pstmt.setNull(6, java.sql.Types.TIME);
            }

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating notification preferences: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update the location field for a user.
     *
     * @param userId The user ID
     * @param location The new location value
     * @return true if successful, false otherwise
     */
    public boolean updateLocation(int userId, String location) {
        String query = "UPDATE users SET location = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, location);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating user location: " + e.getMessage());
            return false;
        }
    }

}
