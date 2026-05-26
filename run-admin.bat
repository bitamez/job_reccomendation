@echo off
echo Starting Admin Panel for AI Job Recommendation System...

REM Set local environment variables for the downloaded tools
set JAVA_HOME=C:\Users\user\Downloads\OpenJDK17U-jdk_x64_windows_hotspot_17.0.18_8\jdk-17.0.18+8
set MAVEN_HOME=C:\Users\user\Downloads\apache-maven-3.9.12-bin\apache-maven-3.9.12
set PATH=%MAVEN_HOME%\bin;%JAVA_HOME%\bin;%PATH%

echo Compiling project...
mvn compile

echo Copying dependencies...
mvn dependency:copy-dependencies

echo Launching Admin Panel...
java -cp "target/classes;target/dependency/*" com.mesi.jobai.AdminMain

pause