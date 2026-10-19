@echo off
title ResumeMatcher - Spring Boot Java Backend (Port 8080)
cd /d "%~dp0backend"
echo ========================================================
echo   Starting ResumeMatcher Java Spring Boot REST API
echo ========================================================
echo Running on http://localhost:8080
echo Database: MySQL (port 3307 / resume_matcher)
echo.

set PATH=%PATH%;C:\Users\HP\apache-maven-3.9.6\bin

if exist "target\backend-1.0.0.jar" (
    java -jar target\backend-1.0.0.jar
) else (
    mvn spring-boot:run
)

pause
