-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: smarthouse
-- ------------------------------------------------------
-- Server version	8.0.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `pesma`
--

DROP TABLE IF EXISTS `pesma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pesma` (
  `idP` int NOT NULL AUTO_INCREMENT,
  `naziv` varchar(45) NOT NULL,
  `link` varchar(45) NOT NULL,
  PRIMARY KEY (`idP`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pesma`
--

LOCK TABLES `pesma` WRITE;
/*!40000 ALTER TABLE `pesma` DISABLE KEYS */;
INSERT INTO `pesma` VALUES (1,'Survivors','https://www.youtube.com/watch?v=72Bmf3wUbP8'),(2,'Monument','https://www.youtube.com/watch?v=8UHZBmJechU'),(4,'Sandstorm','https://www.youtube.com/watch?v=786uJ-MjKS4'),(8,'Dancing with your ghost','https://www.youtube.com/watch?v=Qzc_aX8c8g4'),(9,'defaultRing','https://www.youtube.com/watch?v=zud9LiIS7IQ'),(10,'Monument Keiino','https://www.youtube.com/watch?v=k2GhmQg-pLU'),(11,'Jelena Karleusa Insomnija','https://www.youtube.com/watch?v=MhZToS11TPU'),(12,'Insomnija','https://www.youtube.com/watch?v=MhZToS11TPU'),(13,'Tako lako Dzenan','https://www.youtube.com/watch?v=0ZkNDMzk-t4'),(14,'Miljacka','https://www.youtube.com/watch?v=PlTBOzsABKU'),(15,'best song ever','https://www.youtube.com/watch?v=o_v9MY_FMcw'),(16,'Neka pesma','https://www.youtube.com/watch?v=MsLSDaiX6mY'),(17,'Sreca','https://www.youtube.com/watch?v=BuVnUTW-0ps');
/*!40000 ALTER TABLE `pesma` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-02-26  1:28:18
