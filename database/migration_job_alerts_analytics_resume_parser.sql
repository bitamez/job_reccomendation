-- Migration Script: Job Alerts, Analytics Dashboard, and Resume Parser
-- Feature: job-alerts-analytics-resume-parser
-- Description: Adds tables for notifications, job alerts, notification preferences,
--              analytics cache, parsed resumes, and job processing checkpoint.
--              Also adds employer_id to jobs table and performance indexes.
-- Requirements: 14.3, 15.3

-- ============================================================================
-- 1. Add employer_id column to jobs table
-- ============================================================================
-- This tracks which employer (user) posted each job
ALTER TABLE jobs ADD COLUMN IF NOT EXISTS employer_id INT REFERENCES users(user_id) ON DELETE CASCADE;

-- ============================================================================
-- 2. Notifications table
-- ============================================================================
-- Stores all notifications (job alerts, status changes, system messages)
CREATE TABLE IF NOT EXISTS notifications (
    notification_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL, -- 'job_alert', 'status_change', 'system'
    title VARCHAR(200),
    message TEXT,
    related_job_id INT REFERENCES jobs(job_id) ON DELETE SET NULL,
    related_application_id INT REFERENCES applications(application_id) ON DELETE SET NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- 3. Job alerts table
-- ============================================================================
-- Tracks job alerts created when new jobs match applicant skills
CREATE TABLE IF NOT EXISTS job_alerts (
    alert_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    job_id INT NOT NULL REFERENCES jobs(job_id) ON DELETE CASCADE,
    match_score INT NOT NULL CHECK (match_score >= 0 AND match_score <= 100),
    top_matching_skills TEXT, -- JSON array of top 3 skills
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- 4. User notification preferences
-- ============================================================================
-- Stores per-user notification settings
CREATE TABLE IF NOT EXISTS notification_preferences (
    user_id INT PRIMARY KEY REFERENCES users(user_id) ON DELETE CASCADE,
    push_enabled BOOLEAN DEFAULT TRUE,
    email_enabled BOOLEAN DEFAULT TRUE,
    match_threshold INT DEFAULT 70 CHECK (match_threshold >= 50 AND match_threshold <= 100),
    quiet_hours_start TIME,
    quiet_hours_end TIME,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- 5. Analytics cache table
-- ============================================================================
-- Caches computed analytics metrics to improve dashboard performance
CREATE TABLE IF NOT EXISTS analytics_cache (
    cache_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    cache_key VARCHAR(100) NOT NULL,
    cache_value TEXT, -- JSON serialized metrics
    computed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, cache_key)
);

-- ============================================================================
-- 6. Parsed resumes table
-- ============================================================================
-- Audit trail for resume uploads and parsing results
CREATE TABLE IF NOT EXISTS parsed_resumes (
    resume_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    file_name VARCHAR(255),
    file_size_bytes INT,
    parsed_data TEXT, -- JSON serialized ParsedResume
    parsing_status VARCHAR(50), -- 'success', 'partial', 'failed'
    error_message TEXT,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- 7. Job processing checkpoint
-- ============================================================================
-- Tracks the last processed job for alert monitoring
CREATE TABLE IF NOT EXISTS job_processing_checkpoint (
    checkpoint_id INT PRIMARY KEY DEFAULT 1,
    last_processed_job_id INT,
    last_processed_at TIMESTAMP,
    CHECK (checkpoint_id = 1) -- Ensure single row
);

-- ============================================================================
-- 8. Performance Indexes
-- ============================================================================

-- Notifications indexes
CREATE INDEX IF NOT EXISTS idx_notifications_user_id ON notifications(user_id);
CREATE INDEX IF NOT EXISTS idx_notifications_created_at ON notifications(created_at);
CREATE INDEX IF NOT EXISTS idx_notifications_is_read ON notifications(is_read);
CREATE INDEX IF NOT EXISTS idx_notifications_type ON notifications(type);

-- Job alerts indexes
CREATE INDEX IF NOT EXISTS idx_job_alerts_user_id ON job_alerts(user_id);
CREATE INDEX IF NOT EXISTS idx_job_alerts_job_id ON job_alerts(job_id);
CREATE INDEX IF NOT EXISTS idx_job_alerts_created_at ON job_alerts(created_at);

-- Analytics cache indexes
CREATE INDEX IF NOT EXISTS idx_analytics_cache_user_id ON analytics_cache(user_id);
CREATE INDEX IF NOT EXISTS idx_analytics_cache_computed_at ON analytics_cache(computed_at);

-- Parsed resumes indexes
CREATE INDEX IF NOT EXISTS idx_parsed_resumes_user_id ON parsed_resumes(user_id);
CREATE INDEX IF NOT EXISTS idx_parsed_resumes_uploaded_at ON parsed_resumes(uploaded_at);

-- Applications indexes (for analytics queries)
CREATE INDEX IF NOT EXISTS idx_applications_user_id ON applications(user_id);
CREATE INDEX IF NOT EXISTS idx_applications_job_id ON applications(job_id);
CREATE INDEX IF NOT EXISTS idx_applications_status ON applications(status);
CREATE INDEX IF NOT EXISTS idx_applications_applied_at ON applications(applied_at);

-- Jobs indexes (for analytics and alert queries)
CREATE INDEX IF NOT EXISTS idx_jobs_created_at ON jobs(created_at);
CREATE INDEX IF NOT EXISTS idx_jobs_employer_id ON jobs(employer_id);

-- Recommendations indexes (for match score trends)
CREATE INDEX IF NOT EXISTS idx_recommendations_user_id ON recommendations(user_id);
CREATE INDEX IF NOT EXISTS idx_recommendations_created_at ON recommendations(created_at);

-- ============================================================================
-- 9. Initialize checkpoint table with single row
-- ============================================================================
INSERT INTO job_processing_checkpoint (checkpoint_id, last_processed_job_id, last_processed_at)
VALUES (1, 0, CURRENT_TIMESTAMP)
ON CONFLICT (checkpoint_id) DO NOTHING;

-- ============================================================================
-- Migration Complete
-- ============================================================================
-- Tables created: notifications, job_alerts, notification_preferences,
--                 analytics_cache, parsed_resumes, job_processing_checkpoint
-- Columns added: jobs.employer_id
-- Indexes added: 18 performance indexes for optimized queries
-- ============================================================================
