-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: pkdd99
-- ------------------------------------------------------
-- Server version	5.7.21-log

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `account_id` int(11) NOT NULL,
  `district_id` int(11) NOT NULL,
  `frequency` varchar(100) NOT NULL,
  `date` int(11) NOT NULL,
  `district_name` varchar(100) NOT NULL,
  `region` varchar(100) NOT NULL,
  `Inhabitants` int(11) NOT NULL,
  `Municipalities499` int(11) NOT NULL,
  `Municipalities500_1999` int(11) NOT NULL,
  `Municipalities2000_9999` int(11) NOT NULL,
  `Municipalities10000` int(11) NOT NULL,
  `Cities` int(11) NOT NULL,
  `Ratio_of_urban _Inh` double NOT NULL,
  `Avg_Salary` int(11) NOT NULL,
  `Unemploymant_Rate95` double NOT NULL,
  `Unemploymant_Rate96` double NOT NULL,
  `Enterpreuners` int(11) NOT NULL,
  `Commited_Crimes95` int(11) NOT NULL,
  `Commited_Crimes96` int(11) NOT NULL,
  `All` varchar(5) NOT NULL,
  PRIMARY KEY (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `date`
--

DROP TABLE IF EXISTS `date`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `date` (
  `SK_Day` int(11) NOT NULL,
  `Day` date NOT NULL,
  `Month` varchar(45) NOT NULL,
  `Year` int(11) NOT NULL,
  `All` varchar(5) NOT NULL,
  PRIMARY KEY (`SK_Day`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `loan`
--

DROP TABLE IF EXISTS `loan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `loan` (
  `loan_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `date` int(11) NOT NULL,
  `status` varchar(45) NOT NULL,
  `amount` int(11) NOT NULL,
  `duration` int(11) NOT NULL,
  `payments` double NOT NULL,
  PRIMARY KEY (`loan_id`),
  KEY `account_fk_idx` (`account_id`),
  KEY `date_fk_idx` (`date`),
  KEY `status_fk_idx` (`status`),
  CONSTRAINT `account_fk` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `date_fk` FOREIGN KEY (`date`) REFERENCES `date` (`SK_Day`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `status_fk` FOREIGN KEY (`status`) REFERENCES `status` (`status`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `bank_to` varchar(15) NOT NULL,
  `account_to` int(11) NOT NULL,
  `amount` double NOT NULL,
  `Reason` varchar(50) NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `account_fk2_idx` (`account_id`),
  KEY `reason_fk_idx` (`Reason`),
  CONSTRAINT `account_fk2` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `reason_fk` FOREIGN KEY (`Reason`) REFERENCES `payment_reason` (`reason`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment_reason`
--

DROP TABLE IF EXISTS `payment_reason`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_reason` (
  `reason` varchar(50) NOT NULL,
  `All` varchar(5) NOT NULL,
  PRIMARY KEY (`reason`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status` (
  `status` varchar(45) NOT NULL,
  `All` varchar(5) NOT NULL,
  PRIMARY KEY (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-22 19:16:15
