package com.mesi.jobai.controller;

import com.mesi.jobai.dao.RecommendationDAO;
import com.mesi.jobai.dao.SkillDAO;
import com.mesi.jobai.dao.JobDAO;
import com.mesi.jobai.model.Job;
import com.mesi.jobai.model.Skill;
import com.mesi.jobai.service.AIService;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RecommendationController {
    private final RecommendationDAO recommendationDAO;
    private final SkillDAO skillDAO;

    public RecommendationController() {
        this.recommendationDAO = new RecommendationDAO();
        this.skillDAO = new SkillDAO();
    }

    /**
     * Retrieves AI-based match scores (from the native DB calculation) for a user.
     * @param userId the ID of the user
     * @return a map of job IDs to match scores
     */
    public Map<Integer, Double> getAIMatchScoresForUser(int userId) {
        if (userId <= 0) {
            return Collections.emptyMap();
        }
        return recommendationDAO.getAIMatchScoresForUser(userId);
    }

    /**
     * Calculates the match score for a job and user based on keyword comparison in Java.
     * @param job the Job object
     * @param userId the ID of the user
     * @return match score percentage (0-100)
     */
    public int calculateMatchScore(Job job, int userId) {
        if (job == null || userId <= 0) {
            return 35; // baseline
        }
        List<Skill> userSkills = skillDAO.getSkillsForUser(userId);
        return AIService.calculateMatchScore(job, userSkills);
    }

    /**
     * Gets list of skills for a user.
     * @param userId the user's ID
     * @return a list of Skill objects
     */
    public List<Skill> getUserSkills(int userId) {
        if (userId <= 0) {
            return Collections.emptyList();
        }
        return skillDAO.getSkillsForUser(userId);
    }

    /**
     * Adds a skill to a user profile.
     * @param skill the Skill object
     * @return true if added successfully, false otherwise
     */
    public boolean addUserSkill(Skill skill) {
        if (skill == null || skill.getSkillName() == null || skill.getSkillName().trim().isEmpty()) {
            return false;
        }
        return skillDAO.addSkill(skill);
    }

    /**
     * Removes a skill from a user profile.
     * @param userSkillId the user skill relation ID
     * @return true if removed successfully, false otherwise
     */
    public boolean removeUserSkill(int userSkillId) {
        if (userSkillId <= 0) {
            return false;
        }
        return skillDAO.removeSkill(userSkillId);
    }
}
