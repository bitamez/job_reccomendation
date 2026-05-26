@echo off
echo Starting AI Job Recommendation System - Unified Email Login (Swing)...

REM Set local environment variables for the downloaded tools
set JAVA_HOME=C:\Users\user\Downloads\OpenJDK17U-jdk_x64_windows_hotspot_17.0.18_8\jdk-17.0.18+8
set MAVEN_HOME=C:\Users\user\Downloads\apache-maven-3.9.12-bin\apache-maven-3.9.12
set PATH=%MAVEN_HOME%\bin;%JAVA_HOME%\bin;%PATH%

echo.
echo ========================================
echo   AI Job Recommendation System
echo   Unified Email-Based Login (Swing)
echo ========================================
echo.
echo Available Login Credentials:
echo.
echo ADMIN:
echo   Email: admin@jobai.com
echo   Password: admin123
echo.
echo USERS (Applicants/Employers):
echo   Use any email from database
echo   Password: password123
echo.
echo ========================================
echo.

echo Launching Swing application...
mvn exec:java

pause
