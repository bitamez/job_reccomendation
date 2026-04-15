package com.mesi.jobai.dao;

import com.mesi.jobai.config.DBConnection;
import com.mesi.jobai.model.Job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RecommendationDAO {

    /**
     * Executes the custom AI Match Query efficiently via PostgreSQL native logic
     * instead of relying on the Java loop.
     * Returns a map of Job IDs to their Match Score (0.0 to 1.0).
     */
    public Map<Integer, Double> getAIMatchScoresForUser(int userId) {
        Map<Integer, Double> matchScores = new HashMap<>();
        
        String query = "SELECT " +
                       "    j.job_id, " +
                       "    j.title, " +
                       "    ROUND( " +
                       "        COUNT(us.skill_id)::decimal / NULLIF(COUNT(js.skill_id), 0), 2 " +
                       "    ) AS match_score " +
                       "FROM jobs j " +
                       "LEFT JOIN job_skills js ON j.job_id = js.job_id " +
                       "LEFT JOIN user_skills us " +
                       "    ON us.skill_id = js.skill_id " +
                       "    AND us.user_id = ? " +
                       "GROUP BY j.job_id, j.title " +
                       "ORDER BY match_score DESC NULLS LAST";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int jobId = rs.getInt("job_id");
                    double score = rs.getDouble("match_score");
                    
                    // If jobs have no specific constraints, give a generic 0.35 fit.
                    if (rs.wasNull()) {
                        score = 0.35; 
                    }
                    matchScores.put(jobId, score);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error calculating AI recommendations: " + e.getMessage());
        }
        
        return matchScores;
    }
}
