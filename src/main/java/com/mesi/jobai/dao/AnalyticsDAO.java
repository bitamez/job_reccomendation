package com.mesi.jobai.dao;

import com.mesi.jobai.config.DBConnection;
import com.mesi.jobai.model.ApplicantMetrics;
import com.mesi.jobai.model.DataPoint;
import com.mesi.jobai.model.EmployerMetrics;
import com.mesi.jobai.model.FunnelStage;
import com.mesi.jobai.model.SkillGap;
import com.mesi.jobai.model.SkillTrend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AnalyticsDAO provides database queries for generating
 * analytics reports for both applicants and employers.
 */
public class AnalyticsDAO {

    /**
     * Builds a full ApplicantMetrics summary for a given user.
     * Includes: total applications, success rate, skill gaps, and status breakdown.
     * @param userId the applicant's user ID
     * @return populated ApplicantMetrics object
     */
    public ApplicantMetrics getApplicantMetrics(int userId) {
        ApplicantMetrics metrics = new ApplicantMetrics();

        // 1. Total applications count
        int total = countApplicationsForUser(userId);
        metrics.setTotalApplications(total);

        // 2. Application status breakdown
        Map<String, Integer> statusMap = getApplicationStatusBreakdown(userId);
        metrics.setApplicationsByStatus(statusMap);

        // 3. Success rate = (HIRED + INTERVIEW) / total * 100
        int hired = statusMap.getOrDefault("HIRED", 0);
        int interview = statusMap.getOrDefault("INTERVIEW", 0);
        double successRate = total == 0 ? 0.0 : ((double)(hired + interview) / total) * 100.0;
        metrics.setSuccessRate(Math.round(successRate * 10.0) / 10.0);

        // 4. Top skill gaps (skills required by jobs user applied for but user doesn't have)
        metrics.setTopSkillGaps(getTopSkillGapsForUser(userId));

        // 5. Market trends (most demanded skills across all jobs)
        metrics.setMarketTrends(getMarketSkillTrends());

        return metrics;
    }

    /**
     * Builds a full EmployerMetrics summary.
     * Includes: applicant counts per job, hiring funnel stages, and match score distribution.
     */
    public EmployerMetrics getEmployerMetrics() {
        EmployerMetrics metrics = new EmployerMetrics();
        metrics.setApplicantCountsByJob(getApplicantCountsByJob());
        metrics.setApplicantFunnel(getHiringFunnel());
        return metrics;
    }

    // ─────────────────────── Private Helpers ───────────────────────

    private int countApplicationsForUser(int userId) {
        String query = "SELECT COUNT(*) FROM applications WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting applications: " + e.getMessage());
        }
        return 0;
    }

    private Map<String, Integer> getApplicationStatusBreakdown(int userId) {
        Map<String, Integer> statusMap = new HashMap<>();
        String query = "SELECT status, COUNT(*) as cnt FROM applications WHERE user_id = ? GROUP BY status";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String status = rs.getString("status");
                    int count = rs.getInt("cnt");
                    statusMap.put(status != null ? status.toUpperCase() : "PENDING", count);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting status breakdown: " + e.getMessage());
        }
        return statusMap;
    }

    private List<SkillGap> getTopSkillGapsForUser(int userId) {
        List<SkillGap> gaps = new ArrayList<>();
        // Skills required by jobs the user applied for, but user does not have
        String query =
            "SELECT s.skill_name, COUNT(*) as freq " +
            "FROM job_skills js " +
            "JOIN skills s ON js.skill_id = s.skill_id " +
            "JOIN applications a ON a.job_id = js.job_id " +
            "WHERE a.user_id = ? " +
            "AND js.skill_id NOT IN (" +
            "    SELECT skill_id FROM user_skills WHERE user_id = ?" +
            ") " +
            "GROUP BY s.skill_name ORDER BY freq DESC LIMIT 5";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    gaps.add(new SkillGap(rs.getString("skill_name"), rs.getInt("freq")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting skill gaps: " + e.getMessage());
        }
        return gaps;
    }

    private List<SkillTrend> getMarketSkillTrends() {
        List<SkillTrend> trends = new ArrayList<>();
        String query =
            "SELECT s.skill_name, COUNT(*) as demand " +
            "FROM job_skills js JOIN skills s ON js.skill_id = s.skill_id " +
            "GROUP BY s.skill_name ORDER BY demand DESC LIMIT 10";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                trends.add(new SkillTrend(rs.getString("skill_name"), rs.getInt("demand")));
            }
        } catch (SQLException e) {
            System.err.println("Error getting market trends: " + e.getMessage());
        }
        return trends;
    }

    private Map<Integer, Integer> getApplicantCountsByJob() {
        Map<Integer, Integer> counts = new HashMap<>();
        String query = "SELECT job_id, COUNT(*) as cnt FROM applications GROUP BY job_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                counts.put(rs.getInt("job_id"), rs.getInt("cnt"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting applicant counts by job: " + e.getMessage());
        }
        return counts;
    }

    private List<FunnelStage> getHiringFunnel() {
        List<FunnelStage> funnel = new ArrayList<>();
        String query = "SELECT COALESCE(status, 'PENDING') as stage, COUNT(*) as cnt " +
                       "FROM applications GROUP BY stage ORDER BY cnt DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                funnel.add(new FunnelStage(rs.getString("stage"), rs.getInt("cnt")));
            }
        } catch (SQLException e) {
            System.err.println("Error getting hiring funnel: " + e.getMessage());
        }
        return funnel;
    }
}
