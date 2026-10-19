@echo off
REM Compiles the project into the "out" folder
if not exist out mkdir out
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -d out @sources.txt
if %errorlevel% neq 0 ( echo Build FAILED & exit /b 1 )
del sources.txt
echo Build successful. Now run run.bat
