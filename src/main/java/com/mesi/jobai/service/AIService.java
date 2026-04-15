package com.mesi.jobai.service;

import com.mesi.jobai.model.Job;
import com.mesi.jobai.model.Skill;

import java.util.List;

public class AIService {

    /**
     * Calculates a match score between 0-100 based on keyword intersection 
     * between the user's documented skills and the job's description/requirements.
     */
    public static int calculateMatchScore(Job job, List<Skill> userSkills) {
        if (userSkills == null || userSkills.isEmpty()) {
            return 35; // A baseline score for users who haven't entered skills yet
        }

        String jobText = (job.getTitle() + " " + job.getDescription() + " " + job.getRequirements()).toLowerCase();
        
        int matchWeight = 0;
        int totalWeight = 0;

        for (Skill skill : userSkills) {
            String skillWord = skill.getSkillName().toLowerCase().trim();
            
            // Assign weight based on proficiency level
            int weight = 1; // Beginner
            if ("Intermediate".equalsIgnoreCase(skill.getProficiency())) weight = 2;
            else if ("Advanced".equalsIgnoreCase(skill.getProficiency())) weight = 3;
            else if ("Expert".equalsIgnoreCase(skill.getProficiency())) weight = 4;
            
            totalWeight += weight;
            
            // If the job mentions the skill, add to their match weight
            if (jobText.contains(skillWord)) {
                matchWeight += weight;
            }
        }
        
        // Base match score of 40 (since other soft skills exist), 
        // the remaining 60 points are earned via hard skill matches.
        int baseScore = 40;
        double matchRatio = totalWeight == 0 ? 0 : (double) matchWeight / totalWeight;
        
        int finalScore = baseScore + (int) (matchRatio * 60);

        // Keep it strictly bounded between 0 and 100
        return Math.min(100, Math.max(0, finalScore)); 
    }
}
