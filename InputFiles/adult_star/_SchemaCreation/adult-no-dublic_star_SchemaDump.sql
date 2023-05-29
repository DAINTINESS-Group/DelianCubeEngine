CREATE DATABASE  IF NOT EXISTS `adult_no_dublic_star` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `adult_no_dublic_star`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win32 (AMD64)
--
-- Host: localhost    Database: adult_no_dublic
-- ------------------------------------------------------
-- Server version	5.7.10-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `adult`
--

DROP TABLE IF EXISTS `adult`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adult` (
  `id` int(11) NOT NULL DEFAULT '0',
  `age` int(11) NOT NULL,
  `work_class` varchar(50) NOT NULL,
  `education` varchar(50) NOT NULL,
  `marital_status` varchar(50) NOT NULL,
  `occupation` varchar(50) NOT NULL,
  `race` varchar(50) NOT NULL,
  `gender` varchar(50) NOT NULL,
  `hours_per_week` int(11) NOT NULL,
  `native_country` varchar(50) NOT NULL,
  `salary` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `level0` (`age`),
  KEY `level0_work_class` (`work_class`),
  KEY `level0_edu` (`education`),
  KEY `level0_marit` (`marital_status`),
  KEY `level0_occup` (`occupation`),
  KEY `level0_native_count` (`native_country`),
  KEY `level0_race` (`race`),
  KEY `level0_gender` (`gender`),
  CONSTRAINT `level0` FOREIGN KEY (`age`) REFERENCES `age` (`level0`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `level0_edu` FOREIGN KEY (`education`) REFERENCES `education` (`level0`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `level0_gender` FOREIGN KEY (`gender`) REFERENCES `gender` (`level0`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `level0_marit` FOREIGN KEY (`marital_status`) REFERENCES `marital_status` (`level0`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `level0_native_count` FOREIGN KEY (`native_country`) REFERENCES `native_country` (`level0`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `level0_occup` FOREIGN KEY (`occupation`) REFERENCES `occupation` (`level0`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `level0_race` FOREIGN KEY (`race`) REFERENCES `race` (`level0`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `level0_work_class` FOREIGN KEY (`work_class`) REFERENCES `work_class` (`level0`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `age`
--

DROP TABLE IF EXISTS `age`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `age` (
  `level0` int(11) NOT NULL,
  `level1` varchar(10) NOT NULL,
  `level2` varchar(10) NOT NULL,
  `level3` varchar(10) NOT NULL,
  `level4` varchar(10) NOT NULL,
  PRIMARY KEY (`level0`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `c1`
--

DROP TABLE IF EXISTS `c1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `c1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dim_0` varchar(20) DEFAULT NULL,
  `index_0` int(11) DEFAULT NULL,
  `parent1` int(11) DEFAULT NULL,
  `parent2` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `c2`
--

DROP TABLE IF EXISTS `c2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `c2` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dim_0` varchar(20) DEFAULT NULL,
  `index_0` int(11) DEFAULT NULL,
  `dim_1` varchar(20) DEFAULT NULL,
  `index_1` int(11) DEFAULT NULL,
  `parent1` int(11) DEFAULT NULL,
  `parent2` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indx_C2_1` (`parent1`),
  KEY `indx_C2_2` (`parent2`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `c3`
--

DROP TABLE IF EXISTS `c3`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `c3` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dim_0` varchar(20) DEFAULT NULL,
  `index_0` int(11) DEFAULT NULL,
  `dim_1` varchar(20) DEFAULT NULL,
  `index_1` int(11) DEFAULT NULL,
  `dim_2` varchar(20) DEFAULT NULL,
  `index_2` int(11) DEFAULT NULL,
  `parent1` int(11) DEFAULT NULL,
  `parent2` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `C_allDims_BT_indx` (`index_0`,`index_1`,`index_2`),
  KEY `indx_C3_1` (`parent1`),
  KEY `indx_C3_2` (`parent2`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `e1`
--

DROP TABLE IF EXISTS `e1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `e1` (
  `start` int(11) NOT NULL DEFAULT '0',
  `end` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`start`,`end`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `e2`
--

DROP TABLE IF EXISTS `e2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `e2` (
  `start` int(11) NOT NULL DEFAULT '0',
  `end` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`start`,`end`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `e3`
--

DROP TABLE IF EXISTS `e3`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `e3` (
  `start` int(11) NOT NULL DEFAULT '0',
  `end` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`start`,`end`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `education`
--

DROP TABLE IF EXISTS `education`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `education` (
  `level0` varchar(50) NOT NULL,
  `level1` varchar(50) NOT NULL,
  `level2` varchar(50) NOT NULL,
  `level3` varchar(50) NOT NULL,
  `level4` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`level0`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gender`
--

DROP TABLE IF EXISTS `gender`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gender` (
  `level0` varchar(50) NOT NULL,
  `level1` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`level0`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `marital_status`
--

DROP TABLE IF EXISTS `marital_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `marital_status` (
  `level0` varchar(50) NOT NULL,
  `level1` varchar(50) NOT NULL,
  `level2` varchar(50) NOT NULL,
  `level3` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`level0`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `native_country`
--

DROP TABLE IF EXISTS `native_country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `native_country` (
  `level0` varchar(50) NOT NULL,
  `level1` varchar(50) NOT NULL,
  `level2` varchar(50) NOT NULL,
  `level3` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`level0`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `occupation`
--

DROP TABLE IF EXISTS `occupation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `occupation` (
  `level0` varchar(50) NOT NULL,
  `level1` varchar(50) NOT NULL,
  `level2` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`level0`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `race`
--

DROP TABLE IF EXISTS `race`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `race` (
  `level0` varchar(50) NOT NULL,
  `level1` varchar(50) NOT NULL,
  `level2` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`level0`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `s1`
--

DROP TABLE IF EXISTS `s1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s1` (
  `id` int(11) NOT NULL DEFAULT '0',
  `dim_0` varchar(20) DEFAULT NULL,
  `index_0` int(11) DEFAULT NULL,
  `parent1` int(11) DEFAULT NULL,
  `parent2` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `s2`
--

DROP TABLE IF EXISTS `s2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s2` (
  `id` int(11) NOT NULL DEFAULT '0',
  `dim_0` varchar(20) DEFAULT NULL,
  `index_0` int(11) DEFAULT NULL,
  `dim_1` varchar(20) DEFAULT NULL,
  `index_1` int(11) DEFAULT NULL,
  `parent1` int(11) DEFAULT NULL,
  `parent2` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_class`
--

DROP TABLE IF EXISTS `work_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `work_class` (
  `level0` varchar(50) NOT NULL,
  `level1` varchar(50) NOT NULL,
  `level2` varchar(50) NOT NULL,
  `level3` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`level0`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'adult_no_dublic'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-22 12:30:14
