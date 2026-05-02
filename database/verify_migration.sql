-- Verification Script for Job Alerts, Analytics, and Resume Parser Migration
-- Run this script after executing migration_job_alerts_analytics_resume_parser.sql
-- to verify all tables, columns, and indexes were created successfully

\echo '========================================='
\echo 'Migration Verification Script'
\echo '========================================='
\echo ''

-- Check if new tables exist
\echo 'Checking new tables...'
SELECT 
    CASE 
        WHEN COUNT(*) = 6 THEN '✓ All 6 tables created successfully'
        ELSE '✗ Missing tables! Expected 6, found ' || COUNT(*)
    END AS table_check
FROM information_schema.tables 
WHERE table_schema = 'public' 
AND table_name IN (
    'notifications', 
    'job_alerts', 
    'notification_preferences', 
    'analytics_cache', 
    'parsed_resumes', 
    'job_processing_checkpoint'
);

\echo ''
\echo 'Table details:'
SELECT table_name, 
       (SELECT COUNT(*) FROM information_schema.columns WHERE table_name = t.table_name) as column_count
FROM information_schema.tables t
WHERE table_schema = 'public' 
AND table_name IN (
    'notifications', 
    'job_alerts', 
    'notification_preferences', 
    'analytics_cache', 
    'parsed_resumes', 
    'job_processing_checkpoint'
)
ORDER BY table_name;

\echo ''
\echo 'Checking employer_id column in jobs table...'
SELECT 
    CASE 
        WHEN COUNT(*) = 1 THEN '✓ employer_id column exists in jobs table'
        ELSE '✗ employer_id column missing from jobs table'
    END AS column_check
FROM information_schema.columns 
WHERE table_name = 'jobs' 
AND column_name = 'employer_id';

\echo ''
\echo 'Checking indexes...'
SELECT 
    COUNT(*) as index_count,
    CASE 
        WHEN COUNT(*) >= 18 THEN '✓ All performance indexes created'
        ELSE '⚠ Expected at least 18 indexes, found ' || COUNT(*)
    END AS index_check
FROM pg_indexes 
WHERE schemaname = 'public' 
AND (
    indexname LIKE 'idx_notifications_%' OR
    indexname LIKE 'idx_job_alerts_%' OR
    indexname LIKE 'idx_analytics_cache_%' OR
    indexname LIKE 'idx_parsed_resumes_%' OR
    indexname LIKE 'idx_applications_%' OR
    indexname LIKE 'idx_jobs_%' OR
    indexname LIKE 'idx_recommendations_%'
);

\echo ''
\echo 'Index details:'
SELECT indexname, tablename
FROM pg_indexes 
WHERE schemaname = 'public' 
AND (
    indexname LIKE 'idx_notifications_%' OR
    indexname LIKE 'idx_job_alerts_%' OR
    indexname LIKE 'idx_analytics_cache_%' OR
    indexname LIKE 'idx_parsed_resumes_%' OR
    indexname LIKE 'idx_applications_%' OR
    indexname LIKE 'idx_jobs_%' OR
    indexname LIKE 'idx_recommendations_%'
)
ORDER BY tablename, indexname;

\echo ''
\echo 'Checking job_processing_checkpoint initialization...'
SELECT 
    CASE 
        WHEN COUNT(*) = 1 THEN '✓ Checkpoint table initialized with single row'
        WHEN COUNT(*) = 0 THEN '✗ Checkpoint table is empty'
        ELSE '✗ Checkpoint table has multiple rows (should have exactly 1)'
    END AS checkpoint_check
FROM job_processing_checkpoint;

\echo ''
\echo 'Checking foreign key constraints...'
SELECT 
    COUNT(*) as fk_count,
    '✓ Foreign key constraints created' as fk_check
FROM information_schema.table_constraints 
WHERE constraint_type = 'FOREIGN KEY'
AND table_name IN (
    'notifications', 
    'job_alerts', 
    'notification_preferences', 
    'analytics_cache', 
    'parsed_resumes'
);

\echo ''
\echo '========================================='
\echo 'Verification Complete'
\echo '========================================='
