@echo off
echo Starting AI Job Recommendation System...

REM Set local environment variables for the downloaded tools
set JAVA_HOME=C:\Program Files\Java\jdk-24
set MAVEN_HOME=C:\tools\maven\apache-maven-3.9.6
set PATH=%MAVEN_HOME%\bin;%JAVA_HOME%\bin;%PATH%

echo Compiling and running via Maven...
mvn clean javafx:run

pause
