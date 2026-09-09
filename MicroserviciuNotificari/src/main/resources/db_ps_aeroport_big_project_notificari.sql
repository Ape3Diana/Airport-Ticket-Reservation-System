-- MySQL dump 10.13  Distrib 8.0.39, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: db_ps_aeroport_big_project_notificari
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
-- Table structure for table `notificare`
--

DROP TABLE IF EXISTS `notificare`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificare` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_utilizator` int NOT NULL,
  `tip_notificare` enum('EMAIL','SMS','WHATSAPP') NOT NULL,
  `subiect` varchar(255) DEFAULT NULL,
  `mesaj` varchar(255) NOT NULL,
  `status_trimitere` enum('TRIMIS','ESUAT','IN_ASTEPTARE') DEFAULT 'IN_ASTEPTARE',
  `data_creare` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificare`
--

LOCK TABLES `notificare` WRITE;
/*!40000 ALTER TABLE `notificare` DISABLE KEYS */;
INSERT INTO `notificare` VALUES (1,2,'EMAIL','Securitate Cont','Datele de autentificare ale contului dumneavoastră au fost modificate de către un Administrator.','TRIMIS','2026-05-17 10:30:24'),(2,2,'SMS','Alerta SMS','Datele de autentificare ale contului dumneavoastră au fost modificate de către un Administrator.','TRIMIS','2026-05-17 10:30:24'),(3,2,'WHATSAPP','Alerta WhatsApp','Datele de autentificare ale contului dumneavoastră au fost modificate de către un Administrator.','TRIMIS','2026-05-17 10:30:24'),(4,2,'EMAIL','Securitate Cont','Datele de autentificare ale contului dumneavoastră au fost modificate de către un Administrator.','TRIMIS','2026-05-17 10:32:52'),(5,2,'SMS','Alerta SMS','Datele de autentificare ale contului dumneavoastră au fost modificate de către un Administrator.','TRIMIS','2026-05-17 10:32:52'),(6,2,'WHATSAPP','Alerta WhatsApp','Datele de autentificare ale contului dumneavoastră au fost modificate de către un Administrator.','TRIMIS','2026-05-17 10:32:52'),(7,2,'EMAIL','Securitate Cont','Administratorul v-a modificat datele contului.\n\nS-au făcut următoarele modificări:\n• Nume: Maria Ionescu -> Maria Maria\n• Parolă: [ASCUNSĂ] -> [PAROLĂ NOUĂ]\n','TRIMIS','2026-05-17 10:37:20'),(8,2,'SMS','Alerta SMS','Administratorul v-a modificat datele contului.\n\nS-au făcut următoarele modificări:\n• Nume: Maria Ionescu -> Maria Maria\n• Parolă: [ASCUNSĂ] -> [PAROLĂ NOUĂ]\n','TRIMIS','2026-05-17 10:37:20'),(9,2,'WHATSAPP','Alerta WhatsApp','Administratorul v-a modificat datele contului.\n\nS-au făcut următoarele modificări:\n• Nume: Maria Ionescu -> Maria Maria\n• Parolă: [ASCUNSĂ] -> [PAROLĂ NOUĂ]\n','TRIMIS','2026-05-17 10:37:20'),(10,2,'EMAIL','Securitate Cont','Administratorul v-a modificat datele contului.\n\nS-au făcut următoarele modificări:\n• Nume: Maria Maria -> Maria Ionescu\n• Parolă: manager -> manager123\n','TRIMIS','2026-05-17 10:40:37'),(11,2,'SMS','Alerta SMS','Administratorul v-a modificat datele contului.\n\nS-au făcut următoarele modificări:\n• Nume: Maria Maria -> Maria Ionescu\n• Parolă: manager -> manager123\n','TRIMIS','2026-05-17 10:40:37'),(12,2,'WHATSAPP','Alerta WhatsApp','Administratorul v-a modificat datele contului.\n\nS-au făcut următoarele modificări:\n• Nume: Maria Maria -> Maria Ionescu\n• Parolă: manager -> manager123\n','TRIMIS','2026-05-17 10:40:37');
/*!40000 ALTER TABLE `notificare` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-17 13:56:04
