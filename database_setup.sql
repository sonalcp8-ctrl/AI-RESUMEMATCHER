-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: resume_matcher
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `applications`
--

DROP TABLE IF EXISTS `applications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `applications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `job_id` int DEFAULT NULL,
  `match_score` int DEFAULT '0',
  `status` varchar(50) DEFAULT NULL,
  `applied_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_app` (`user_id`,`job_id`),
  KEY `job_id` (`job_id`),
  CONSTRAINT `applications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `applications_ibfk_2` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applications`
--

LOCK TABLES `applications` WRITE;
/*!40000 ALTER TABLE `applications` DISABLE KEYS */;
INSERT INTO `applications` VALUES (1,1,1,80,'Applied','2026-10-19 02:56:59');
/*!40000 ALTER TABLE `applications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_skills`
--

DROP TABLE IF EXISTS `job_skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_skills` (
  `job_id` int NOT NULL,
  `skill_id` int NOT NULL,
  PRIMARY KEY (`job_id`,`skill_id`),
  KEY `skill_id` (`skill_id`),
  CONSTRAINT `job_skills_ibfk_1` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`id`) ON DELETE CASCADE,
  CONSTRAINT `job_skills_ibfk_2` FOREIGN KEY (`skill_id`) REFERENCES `skills` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_skills`
--

LOCK TABLES `job_skills` WRITE;
/*!40000 ALTER TABLE `job_skills` DISABLE KEYS */;
INSERT INTO `job_skills` VALUES (1,1),(4,1),(9,1),(3,2),(6,2),(2,4),(7,4),(2,5),(7,5),(2,6),(7,6),(2,7),(7,7),(1,8),(1,9),(2,10),(1,11),(3,11),(6,11),(8,11),(8,12),(8,13),(1,14),(2,14),(4,14),(5,14),(8,14),(9,14),(5,15),(10,15),(5,16),(10,16),(3,17),(6,17),(3,18),(6,19),(4,20),(9,22),(5,23),(10,23),(10,24),(10,25);
/*!40000 ALTER TABLE `job_skills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobs`
--

DROP TABLE IF EXISTS `jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jobs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(150) NOT NULL,
  `company` varchar(100) NOT NULL,
  `location` varchar(100) DEFAULT NULL,
  `job_type` varchar(50) DEFAULT NULL,
  `salary_range` varchar(50) DEFAULT NULL,
  `description` text,
  `experience_req` varchar(50) DEFAULT NULL,
  `posted_date` date DEFAULT (curdate()),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobs`
--

LOCK TABLES `jobs` WRITE;
/*!40000 ALTER TABLE `jobs` DISABLE KEYS */;
INSERT INTO `jobs` VALUES (1,'Java Backend Developer','TechCorp India','Bengaluru','Full-Time','6-12 LPA','Develop robust backend services using Java and Spring Boot with MySQL.','1-3 years','2026-10-19'),(2,'Full Stack Web Developer','WebWorks Pvt Ltd','Remote','Remote','5-10 LPA','Build and maintain web applications using React and Node.js.','0-2 years','2026-10-19'),(3,'Data Science Intern','DataHub Solutions','Hyderabad','Internship','15-25K/month','Work on machine learning models and data pipelines using Python.','Fresher','2026-10-19'),(4,'Android App Developer','AppNation','Pune','Full-Time','4-8 LPA','Design and develop Android mobile applications.','1-2 years','2026-10-19'),(5,'DevOps Engineer','CloudBase Tech','Noida','Full-Time','8-15 LPA','Manage CI/CD pipelines, Docker containers and AWS infrastructure.','2-4 years','2026-10-19'),(6,'Machine Learning Engineer','AI Ventures','Remote','Remote','10-20 LPA','Build and deploy ML models for production environments.','2-5 years','2026-10-19'),(7,'Frontend Developer','PixelCraft Studio','Mumbai','Full-Time','4-9 LPA','Create responsive UIs with HTML, CSS, JavaScript and React.','0-3 years','2026-10-19'),(8,'Database Administrator','DataSafe Corp','Chennai','Full-Time','5-10 LPA','Manage MySQL and PostgreSQL databases and optimize queries.','2-4 years','2026-10-19'),(9,'Flutter Mobile Developer','MobiFirst','Bengaluru','Full-Time','6-11 LPA','Develop cross-platform mobile apps using Flutter framework.','1-3 years','2026-10-19'),(10,'Cloud Solutions Architect','SkyNet IT','Gurugram','Full-Time','18-30 LPA','Design scalable cloud architectures on AWS and Azure platforms.','5+ years','2026-10-19');
/*!40000 ALTER TABLE `jobs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skills`
--

DROP TABLE IF EXISTS `skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `skills` (
  `id` int NOT NULL AUTO_INCREMENT,
  `skill_name` varchar(100) NOT NULL,
  `category` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `skill_name` (`skill_name`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skills`
--

LOCK TABLES `skills` WRITE;
/*!40000 ALTER TABLE `skills` DISABLE KEYS */;
INSERT INTO `skills` VALUES (1,'Java','Programming'),(2,'Python','Programming'),(3,'C++','Programming'),(4,'JavaScript','Web'),(5,'HTML','Web'),(6,'CSS','Web'),(7,'React','Web'),(8,'Spring Boot','Framework'),(9,'Hibernate','Framework'),(10,'Node.js','Framework'),(11,'MySQL','Database'),(12,'MongoDB','Database'),(13,'PostgreSQL','Database'),(14,'Git','Tools'),(15,'Docker','Tools'),(16,'Linux','Tools'),(17,'Machine Learning','AI/ML'),(18,'Data Science','AI/ML'),(19,'Deep Learning','AI/ML'),(20,'Android','Mobile'),(21,'Swift','Mobile'),(22,'Flutter','Mobile'),(23,'AWS','Cloud'),(24,'Azure','Cloud'),(25,'DevOps','Cloud');
/*!40000 ALTER TABLE `skills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_skills`
--

DROP TABLE IF EXISTS `user_skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_skills` (
  `user_id` int NOT NULL,
  `skill_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`skill_id`),
  KEY `skill_id` (`skill_id`),
  CONSTRAINT `user_skills_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_skills_ibfk_2` FOREIGN KEY (`skill_id`) REFERENCES `skills` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_skills`
--

LOCK TABLES `user_skills` WRITE;
/*!40000 ALTER TABLE `user_skills` DISABLE KEYS */;
INSERT INTO `user_skills` VALUES (1,1),(4,1),(2,2),(3,4),(3,5),(3,6),(3,7),(1,8),(1,11),(2,11),(1,14),(4,14),(5,15),(5,16),(2,17),(2,18),(4,20),(4,22),(5,23),(5,25);
/*!40000 ALTER TABLE `user_skills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `resume_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Sonal Mishra','sonal@gmail.com','123456','9876543210','2026-10-19 06:04:24',NULL),(2,'Rahul Sharma','rahul@gmail.com','rahul123','9812345678','2026-10-19 06:04:24',NULL),(3,'Priya Singh','priya@gmail.com','priya123','9834567890','2026-10-19 06:04:24',NULL),(4,'Amit Kumar','amit@gmail.com','amit456','9856781234','2026-10-19 06:04:24',NULL),(5,'Neha Verma','neha@gmail.com','neha789','9867891234','2026-10-19 06:04:24',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-19 14:04:00
