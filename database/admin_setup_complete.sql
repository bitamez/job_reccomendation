-- Complete Admin Setup for Job AI System (PostgreSQL)
-- This script works with your existing schema and adds admin functionality

-- 1. Create admins table
CREATE TABLE IF NOT EXISTS admins (
    admin_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Create function for updated_at trigger
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 3. Create trigger for updated_at
DROP TRIGGER IF EXISTS update_admins_updated_at ON admins;
CREATE TRIGGER update_admins_updated_at 
    BEFORE UPDATE ON admins 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- 4. Add role column to users table if it doesn't exist
ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(50) DEFAULT 'APPLICANT';

-- 5. Add company column to jobs table for compatibility
ALTER TABLE jobs ADD COLUMN IF NOT EXISTS company VARCHAR(100);

-- 6. Update company column from companies table
UPDATE jobs 
SET company = c.name 
FROM companies c 
WHERE jobs.company_id = c.company_id 
AND jobs.company IS NULL;

-- 7. Insert default admin user (password: admin123)
INSERT INTO admins (username, password, email, full_name) VALUES 
('admin', 'admin123', 'admin@jobai.com', 'System Administrator')
ON CONFLICT (username) DO NOTHING;

-- 8. Add indexes for better performance
CREATE INDEX IF NOT EXISTS idx_admins_username ON admins(username);
CREATE INDEX IF NOT EXISTS idx_admins_email ON admins(email);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);

-- 9. Insert some sample data for testing (optional)
-- Sample companies
INSERT INTO companies (name, location, description) VALUES 
('TechCorp', 'San Francisco', 'Leading technology company'),
('DataSoft', 'New York', 'Data analytics solutions'),
('WebDev Inc', 'Austin', 'Web development services')
ON CONFLICT DO NOTHING;

-- Sample skills
INSERT INTO skills (skill_name) VALUES 
('Java'), ('Python'), ('JavaScript'), ('React'), ('Node.js'),
('PostgreSQL'), ('MySQL'), ('Git'), ('Docker'), ('AWS')
ON CONFLICT (skill_name) DO NOTHING;

-- Sample users with roles
INSERT INTO users (full_name, email, password, role) VALUES 
('John Employer', 'employer@test.com', 'password123', 'EMPLOYER'),
('Jane Applicant', 'applicant@test.com', 'password123', 'APPLICANT'),
('Bob Manager', 'manager@test.com', 'password123', 'EMPLOYER')
ON CONFLICT (email) DO NOTHING;

-- Verify setup
SELECT 'Admin table created successfully' as status;
SELECT 'Total admins: ' || COUNT(*) as admin_count FROM admins;
SELECT 'Total users: ' || COUNT(*) as user_count FROM users;
SELECT 'Total companies: ' || COUNT(*) as company_count FROM companies;
SELECT 'Total skills: ' || COUNT(*) as skill_count FROM skills;