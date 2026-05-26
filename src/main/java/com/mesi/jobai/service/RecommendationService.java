package com.mesi.jobai.service;

import com.mesi.jobai.dao.RecommendationDAO;
import com.mesi.jobai.dao.SkillDAO;
import com.mesi.jobai.model.Job;
import com.mesi.jobai.model.Skill;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * RecommendationService handles the business logic for
 * matching candidates to jobs using AI scoring.
 * It sits between RecommendationController and the DAOs/AIService.
 */
public class RecommendationService {
    private final RecommendationDAO recommendationDAO;
    private final SkillDAO skillDAO;

    public RecommendationService() {
        this.recommendationDAO = new RecommendationDAO();
        this.skillDAO = new SkillDAO();
    }

    /**
     * Retrieves AI match scores for a user from the database.
     * Returns an empty map if userId is invalid.
     */
    public Map<Integer, Double> getMatchScoresForUser(int userId) {
        if (userId <= 0) return Collections.emptyMap();
        return recommendationDAO.getAIMatchScoresForUser(userId);
    }

    /**
     * Calculates a Java-side match score between a job and user's skills.
     * Falls back to 35 (baseline) if no skills are found.
     */
    public int calculateMatchScore(Job job, int userId) {
        if (job == null || userId <= 0) return 35;
        List<Skill> userSkills = skillDAO.getSkillsForUser(userId);
        return AIService.calculateMatchScore(job, userSkills);
    }
}
