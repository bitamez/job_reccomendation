@echo off
echo Starting AI Job Recommendation System...

REM Set local environment variables for the downloaded tools
set JAVA_HOME=C:\Users\user\Downloads\OpenJDK17U-jdk_x64_windows_hotspot_17.0.18_8\jdk-17.0.18+8
set MAVEN_HOME=C:\Users\user\Downloads\apache-maven-3.9.12-bin\apache-maven-3.9.12
set PATH=%MAVEN_HOME%\bin;%JAVA_HOME%\bin;%PATH%

echo Compiling and running via Maven...
mvn clean javafx:run

pause
