CREATE DATABASE  IF NOT EXISTS `dev2_mife_sandbox_hub` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `dev2_mife_sandbox_hub`;
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
-- Table structure for table `sbtprmsisdnservicessmap`
--

DROP TABLE IF EXISTS `sbtprmsisdnservicessmap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbtprmsisdnservicessmap` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `numbersid` int(11) NOT NULL,
  `servicesid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk01sbtprmsisdnservicessmap` (`numbersid`),
  KEY `fk02sbtprmsisdnservicessmap` (`servicesid`),
  CONSTRAINT `fk01sbtprmsisdnservicessmap` FOREIGN KEY (`numbersid`) REFERENCES `numbers` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk02sbtprmsisdnservicessmap` FOREIGN KEY (`servicesid`) REFERENCES `sbxprservices` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbtprmsisdnservicessmap`
--

LOCK TABLES `sbtprmsisdnservicessmap` WRITE;
/*!40000 ALTER TABLE `sbtprmsisdnservicessmap` DISABLE KEYS */;
INSERT INTO `sbtprmsisdnservicessmap` VALUES (1,1,1),(2,1,2),(3,2,1),(4,1,3),(5,2,2),(6,2,3);
/*!40000 ALTER TABLE `sbtprmsisdnservicessmap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sbtprprovisionedservices`
--

DROP TABLE IF EXISTS `sbtprprovisionedservices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbtprprovisionedservices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msisdnservicesmapid` int(11) DEFAULT NULL,
  `clientcorrelator` varchar(100) DEFAULT NULL,
  `clientreferencecode` varchar(45) DEFAULT NULL,
  `notifyurl` varchar(255) DEFAULT NULL,
  `callbackdata` varchar(45) DEFAULT NULL,
  `statusid` int(11) DEFAULT NULL,
  `createddate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk01sbtprsbtprprovisionedservices` (`statusid`),
  KEY `fk02sbtprsbtprprovisionedservices` (`msisdnservicesmapid`),
  CONSTRAINT `fk01sbtprsbtprprovisionedservices` FOREIGN KEY (`statusid`) REFERENCES `sbxstatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk02sbtprsbtprprovisionedservices` FOREIGN KEY (`msisdnservicesmapid`) REFERENCES `sbtprmsisdnservicessmap` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbtprprovisionedservices`
--

LOCK TABLES `sbtprprovisionedservices` WRITE;
/*!40000 ALTER TABLE `sbtprprovisionedservices` DISABLE KEYS */;
INSERT INTO `sbtprprovisionedservices` VALUES (3,3,'',NULL,NULL,NULL,1,'2016-09-13 17:53:07'),(5,5,NULL,NULL,NULL,'',3,'2016-09-13 17:54:58'),(6,6,NULL,NULL,NULL,NULL,2,'2016-09-13 17:54:58'),(7,1,'1345:0001:12','REF12345','https://gateway1a.mife.sla-mobile.com.my:8243/provisioning/v1/ProvisionNotification/69','some-data-useful-to-the-requester',6,'2016-10-10 10:40:11'),(8,2,'1345','REF12345','http://application.example.com/notifications/DeliveryInfoNotification','some-data-useful-to-the-requester',3,'2016-09-30 05:36:21');
/*!40000 ALTER TABLE `sbtprprovisionedservices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sbtprspexpectmessage`
--

DROP TABLE IF EXISTS `sbtprspexpectmessage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbtprspexpectmessage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `numberid` int(11) NOT NULL,
  `messageid` int(11) NOT NULL,
  `requesttype` varchar(50) DEFAULT NULL,
  `servicesid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk01sbtprspexpectmessage` (`numberid`),
  KEY `fk02sbtprspexpectmessage` (`messageid`),
  KEY `fk03sbtprspexpectmessage` (`servicesid`),
  CONSTRAINT `fk01sbtprspexpectmessage` FOREIGN KEY (`numberid`) REFERENCES `numbers` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk02sbtprspexpectmessage` FOREIGN KEY (`messageid`) REFERENCES `sbxresponsemessage` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk03sbtprspexpectmessage` FOREIGN KEY (`servicesid`) REFERENCES `sbxprservices` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbtprspexpectmessage`
--

LOCK TABLES `sbtprspexpectmessage` WRITE;
/*!40000 ALTER TABLE `sbtprspexpectmessage` DISABLE KEYS */;
INSERT INTO `sbtprspexpectmessage` VALUES (1,1,1,'PROVISION',1);
/*!40000 ALTER TABLE `sbtprspexpectmessage` ENABLE KEYS */;
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
-- Table structure for table `sbxprrequesstlog`
--

