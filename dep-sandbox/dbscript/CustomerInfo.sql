-- MySQL dump 10.13  Distrib 5.7.15, for Linux (x86_64)
--
-- Host: localhost    Database: dev2_mife_sandbox_hub
-- ------------------------------------------------------
-- Server version	5.7.13-0ubuntu0.16.04.2

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
-- Table structure for table `numbers`
--

DROP TABLE IF EXISTS `numbers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `numbers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(255) DEFAULT NULL,
  `imsi` varchar(255) DEFAULT NULL,
  `mnc` int(5) DEFAULT NULL,
  `mcc` int(5) DEFAULT NULL,
  `num_balance` double DEFAULT NULL,
  `reserved_amount` double NOT NULL DEFAULT '0',
  `num_description` varchar(255) DEFAULT NULL,
  `num_status` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK88C28E4A9E083448` (`user_id`),
  CONSTRAINT `FK88C28E4A9E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `numbers`
--

LOCK TABLES `numbers` WRITE;
/*!40000 ALTER TABLE `numbers` DISABLE KEYS */;
INSERT INTO `numbers` VALUES (1,'94773524308',NULL,NULL,NULL,989.85,0,'test number',1,1),(2,'94123123123',NULL,NULL,NULL,1000,0,'testAuxenta',1,5),(3,'94771564812',NULL,NULL,NULL,1000,0,'dfaf',1,1);
/*!40000 ALTER TABLE `numbers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sbtattributedistribution`
--

DROP TABLE IF EXISTS `sbtattributedistribution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbtattributedistribution` (
  `sbtattributedistributiondid` int(11) NOT NULL AUTO_INCREMENT,
  `attributedid` int(11) NOT NULL,
  `apiservicecallsdid` int(11) NOT NULL,
  PRIMARY KEY (`sbtattributedistributiondid`),
  KEY `fk01sbtattributedistribution_idx` (`attributedid`),
  KEY `fk02sbtattributedistribution_idx` (`apiservicecallsdid`),
  CONSTRAINT `fk01sbtattributedistribution` FOREIGN KEY (`attributedid`) REFERENCES `sbxattribute` (`sbxattributedid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk02sbtattributedistribution` FOREIGN KEY (`apiservicecallsdid`) REFERENCES `sbxapiservicecalls` (`sbxapiservicecallsdid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbtattributedistribution`
--

LOCK TABLES `sbtattributedistribution` WRITE;
/*!40000 ALTER TABLE `sbtattributedistribution` DISABLE KEYS */;
INSERT INTO `sbtattributedistribution` VALUES (1,1,2),(2,2,2),(3,3,2),(4,4,2),(5,5,2),(6,10,2),(7,11,2),(8,12,2),(9,13,2),(10,14,1),(11,15,1),(12,16,1),(13,17,1),(14,18,2),(15,19,2);
/*!40000 ALTER TABLE `sbtattributedistribution` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sbxapiservicecalls`
--

DROP TABLE IF EXISTS `sbxapiservicecalls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbxapiservicecalls` (
  `sbxapiservicecallsdid` int(11) NOT NULL AUTO_INCREMENT,
  `apitypesdid` int(11) NOT NULL,
  `service` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`sbxapiservicecallsdid`),
  KEY `fk01sbxapiservicecalls_idx` (`apitypesdid`),
  CONSTRAINT `fk01sbxapiservicecalls` FOREIGN KEY (`apitypesdid`) REFERENCES `sbxapitypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbxapiservicecalls`
--

LOCK TABLES `sbxapiservicecalls` WRITE;
/*!40000 ALTER TABLE `sbxapiservicecalls` DISABLE KEYS */;
INSERT INTO `sbxapiservicecalls` VALUES (1,8,'Attributes'),(2,8,'Profile');
/*!40000 ALTER TABLE `sbxapiservicecalls` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sbxapitypes`
--

DROP TABLE IF EXISTS `sbxapitypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbxapitypes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiname` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbxapitypes`
--

LOCK TABLES `sbxapitypes` WRITE;
/*!40000 ALTER TABLE `sbxapitypes` DISABLE KEYS */;
INSERT INTO `sbxapitypes` VALUES (1,'LOCATION'),(2,'SMS'),(3,'USSD'),(4,'PAYMENT'),(5,'CREDIT'),(6,'WALLET'),(7,'PROVISION'),(8,'CUSTOMER');
/*!40000 ALTER TABLE `sbxapitypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sbxattribute`
--

DROP TABLE IF EXISTS `sbxattribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbxattribute` (
  `sbxattributedid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`sbxattributedid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbxattribute`
--

LOCK TABLES `sbxattribute` WRITE;
/*!40000 ALTER TABLE `sbxattribute` DISABLE KEYS */;
INSERT INTO `sbxattribute` VALUES (1,'title'),(2,'firstname'),(3,'lastname'),(4,'dob'),(5,'address'),(10,'id_type'),(11,'id_status'),(12,'owner_type'),(13,'account_type'),(14,'basic'),(15,'billing'),(16,'identification'),(17,'account'),(18,'additional_info'),(19,'id_number');
/*!40000 ALTER TABLE `sbxattribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sbxattributevalue`
--

DROP TABLE IF EXISTS `sbxattributevalue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbxattributevalue` (
  `sbxattributevaluedid` int(11) NOT NULL AUTO_INCREMENT,
  `attributedistributiondid` int(11) NOT NULL,
  `tobject` varchar(45) NOT NULL,
  `ownerdid` varchar(45) NOT NULL,
  `value` text,
  PRIMARY KEY (`sbxattributevaluedid`),
  KEY `attributedid_idx` (`attributedistributiondid`),
  CONSTRAINT `fk01sbxattributevalue` FOREIGN KEY (`attributedistributiondid`) REFERENCES `sbtattributedistribution` (`sbtattributedistributiondid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbxattributevalue`
--

LOCK TABLES `sbxattributevalue` WRITE;
/*!40000 ALTER TABLE `sbxattributevalue` DISABLE KEYS */;
INSERT INTO `sbxattributevalue` VALUES (1,1,'user','1','MR'),(2,2,'user','1','Chinthaka'),(3,3,'user','1','Weerakkody'),(4,4,'user','1','1987-10-10 18:30:19'),(5,5,'user','1','{\"line1\":\"242/25\",\"line2\":\"\",\"line3\":\"\",\"city\":\"Homagama\",\"country\":\"Sri Lanka\"}'),(6,6,'user','1','PP'),(7,7,'user','1','CONFIRMED'),(8,9,'user','1','Prepaid');
/*!40000 ALTER TABLE `sbxattributevalue` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-10-12  9:56:49
