CREATE DATABASE  IF NOT EXISTS `token_service` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `token_service`;
-- MySQL dump 10.13  Distrib 5.7.9, for linux-glibc2.5 (x86_64)
--
-- Host: localhost    Database: token_service
-- ------------------------------------------------------
-- Server version	5.6.30-0ubuntu0.15.10.1

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
-- Table structure for table `tstevent`
--

DROP TABLE IF EXISTS `tstevent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tstevent` (
  `eventdid` int(11) NOT NULL AUTO_INCREMENT,
  `jobname` varchar(200) NOT NULL,
  `createdtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `text` varchar(400) DEFAULT NULL,
  `event` varchar(100) NOT NULL,
  `status` varchar(100) NOT NULL,
  PRIMARY KEY (`eventdid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tstevent`
--

LOCK TABLES `tstevent` WRITE;
/*!40000 ALTER TABLE `tstevent` DISABLE KEYS */;
/*!40000 ALTER TABLE `tstevent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tsttoken`
--

DROP TABLE IF EXISTS `tsttoken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tsttoken` (
  `tokendid` int(11) NOT NULL AUTO_INCREMENT,
  `tsxwhodid` int(11) NOT NULL,
  `tokenauth` varchar(300) NOT NULL,
  `tokenvalidity` int(11) NOT NULL,
  `accesstoken` varchar(200) DEFAULT NULL,
  `refreshtoken` varchar(200) DEFAULT NULL,
  `parenttokendid` int(11) DEFAULT NULL,
  `isvalid` BIT(1) NOT NULL,
  `uc` int(11) DEFAULT '0',
  `createdtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT `pk01tsttoken` PRIMARY KEY (`tokendid`),
  CONSTRAINT `fk01tsttoken` FOREIGN KEY (`tsxwhodid`) REFERENCES `tsxwho` (`tsxwhodid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tsttoken`
--

LOCK TABLES `tsttoken` WRITE;
/*!40000 ALTER TABLE `tsttoken` DISABLE KEYS */;
/*!40000 ALTER TABLE `tsttoken` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tsxwho`
--

DROP TABLE IF EXISTS `tsxwho`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tsxwho` (
  `tsxwhodid` int(11) NOT NULL AUTO_INCREMENT,
  `ownerid` varchar(200) NOT NULL,
  `tokenurl` varchar(400) NOT NULL,
  `defaultconnectionresettime` bigint(20) NOT NULL,
  `isvalid` BIT(1) NOT NULL,
  `createddate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `uc` int(11) DEFAULT '0',
  PRIMARY KEY (`tsxwhodid`),
  UNIQUE KEY `ownerid_UNIQUE` (`ownerid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tsxwho`
--

LOCK TABLES `tsxwho` WRITE;
/*!40000 ALTER TABLE `tsxwho` DISABLE KEYS */;
/*!40000 ALTER TABLE `tsxwho` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-06-23 10:18:38
