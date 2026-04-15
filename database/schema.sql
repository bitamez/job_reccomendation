DROP TABLE IF EXISTS recommendations CASCADE;
DROP TABLE IF EXISTS applications CASCADE;
DROP TABLE IF EXISTS job_skills CASCADE;
DROP TABLE IF EXISTS jobs CASCADE;
DROP TABLE IF EXISTS companies CASCADE;
DROP TABLE IF EXISTS user_skills CASCADE;
DROP TABLE IF EXISTS skills CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    full_name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password TEXT,
    location VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE skills (
    skill_id SERIAL PRIMARY KEY,
    skill_name VARCHAR(100) UNIQUE
);

CREATE TABLE user_skills (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE,
    skill_id INT REFERENCES skills(skill_id) ON DELETE CASCADE,
    level VARCHAR(50) -- Beginner, Intermediate, Expert
);

CREATE TABLE companies (
    company_id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    location VARCHAR(100),
    description TEXT
); 

CREATE TABLE jobs (
    job_id SERIAL PRIMARY KEY,
    company_id INT REFERENCES companies(company_id) ON DELETE CASCADE,
    title VARCHAR(100),
    description TEXT,
    location VARCHAR(100),
    salary_min INT,
    salary_max INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 

CREATE TABLE job_skills (
    id SERIAL PRIMARY KEY,
    job_id INT REFERENCES jobs(job_id) ON DELETE CASCADE,
    skill_id INT REFERENCES skills(skill_id) ON DELETE CASCADE,
    importance INT -- 1 to 5
); 

CREATE TABLE applications (
    application_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    job_id INT REFERENCES jobs(job_id),
    status VARCHAR(50) DEFAULT 'pending',
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE recommendations (
    rec_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    job_id INT REFERENCES jobs(job_id),
    match_score FLOAT, -- AI score (0-1 or %)
    reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
