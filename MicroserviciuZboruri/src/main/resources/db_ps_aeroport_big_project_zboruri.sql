-- MySQL dump 10.13  Distrib 8.0.39, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: db_ps_aeroport_big_project_zboruri
-- ------------------------------------------------------
-- Server version	8.0.39

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
-- Table structure for table `aeroport`
--

DROP TABLE IF EXISTS `aeroport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aeroport` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nume` varchar(100) NOT NULL,
  `oras` varchar(100) NOT NULL,
  `tara` varchar(100) NOT NULL,
  `cod_iata` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cod_iata` (`cod_iata`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aeroport`
--

LOCK TABLES `aeroport` WRITE;
/*!40000 ALTER TABLE `aeroport` DISABLE KEYS */;
INSERT INTO `aeroport` VALUES (1,'Henri Coanda','Bucuresti','Romania','OTP'),(2,'Avram Iancu','Cluj-Napoca','Romania','CLJ'),(3,'Heathrow','Londra','Marea Britanie','LHR'),(4,'Charles de Gaulle','Paris','Franta','CDG'),(5,'Frankfurt Airport','Frankfurt','Germania','FRA'),(6,'Leonardo da Vinci','Roma','Italia','FCO'),(7,'Barajas','Madrid','Spania','MAD');
/*!40000 ALTER TABLE `aeroport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zbor`
--

DROP TABLE IF EXISTS `zbor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zbor` (
  `id` int NOT NULL AUTO_INCREMENT,
  `numar_zbor` varchar(20) NOT NULL,
  `id_aeroport_plecare` int NOT NULL,
  `id_aeroport_sosire` int NOT NULL,
  `ora_decolare` datetime NOT NULL,
  `ora_aterizare` datetime NOT NULL,
  `pret_bilet` decimal(10,2) NOT NULL,
  `locuri_disponibile` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_zbor_data` (`numar_zbor`,`ora_decolare`),
  KEY `fk_aeroport_plecare` (`id_aeroport_plecare`),
  KEY `fk_aeroport_sosire` (`id_aeroport_sosire`),
  CONSTRAINT `fk_aeroport_plecare` FOREIGN KEY (`id_aeroport_plecare`) REFERENCES `aeroport` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_aeroport_sosire` FOREIGN KEY (`id_aeroport_sosire`) REFERENCES `aeroport` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chk_max_locuri` CHECK (((`locuri_disponibile` <= 90) and (`locuri_disponibile` >= 0)))
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zbor`
--

LOCK TABLES `zbor` WRITE;
/*!40000 ALTER TABLE `zbor` DISABLE KEYS */;
INSERT INTO `zbor` VALUES (1,'RO301',1,4,'2026-06-01 08:00:00','2026-06-01 10:30:00',200.00,80),(2,'W63301',2,4,'2026-06-02 09:15:00','2026-06-02 11:00:00',100.00,75),(3,'BA102',1,3,'2026-06-03 14:00:00','2026-06-03 15:30:00',300.00,0),(4,'LH142',5,3,'2026-06-04 11:00:00','2026-06-04 12:45:00',250.00,86),(5,'RO505',4,1,'2026-06-05 18:00:00','2026-06-05 21:00:00',150.00,90),(6,'AZ220',3,6,'2026-06-06 07:30:00','2026-06-06 10:00:00',500.00,89),(7,'IB303',1,7,'2026-06-07 19:20:00','2026-06-07 22:15:00',120.00,1),(8,'RO999',2,5,'2026-06-08 13:00:00','2026-06-08 14:10:00',80.00,45),(9,'W6777',6,7,'2026-06-09 10:00:00','2026-06-09 12:30:00',90.00,78),(10,'RO202',5,4,'2026-06-10 15:00:00','2026-06-10 16:20:00',130.00,45),(11,'RO309',1,4,'2026-06-01 14:30:00','2026-06-01 17:00:00',220.00,40),(12,'AF112',1,4,'2026-06-01 20:00:00','2026-06-01 22:30:00',180.00,70),(13,'RO411',1,7,'2026-06-07 06:45:00','2026-06-07 09:40:00',140.00,5),(14,'LH882',2,5,'2026-06-08 18:00:00','2026-06-08 19:10:00',95.00,80),(15,'RO305',1,3,'2026-04-15 18:45:00','2026-04-15 20:30:00',195.00,78);
/*!40000 ALTER TABLE `zbor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-17 13:56:49