DROP TABLE IF EXISTS `sbxprrequesstlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbxprrequesstlog` (
  `provision_request_log_id` int(11) NOT NULL AUTO_INCREMENT,
  `requesttype` varchar(50) NOT NULL,
  `msisdn` varchar(50) NOT NULL,
  `userid` int(11) NOT NULL,
  `clientcorrelator` varchar(255) DEFAULT NULL,
  `clientreferencecode` varchar(255) DEFAULT NULL,
  `notifyurl` varchar(255) DEFAULT NULL,
  `callbackdata` varchar(255) DEFAULT NULL,
  `timestamp` date DEFAULT NULL,
  PRIMARY KEY (`provision_request_log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=308 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbxprrequesstlog`
--

LOCK TABLES `sbxprrequesstlog` WRITE;
/*!40000 ALTER TABLE `sbxprrequesstlog` DISABLE KEYS */;
INSERT INTO `sbxprrequesstlog` VALUES (181,'LIST_ACTIVE_PROVISIONED_SERVICES','tel:+94773524308',1,NULL,NULL,NULL,NULL,'2016-09-24'),(182,'DELETE_PROVISION_SERVICE','tel:+94773524308',1,'1345','REF12345','http://application.example.com/notifications/DeliveryInfoNotification','some-data-useful-to-the-requester','2016-09-24'),(183,'DELETE_PROVISION_SERVICE','tel:+94773524308',1,'1345','REF12345','http://application.example.com/notifications/DeliveryInfoNotification','some-data-useful-to-the-requester','2016-09-24');
/*!40000 ALTER TABLE `sbxprrequesstlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sbxprservices`
--

DROP TABLE IF EXISTS `sbxprservices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbxprservices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  `description` varchar(150) DEFAULT NULL,
  `charge` float DEFAULT NULL,
  `tag` varchar(45) DEFAULT NULL,
  `value` varchar(45) DEFAULT NULL,
  `userid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk01sbxprservice` (`userid`),
  CONSTRAINT `fk01sbxprservice` FOREIGN KEY (`userid`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbxprservices`
--

LOCK TABLES `sbxprservices` WRITE;
/*!40000 ALTER TABLE `sbxprservices` DISABLE KEYS */;
INSERT INTO `sbxprservices` VALUES (1,'SRV0001','ROAM5G','ROAMING','ServiceDescription',10,'count','25',1),(2,'SRV0002','FBDATA','DATA','ServiceDescription',0,'limit','1000',1),(3,'SRV0003','VoIP','VOICE','Voice',20,NULL,NULL,1);
/*!40000 ALTER TABLE `sbxprservices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sbxresponsemessage`
--

DROP TABLE IF EXISTS `sbxresponsemessage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbxresponsemessage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `categoryid` int(11) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `message` text,
  `apitypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk01sbxresponsemessage` (`categoryid`),
  KEY `fk02sbxresponsemessage` (`apitypeid`),
  CONSTRAINT `fk01sbxresponsemessage` FOREIGN KEY (`categoryid`) REFERENCES `sbxresponsemessagecategory` (`id`),
  CONSTRAINT `fk02sbxresponsemessage` FOREIGN KEY (`apitypeid`) REFERENCES `sbxapitypes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbxresponsemessage`
--

LOCK TABLES `sbxresponsemessage` WRITE;
/*!40000 ALTER TABLE `sbxresponsemessage` DISABLE KEYS */;
INSERT INTO `sbxresponsemessage` VALUES (1,1,'SVC0009','Service Related Error',7);
/*!40000 ALTER TABLE `sbxresponsemessage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sbxresponsemessagecategory`
--

DROP TABLE IF EXISTS `sbxresponsemessagecategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbxresponsemessagecategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbxresponsemessagecategory`
--

LOCK TABLES `sbxresponsemessagecategory` WRITE;
/*!40000 ALTER TABLE `sbxresponsemessagecategory` DISABLE KEYS */;
INSERT INTO `sbxresponsemessagecategory` VALUES (1,'SERVICEEXCEPTION'),(2,'POLICYEXCEPTION'),(3,'General Error');
/*!40000 ALTER TABLE `sbxresponsemessagecategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sbxstatus`
--

DROP TABLE IF EXISTS `sbxstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbxstatus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(45) DEFAULT NULL,
  `code` varchar(40) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbxstatus`
--

LOCK TABLES `sbxstatus` WRITE;
/*!40000 ALTER TABLE `sbxstatus` DISABLE KEYS */;
INSERT INTO `sbxstatus` VALUES (1,'Pending','PRV_PROVISION_PENDING','Provision Transaction is pendign with subsystem'),(2,'Failed','PRV_PROVISION_FAILED','Provisioning failed due to subsystem error'),(3,'Success','PRV_PROVISION_SUCCESS','Successfully provisioned'),(4,'NotActive','PRV_DELETE_NOT_ACTIVE','Service not provisioned for user'),(5,'AlreadyActive','PRV_PROVISION_ALREADY_ACTIVE','Service already active'),(6,'Pending','PRV_DELETE_PENDING','Delete Transaction is pending with subsystem'),(7,'Failed','PRV_DELETE_FAILED','Removal failed due to subsystem error'),(8,'Success','PRV_DELETE_SUCCESS','Successfully removed');
/*!40000 ALTER TABLE `sbxstatus` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-10-14  9:54:16
