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
-- Table structure for table `obaveza`
--

DROP TABLE IF EXISTS `obaveza`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `obaveza` (
  `idO` int NOT NULL AUTO_INCREMENT,
  `pocetak` datetime NOT NULL,
  `kraj` datetime NOT NULL,
  `mesto` varchar(45) NOT NULL,
  `idU` int NOT NULL,
  `idA` int DEFAULT NULL,
  PRIMARY KEY (`idO`),
  KEY `idU_idx` (`idU`),
  KEY `idAlarm_idx` (`idA`),
  CONSTRAINT `idAlarm` FOREIGN KEY (`idA`) REFERENCES `alarm` (`idA`) ON DELETE CASCADE,
  CONSTRAINT `idUser` FOREIGN KEY (`idU`) REFERENCES `user` (`idU`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `obaveza`
--

LOCK TABLES `obaveza` WRITE;
/*!40000 ALTER TABLE `obaveza` DISABLE KEYS */;
INSERT INTO `obaveza` VALUES (1,'2021-02-25 19:41:46','2021-02-25 19:46:46','Bezanijska kosa',1,NULL),(2,'2021-02-24 18:32:15','2021-02-24 18:37:15','Banovo brdo',1,NULL),(8,'2021-02-24 19:13:35','2021-02-24 19:18:35','Elektrotehnicki fakultet Beograd',1,NULL),(21,'2021-02-25 00:13:10','2021-02-25 00:18:11','Pozeska 30',1,36),(24,'2021-02-26 01:04:12','2021-02-26 01:09:12','Elektrotehnicki fakultet Beograd',1,NULL),(25,'2021-02-26 02:30:00','2021-02-26 03:00:00','Beograd na vodi',1,NULL);
/*!40000 ALTER TABLE `obaveza` ENABLE KEYS */;
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
