# 🎯 ResumeMatcher - Full Stack Web Application
### React + Java (Spring Boot) + MySQL

A modern Full-Stack web application that matches candidates' skills with job requirements, calculates real-time match percentages, enables resume PDF uploads, and allows one-click job applications.

---

## 🏗️ Architecture Overview

- **Frontend:** React 19 + Vite + Lucide Icons + React Router (Port `5173`)
- **Backend:** Java 21/24 + Spring Boot 3.4.x (REST API, Spring Data JPA, Hibernate) (Port `8080`)
- **Database:** MySQL 8.0 (Port `3307`, Database: `resume_matcher`)
- **File Storage:** Local secure resume uploads (`backend/uploads/`)

---

## ⚡ Quick Start (1-Click Run)

Simply double-click:
```cmd
start-all.bat
```
This script will:
1. Start the **Java Spring Boot Backend** on `http://localhost:8080`
2. Start the **React Frontend** on `http://localhost:5173`

Or you can run each service individually:
- Backend: Double-click `start-backend.bat`
- Frontend: Double-click `start-frontend.bat`

---

## 🔑 Demo Test Accounts (Ready in Database)

You can immediately log in with any of these sample users:

| Full Name | Email | Password |
|---|---|---|
| **Sonal Mishra** | `sonal@gmail.com` | `123456` |
| **Rahul Sharma** | `rahul@gmail.com` | `rahul123` |
| **Priya Singh** | `priya@gmail.com` | `priya123` |
| **Amit Kumar** | `amit@gmail.com` | `amit456` |
| **Neha Verma** | `neha@gmail.com` | `neha789` |

*(You can also click the quick-login buttons on the login screen or register a new account!)*

---

## 📋 Manual Step-by-Step Setup (If Needed)

### Step 1: MySQL Database
- Ensure MySQL service is running on port **3307** (or port 3306).
- Database name: `resume_matcher`
- Username: `root`
- Password: *(empty / blank by default)*
- If your MySQL is running on port **3306** or uses a password, simply update `backend/src/main/resources/application.properties`:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/resume_matcher?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true
  spring.datasource.username=root
  spring.datasource.password=YOUR_PASSWORD_HERE
  ```

### Step 2: Start Java Spring Boot Backend
Open PowerShell / Command Prompt:
```powershell
cd c:\Users\HP\Downloads\ResumeMatcher\backend
& "C:\Users\HP\apache-maven-3.9.6\bin\mvn.cmd" spring-boot:run
```
Backend will be live at `http://localhost:8080/api`

### Step 3: Start React Frontend
Open another terminal:
```powershell
cd c:\Users\HP\Downloads\ResumeMatcher\frontend
npm run dev
```
Open your browser at `http://localhost:5173`

---

## 🚀 Key Features

1. **Skill Matching Algorithm:**
   $$\text{Match \%} = \left(\frac{\text{Skills user has for this job}}{\text{Total required skills of job}}\right) \times 100$$
2. **Interactive Dashboard:**
   - Real-time job filtering by keyword, location, job type (Full-Time, Remote, Internship), and minimum match score slider.
   - Color-coded match badges:
     - 🟢 **≥ 75%** High Match
     - 🟡 **50% - 74%** Medium Match
     - ⚪ **< 50%** Low Match
   - Matched skills tagged with green checkmarks, missing skills clearly highlighted.
3. **Skill Management:**
   - Add skills from categorized database library (Programming, Framework, Database, Tools, AI/ML, etc.).
   - Create custom skills.
   - Delete skills with instant dynamic recalculation of all job matches!
4. **Resume Upload & Download:**
   - Upload PDF/DOCX resume documents directly attached to user profiles.
   - One-click viewing and downloading of uploaded resumes.
5. **Job Application Tracker:**
   - One-click application to any job.
   - Status tracker: Applied, Under Review, Shortlisted, Rejected.

---

## 📡 REST API Reference

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Login user |
| `GET` | `/api/auth/me/{userId}` | Get profile details |
| `PUT` | `/api/auth/profile/{userId}` | Update profile details |
| `GET` | `/api/jobs?userId={id}` | Get jobs with calculated match scores |
| `GET` | `/api/jobs/{id}?userId={id}` | Get single job details |
| `GET` | `/api/skills` | List all available skills |
| `GET` | `/api/skills/user/{userId}` | Get user's skills |
| `POST` | `/api/skills/user/{userId}/add` | Add skill to user profile |
| `DELETE` | `/api/skills/user/{userId}/remove/{skillId}` | Remove skill from profile |
| `POST` | `/api/applications/apply` | Apply to a job |
| `GET` | `/api/applications/user/{userId}` | Get user's applied jobs |
| `POST` | `/api/resume/upload/{userId}` | Upload PDF resume |
| `GET` | `/api/resume/download/{userId}` | Download/view resume |
