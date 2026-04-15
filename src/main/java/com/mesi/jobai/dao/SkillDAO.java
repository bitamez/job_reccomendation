package com.mesi.jobai.dao;

import com.mesi.jobai.config.DBConnection;
import com.mesi.jobai.model.Skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SkillDAO {

    public boolean addSkill(Skill skill) {
        String insertSkillQuery = "INSERT INTO skills (skill_name) VALUES (?) ON CONFLICT (skill_name) DO NOTHING";
        String getSkillIdQuery = "SELECT skill_id FROM skills WHERE skill_name = ?";
        String linkSkillQuery = "INSERT INTO user_skills (user_id, skill_id, level) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection()) {
            // 1. Ensure the skill exists in globally defined `skills` table
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSkillQuery)) {
                insertStmt.setString(1, skill.getSkillName());
                insertStmt.executeUpdate();
            }
            
            // 2. Fetch the global skill_id
            int globalSkillId = -1;
            try (PreparedStatement getStmt = conn.prepareStatement(getSkillIdQuery)) {
                getStmt.setString(1, skill.getSkillName());
                ResultSet rs = getStmt.executeQuery();
                if (rs.next()) {
                    globalSkillId = rs.getInt("skill_id");
                }
            }
            
            if (globalSkillId == -1) return false;
            
            // 3. Link it to the user in `user_skills`
            try (PreparedStatement linkStmt = conn.prepareStatement(linkSkillQuery)) {
                linkStmt.setInt(1, skill.getUserId());
                linkStmt.setInt(2, globalSkillId);
                linkStmt.setString(3, skill.getProficiency());
                return linkStmt.executeUpdate() > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error adding skill: " + e.getMessage());
            return false;
        }
    }

    public boolean removeSkill(int userSkillId) {
        String query = "DELETE FROM user_skills WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userSkillId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error removing skill: " + e.getMessage());
            return false;
        }
    }

    public List<Skill> getSkillsForUser(int userId) {
        List<Skill> skills = new ArrayList<>();
        String query = "SELECT us.id, us.user_id, s.skill_name, us.level " +
                       "FROM user_skills us JOIN skills s ON us.skill_id = s.skill_id " +
                       "WHERE us.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    skills.add(new Skill(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("skill_name"),
                        rs.getString("level")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching skills: " + e.getMessage());
        }
        return skills;
    }
}
