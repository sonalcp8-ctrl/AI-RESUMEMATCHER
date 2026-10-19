# Resume Matcher & Job Recommendation System
Java Swing desktop app + MySQL (JDBC) + optional C++ scoring module.

## What it does
A user registers, adds skills, uploads a PDF resume, and the app ranks jobs by **match percentage**:

    match % = (required skills the user has / total required skills of the job) x 100

Example: a job needs 10 skills, the user has 6 of them -> 60%.

Screens: Login/Register | Dashboard (job matches + Apply) | My Skills | My Applications | My Profile (resume upload)

## Setup (Windows, Eclipse or command line)
1. Install JDK 8 or newer and MySQL 8.
2. Open MySQL and run **schema.sql** (creates database `resume_matcher`, tables and 8 sample jobs).
3. Edit **db.properties** with your MySQL username and password.
4. Download MySQL Connector/J (https://dev.mysql.com/downloads/connector/j/) and copy the `.jar` into **lib/**.
5. Build and run:
   - Command line: `build.bat` then `run.bat`  (Linux/Mac: `./build.sh` then `./run.sh`)
   - Eclipse: File > New > Java Project, copy `src/` in, add the connector jar to the Build Path,
     run `resumematcher.Main`. Keep `db.properties` in the project root (the working directory).

## Optional: C++ scoring module
`cpp/matcher.cpp` does the same score calculation. If a compiled `cpp/matcher.exe` (or `cpp/matcher`) exists,
Java calls it through `ProcessBuilder`; if not, Java calculates the score itself, so the app works either way.

    g++ -O2 -o cpp/matcher.exe cpp/matcher.cpp      (MinGW / g++)

## Project structure
    src/resumematcher/Main.java          entry point
    src/resumematcher/ui/                Swing screens (LoginFrame, MainFrame, DashboardPanel, SkillsPanel,
                                         ApplicationsPanel, ProfilePanel)
    src/resumematcher/dao/Dao.java       all SQL (PreparedStatement everywhere)
    src/resumematcher/db/Database.java   JDBC connection from db.properties
    src/resumematcher/model/             User, Job
    src/resumematcher/util/Matcher.java  match score (calls C++ if available)
    src/resumematcher/util/Security.java password hashing (SHA-256 + random salt)
    cpp/matcher.cpp                      optional C++ scorer
    schema.sql                           tables + sample data

## Database tables
users, jobs, job_skills, user_skills, applications (foreign keys link them; UNIQUE keys stop duplicates).

## Known limitations (be honest about these in the interview)
- Matching is exact keyword matching: "JS" does not match "JavaScript".
- The PDF resume is stored, but skills are added manually; the PDF text is NOT parsed.
- Possible improvements: NLP (TF-IDF + cosine similarity), PDF parsing with Apache PDFBox,
  job-specific online assessment after shortlisting, then an interview step.
