@echo off
title ResumeMatcher - Full Stack Launcher
echo ========================================================
echo   Launching ResumeMatcher (Java + MySQL + React)
echo ========================================================
echo 1. Starting Java Spring Boot Backend in a new window...
start "ResumeMatcher Backend" cmd /c "%~dp0start-backend.bat"

echo 2. Waiting 3 seconds for Backend initialization...
timeout /t 3 /nobreak >nul

echo 3. Starting React Frontend in a new window...
start "ResumeMatcher Frontend" cmd /c "%~dp0start-frontend.bat"

echo.
echo Both servers are launching!
echo Backend:  http://localhost:8080/api
echo Frontend: http://localhost:5173
echo.
echo Press any key to exit this launcher window.
pause >nul
