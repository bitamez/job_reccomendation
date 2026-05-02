# Database Migration Guide

## Overview

This directory contains database schema files and migration scripts for the AI Job Recommendation System.

## Files

- `schema.sql` - Base database schema with core tables (users, jobs, applications, etc.)
- `migration_job_alerts_analytics_resume_parser.sql` - Migration script for job alerts, analytics, and resume parser features

## Running Migrations

### Option 1: Using psql Command Line

```bash
psql -U postgres -d job-reccomendation -f database/migration_job_alerts_analytics_resume_parser.sql
```

### Option 2: Using pgAdmin

1. Open pgAdmin and connect to the `job-reccomendation` database
2. Open the Query Tool (Tools > Query Tool)
3. Open the migration file: `database/migration_job_alerts_analytics_resume_parser.sql`
4. Execute the script (F5 or click Execute button)

### Option 3: Using Java MigrationRunner

```bash
# Compile and run the MigrationRunner utility
javac -cp "target/classes" src/main/java/com/mesi/jobai/util/MigrationRunner.java
java -cp "target/classes:lib/*" com.mesi.jobai.util.MigrationRunner database/migration_job_alerts_analytics_resume_parser.sql
```

## Migration: job_alerts_analytics_resume_parser

**Purpose**: Adds support for job alerts, analytics dashboard, and resume parser features

**Changes**:
- Adds `employer_id` column to `jobs` table
- Creates 6 new tables:
  - `notifications` - Stores all user notifications
  - `job_alerts` - Tracks job alerts for applicants
  - `notification_preferences` - User notification settings
  - `analytics_cache` - Cached analytics metrics
  - `parsed_resumes` - Resume upload audit trail
  - `job_processing_checkpoint` - Job alert monitoring state
- Adds 18 performance indexes for optimized queries

**Requirements**: 14.3, 15.3

**Safe to Re-run**: Yes, uses `IF NOT EXISTS` clauses

## Verifying Migration

After running the migration, verify the tables were created:

```sql
-- Check new tables exist
SELECT table_name 
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

-- Check indexes were created
SELECT indexname 
FROM pg_indexes 
WHERE schemaname = 'public' 
AND indexname LIKE 'idx_%';

-- Check employer_id column was added
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'jobs' 
AND column_name = 'employer_id';
```

## Rollback

If you need to rollback this migration:

```sql
-- Drop new tables
DROP TABLE IF EXISTS job_processing_checkpoint CASCADE;
DROP TABLE IF EXISTS parsed_resumes CASCADE;
DROP TABLE IF EXISTS analytics_cache CASCADE;
DROP TABLE IF EXISTS notification_preferences CASCADE;
DROP TABLE IF EXISTS job_alerts CASCADE;
DROP TABLE IF EXISTS notifications CASCADE;

-- Remove employer_id column
ALTER TABLE jobs DROP COLUMN IF EXISTS employer_id;

-- Drop indexes (they will be dropped automatically with tables)
```

## Database Connection

The application connects to PostgreSQL using these settings (configured in `DBConnection.java`):
- **Host**: localhost:5432
- **Database**: job-reccomendation
- **User**: postgres
- **Password**: (configured in DBConnection.java)

## Notes

- All migrations use `IF NOT EXISTS` clauses to be idempotent
- Foreign keys use `ON DELETE CASCADE` or `ON DELETE SET NULL` for referential integrity
- Indexes are created for all frequently queried columns
- The `job_processing_checkpoint` table is constrained to a single row
