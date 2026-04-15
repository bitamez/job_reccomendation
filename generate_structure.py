import os
import shutil

base_dir = r"c:\Users\user\OneDrive\Desktop\job"

folders = [
    "src/main/java/com/mesi/jobai/config",
    "src/main/java/com/mesi/jobai/model",
    "src/main/java/com/mesi/jobai/dao",
    "src/main/java/com/mesi/jobai/service",
    "src/main/java/com/mesi/jobai/ui",
    "src/main/java/com/mesi/jobai/controller",
    "src/main/resources/styles",
    "src/main/resources/images",
    "database"
]

files = {
    "src/main/java/com/mesi/jobai/Main.java": "package com.mesi.jobai;\n\npublic class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Starting AI Job System...\");\n    }\n}\n",
    "src/main/java/com/mesi/jobai/config/DBConnection.java": "package com.mesi.jobai.config;\n\npublic class DBConnection {}\n",
    "src/main/java/com/mesi/jobai/model/User.java": "package com.mesi.jobai.model;\n\npublic class User {}\n",
    "src/main/java/com/mesi/jobai/model/Job.java": "package com.mesi.jobai.model;\n\npublic class Job {}\n",
    "src/main/java/com/mesi/jobai/model/Skill.java": "package com.mesi.jobai.model;\n\npublic class Skill {}\n",
    "src/main/java/com/mesi/jobai/model/Application.java": "package com.mesi.jobai.model;\n\npublic class Application {}\n",
    "src/main/java/com/mesi/jobai/model/Recommendation.java": "package com.mesi.jobai.model;\n\npublic class Recommendation {}\n",
    "src/main/java/com/mesi/jobai/dao/UserDAO.java": "package com.mesi.jobai.dao;\n\npublic class UserDAO {}\n",
    "src/main/java/com/mesi/jobai/dao/JobDAO.java": "package com.mesi.jobai.dao;\n\npublic class JobDAO {}\n",
    "src/main/java/com/mesi/jobai/dao/SkillDAO.java": "package com.mesi.jobai.dao;\n\npublic class SkillDAO {}\n",
    "src/main/java/com/mesi/jobai/dao/ApplicationDAO.java": "package com.mesi.jobai.dao;\n\npublic class ApplicationDAO {}\n",
    "src/main/java/com/mesi/jobai/dao/RecommendationDAO.java": "package com.mesi.jobai.dao;\n\npublic class RecommendationDAO {}\n",
    "src/main/java/com/mesi/jobai/service/AuthService.java": "package com.mesi.jobai.service;\n\npublic class AuthService {}\n",
    "src/main/java/com/mesi/jobai/service/JobService.java": "package com.mesi.jobai.service;\n\npublic class JobService {}\n",
    "src/main/java/com/mesi/jobai/service/RecommendationService.java": "package com.mesi.jobai.service;\n\npublic class RecommendationService {}\n",
    "src/main/java/com/mesi/jobai/service/AIService.java": "package com.mesi.jobai.service;\n\npublic class AIService {}\n",
    "src/main/java/com/mesi/jobai/ui/LoginUI.java": "package com.mesi.jobai.ui;\n\npublic class LoginUI {}\n",
    "src/main/java/com/mesi/jobai/ui/DashboardUI.java": "package com.mesi.jobai.ui;\n\npublic class DashboardUI {}\n",
    "src/main/java/com/mesi/jobai/ui/JobListUI.java": "package com.mesi.jobai.ui;\n\npublic class JobListUI {}\n",
    "src/main/java/com/mesi/jobai/ui/JobDetailsUI.java": "package com.mesi.jobai.ui;\n\npublic class JobDetailsUI {}\n",
    "src/main/java/com/mesi/jobai/ui/ApplicationsUI.java": "package com.mesi.jobai.ui;\n\npublic class ApplicationsUI {}\n",
    "src/main/java/com/mesi/jobai/controller/AuthController.java": "package com.mesi.jobai.controller;\n\npublic class AuthController {}\n",
    "src/main/java/com/mesi/jobai/controller/JobController.java": "package com.mesi.jobai.controller;\n\npublic class JobController {}\n",
    "src/main/java/com/mesi/jobai/controller/ApplicationController.java": "package com.mesi.jobai.controller;\n\npublic class ApplicationController {}\n",
    "src/main/java/com/mesi/jobai/controller/RecommendationController.java": "package com.mesi.jobai.controller;\n\npublic class RecommendationController {}\n",
    "README.md": "# AI Job Recommendation System\n"
}

# Create folders
for folder in folders:
    os.makedirs(os.path.join(base_dir, folder), exist_ok=True)

# Delete the old com/jobapp
old_pkg = os.path.join(base_dir, "src/main/java/com/jobapp")
if os.path.exists(old_pkg):
    shutil.rmtree(old_pkg)

# Move schema.sql if exists to database
old_schema = os.path.join(base_dir, "src/main/resources/schema.sql")
new_schema = os.path.join(base_dir, "database/schema.sql")
if os.path.exists(old_schema):
    shutil.move(old_schema, new_schema)

# Create files
for filepath, content in files.items():
    full_path = os.path.join(base_dir, filepath)
    if not os.path.exists(full_path):
        with open(full_path, "w", encoding="utf-8") as f:
            f.write(content)

print("Structure generated successfully.")
