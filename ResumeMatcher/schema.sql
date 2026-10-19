-- Resume Matcher database (MySQL 8.x)
CREATE DATABASE IF NOT EXISTS resume_matcher;
USE resume_matcher;

CREATE TABLE IF NOT EXISTS users (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    salt          VARCHAR(40)  NOT NULL,
    resume_path   VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS jobs (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    title    VARCHAR(100) NOT NULL,
    company  VARCHAR(100) NOT NULL,
    location VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS job_skills (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    job_id INT NOT NULL,
    skill  VARCHAR(50) NOT NULL,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    UNIQUE KEY uq_job_skill (job_id, skill)
);

CREATE TABLE IF NOT EXISTS user_skills (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    skill   VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uq_user_skill (user_id, skill)
);

CREATE TABLE IF NOT EXISTS applications (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT NOT NULL,
    job_id      INT NOT NULL,
    match_score INT NOT NULL,
    status      VARCHAR(30) NOT NULL DEFAULT 'Applied',
    applied_on  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (job_id)  REFERENCES jobs(id)  ON DELETE CASCADE,
    UNIQUE KEY uq_application (user_id, job_id)
);

-- Sample jobs
INSERT INTO jobs (title, company, location) VALUES
 ('Java Developer',        'Capgemini',   'Pune'),
 ('Full Stack Developer',  'TechNova',    'Bengaluru'),
 ('Python Developer',      'DataWorks',   'Hyderabad'),
 ('Frontend Developer',    'PixelHub',    'Noida'),
 ('Backend Engineer',      'CloudSoft',   'Chennai'),
 ('C++ Software Engineer', 'SysLogic',    'Pune'),
 ('Database Administrator','InfoBase',    'Mumbai'),
 ('Software Trainee',      'NextGen IT',  'Gurugram');

INSERT INTO job_skills (job_id, skill) VALUES
 (1,'java'),(1,'oop'),(1,'mysql'),(1,'jdbc'),(1,'git'),
 (2,'html'),(2,'css'),(2,'javascript'),(2,'react'),(2,'node.js'),(2,'mysql'),
 (3,'python'),(3,'sql'),(3,'flask'),(3,'git'),
 (4,'html'),(4,'css'),(4,'javascript'),(4,'react'),
 (5,'java'),(5,'mysql'),(5,'rest api'),(5,'git'),(5,'spring'),
 (6,'c++'),(6,'data structures'),(6,'oop'),(6,'stl'),
 (7,'mysql'),(7,'sql'),(7,'normalization'),
 (8,'java'),(8,'c++'),(8,'data structures'),(8,'sql');
