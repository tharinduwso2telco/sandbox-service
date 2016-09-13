CREATE DATABASE  IF NOT EXISTS `dev2_mife_sandbox_hub` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `dev2_mife_sandbox_hub`;
-- MySQL dump 10.13  Distrib 5.7.13, for Linux (x86_64)
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
-- Table structure for table `charge_amount_request`
--

DROP TABLE IF EXISTS `charge_amount_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `charge_amount_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `effect_date` date DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `channel` varchar(255) DEFAULT NULL,
  `client_correlator` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `end_user_id` varchar(255) DEFAULT NULL,
  `notify_url` varchar(255) DEFAULT NULL,
  `on_behalf_of` varchar(255) DEFAULT NULL,
  `purchase_cat_code` varchar(255) DEFAULT NULL,
  `reference_code` varchar(255) DEFAULT NULL,
  `tax_amount` double DEFAULT NULL,
  `tran_oper_status` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `callback_data` varchar(255) DEFAULT NULL,
  `mandate_id` varchar(255) DEFAULT NULL,
  `notification_format` varchar(255) DEFAULT NULL,
  `product_id` varchar(255) DEFAULT NULL,
  `reference_sequence` int(11) DEFAULT NULL,
  `original_server_reference_code` varchar(255) DEFAULT NULL,
  `service_id` varchar(255) DEFAULT NULL,
  `total_amount_charged` double DEFAULT NULL,
  `amount_reserved` double DEFAULT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  `payment_transaction_type` int(11) DEFAULT NULL,
  `refund_status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_haok1xtx5f32qy18r9yt06p31` (`client_correlator`),
  KEY `FKB48C1E939E083448` (`user_id`),
  CONSTRAINT `FKB48C1E939E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `charge_amount_request`
--

LOCK TABLES `charge_amount_request` WRITE;
/*!40000 ALTER TABLE `charge_amount_request` DISABLE KEYS */;
INSERT INTO `charge_amount_request` VALUES (1,'2016-04-27',10,'SMS','54321',NULL,'USD','Alien Invaders Game','tel:+94773524308',NULL,NULL,'Game','REF-12345',0.15,'Charged',1,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-1',1,0),(2,'2016-05-02',10,'SMS','4027901',NULL,'USD','Alien Invaders Game','tel:+94123123123',NULL,NULL,'Game','REF-12345',0.15,'Charged',5,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-2',1,0),(3,'2016-05-02',10,'SMS','5024530',NULL,'USD','Alien Invaders Game','tel:+94123123123',NULL,NULL,'Game','REF-12345',0.15,'Charged',5,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-3',1,0),(4,'2016-05-02',10,'SMS','2067403',NULL,'USD','Alien Invaders Game','tel:+94123123123',NULL,NULL,'Game','REF-12345',0.15,'Charged',5,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-4',1,0),(5,'2016-05-02',10,'SMS','3646144',NULL,'USD','Alien Invaders Game','tel:+94123123123',NULL,NULL,'Game','REF-12345',0.15,'Charged',5,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-5',1,0),(6,'2016-05-02',10,'SMS','7069465',NULL,'USD','Alien Invaders Game','tel:+94123123123',NULL,NULL,'Game','REF-12345',0.15,'Charged',5,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-6',1,0),(7,'2016-05-02',10,'SMS','3687508',NULL,'USD','Alien Invaders Game','tel:+94123123123',NULL,NULL,'Game','REF-12345',0.15,'Charged',5,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-7',1,0),(8,'2016-05-02',10,'SMS','0987581',NULL,'USD','Alien Invaders Game','tel:+94123123123',NULL,NULL,'Game','REF-12345',0.15,'Charged',5,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-8',1,1),(9,'2016-05-02',10,'SMS','3641564','ABC','USD','Alien Invaders Game','tel:+94123123123','http://localhost:8080/mifeapiserver/callback.jsp','Example Games Inc','Game','REF-12345',0.15,'Refunded',5,'54321','0000','54321','0000',0,'src-8','0000',0,0,'paytran-9',6,0),(10,'2016-05-02',10,'SMS','9317007',NULL,'USD','Alien Invaders Game','tel:+94123123123',NULL,NULL,'Game','REF-12345',0.15,'Charged',5,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-10',1,0);
/*!40000 ALTER TABLE `charge_amount_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locationparam`
--

DROP TABLE IF EXISTS `locationparam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `locationparam` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `altitude` varchar(255) DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `loc_ret_status` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `locationparam_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locationparam`
--

LOCK TABLES `locationparam` WRITE;
/*!40000 ALTER TABLE `locationparam` DISABLE KEYS */;
INSERT INTO `locationparam` VALUES (1,'2341','45244444','135131','Retrieved',1);
/*!40000 ALTER TABLE `locationparam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locationtransactionlog`
--

DROP TABLE IF EXISTS `locationtransactionlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `locationtransactionlog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `requested_accuracy` double DEFAULT NULL,
  `tran_oper_status` varchar(255) DEFAULT NULL,
  `effect_date` date DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `locationtransactionlog_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locationtransactionlog`
--

LOCK TABLES `locationtransactionlog` WRITE;
/*!40000 ALTER TABLE `locationtransactionlog` DISABLE KEYS */;
INSERT INTO `locationtransactionlog` VALUES (1,'tel:+94773524308',1000,'Retrieved','2016-04-27',1),(2,'tel:+94773524308',1000,'Retrieved','2016-04-27',1),(3,'tel:+94773524308',1000,'Retrieved','2016-04-27',1),(4,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(5,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(6,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(7,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(8,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(9,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(10,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(11,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(12,'tel:+94773524308',1000,'Retrieved','2016-08-24',4),(13,'tel:+94773524308',1000,'Retrieved','2016-08-24',4),(14,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(15,'tel:+94773524308',1000,'Retrieved','2016-08-29',1),(16,'tel:+94773524308',1000,'Retrieved','2016-08-29',1),(17,'tel:+94773524308',1000,'Retrieved','2016-08-30',1),(18,'tel:+94773524308',1000,'Retrieved','2016-08-30',1),(19,'tel:+94773524308',1000,'Retrieved','2016-08-30',1),(20,'tel:+94773524308',1000,'Retrieved','2016-08-31',1),(21,'tel:+94773524308',1000,'Retrieved','2016-08-31',1),(22,'tel:+94773524308',1000,'Retrieved','2016-08-31',1),(23,'tel:+94773524308',1000,'Retrieved','2016-09-08',1),(24,'tel:+94773524308',1000,'Retrieved','2016-09-08',1);
/*!40000 ALTER TABLE `locationtransactionlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mobileidapiencoderequest`
--

DROP TABLE IF EXISTS `mobileidapiencoderequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mobileidapiencoderequest` (
  `mobIdApiId` int(11) NOT NULL AUTO_INCREMENT,
  `consumerkey` varchar(255) DEFAULT NULL,
  `consumersecret` varchar(255) DEFAULT NULL,
  `authcode` varchar(255) DEFAULT NULL,
  `granttype` varchar(45) DEFAULT NULL,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `scope` varchar(45) DEFAULT NULL,
  `user` varchar(45) DEFAULT NULL,
  `refreshToken` varchar(45) DEFAULT NULL,
  `accessToken` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`mobIdApiId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mobileidapiencoderequest`
--

LOCK TABLES `mobileidapiencoderequest` WRITE;
/*!40000 ALTER TABLE `mobileidapiencoderequest` DISABLE KEYS */;
/*!40000 ALTER TABLE `mobileidapiencoderequest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `numbers`
--

DROP TABLE IF EXISTS `numbers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `numbers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(255) DEFAULT NULL,
  `num_balance` double DEFAULT NULL,
  `reserved_amount` double NOT NULL DEFAULT '0',
  `num_description` varchar(255) DEFAULT NULL,
  `num_status` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK88C28E4A9E083448` (`user_id`),
  CONSTRAINT `FK88C28E4A9E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `numbers`
--

LOCK TABLES `numbers` WRITE;
/*!40000 ALTER TABLE `numbers` DISABLE KEYS */;
INSERT INTO `numbers` VALUES (1,'94773524308',989.85,0,'test number',1,1),(11,'94123123123',1000,0,'testAuxenta',1,5),(12,'945915648',1000,0,'dfaf',1,1);
/*!40000 ALTER TABLE `numbers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_gen`
--

DROP TABLE IF EXISTS `payment_gen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_gen` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `delivery_status` varchar(255) DEFAULT NULL,
  `max_pay_amount` varchar(255) DEFAULT NULL,
  `max_tx_perday` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA4347D979E083448` (`user_id`),
  CONSTRAINT `FKA4347D979E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_gen`
--

LOCK TABLES `payment_gen` WRITE;
/*!40000 ALTER TABLE `payment_gen` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_gen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_transaction`
--

DROP TABLE IF EXISTS `payment_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_transaction` (
  `transaction_id` varchar(255) NOT NULL,
  `effect_date` date DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `currency` varchar(50) DEFAULT NULL,
  `end_user_id` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `FKB154785395263845` (`user_id`),
  CONSTRAINT `FKB48C1E55465448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_transaction`
--

LOCK TABLES `payment_transaction` WRITE;
/*!40000 ALTER TABLE `payment_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paymentparam`
--

DROP TABLE IF EXISTS `paymentparam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paymentparam` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `created` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `lastupdated` varchar(255) DEFAULT NULL,
  `lastupdated_date` datetime DEFAULT NULL,
  `maxamt` double(11,2) DEFAULT NULL,
  `maxtrn` int(11) DEFAULT NULL,
  `paystatus` varchar(255) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paymentparam`
--

LOCK TABLES `paymentparam` WRITE;
/*!40000 ALTER TABLE `paymentparam` DISABLE KEYS */;
INSERT INTO `paymentparam` VALUES (1,NULL,NULL,NULL,NULL,100000000.00,1000,'Charged',1),(2,NULL,NULL,NULL,NULL,15000.00,1000,'Processing',2),(3,NULL,NULL,NULL,NULL,15000.00,1000,'Processing',3),(4,NULL,NULL,NULL,NULL,15000.00,1000,'Processing',4),(5,NULL,NULL,NULL,NULL,10.00,1000,'Charged',5);
/*!40000 ALTER TABLE `paymentparam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sbtprmsisdnservicessmap`
--

DROP TABLE IF EXISTS `sbtprmsisdnservicessmap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbtprmsisdnservicessmap` (
  `id` int(11) NOT NULL,
  `numbersid` int(11) NOT NULL,
  `servicesid` int(11) NOT NULL,
  CONSTRAINT `pksbtprmsisdnservicessmap` PRIMARY KEY (`id`), 
  CONSTRAINT `fk01sbtprmsisdnservicessmap` FOREIGN KEY (`numbersid`) REFERENCES `numbers` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk02sbtprmsisdnservicessmap` FOREIGN KEY (`servicesid`) REFERENCES `sbxprservices` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbtprmsisdnservicessmap`
--

LOCK TABLES `sbtprmsisdnservicessmap` WRITE;
/*!40000 ALTER TABLE `sbtprmsisdnservicessmap` DISABLE KEYS */;
INSERT INTO `sbtprmsisdnservicessmap` VALUES (1,1,1),(2,1,2);
/*!40000 ALTER TABLE `sbtprmsisdnservicessmap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sbtprprovisionedservices`
--

DROP TABLE IF EXISTS `sbtprprovisionedservices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbtprprovisionedservices` (
  `id` int(11) NOT NULL,
  `msisdnservicesmapid` int(11) DEFAULT NULL,
  `clientcorrelator` varchar(100) DEFAULT NULL,
  `clientreferencecode` varchar(45) DEFAULT NULL,
  `notifyurl` varchar(255) DEFAULT NULL,
  `callbackdata` varchar(45) DEFAULT NULL,
  `statusid` int(11) DEFAULT NULL,
  `createddate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT `pksbtprprovisionedservices` PRIMARY KEY (`id`), 
  CONSTRAINT `fk01sbtprsbtprprovisionedservices` FOREIGN KEY (`statusid`) REFERENCES `sbxstatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk02sbtprsbtprprovisionedservices` FOREIGN KEY (`msisdnservicesmapid`) REFERENCES `sbtprmsisdnservicessmap` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbtprprovisionedservices`
--

LOCK TABLES `sbtprprovisionedservices` WRITE;
/*!40000 ALTER TABLE `sbtprprovisionedservices` DISABLE KEYS */;
INSERT INTO `sbtprprovisionedservices` VALUES (1,1,'12345','REF12345','http://application.com/notifyURL','',1,'');
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
  CONSTRAINT `pksbtprspexpectmessage` PRIMARY KEY (`id`), 
  CONSTRAINT `fk01sbtprspexpectmessage` FOREIGN KEY (`numberid`) REFERENCES `numbers` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk02sbtprspexpectmessage` FOREIGN KEY (`messageid`) REFERENCES `sbxresponsemessage` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk03sbtprspexpectmessage` FOREIGN KEY (`servicesid`) REFERENCES `sbxprservices` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbtprspexpectmessage`
--

LOCK TABLES `sbtprspexpectmessage` WRITE;
/*!40000 ALTER TABLE `sbtprspexpectmessage` DISABLE KEYS */;
INSERT INTO `sbtprspexpectmessage` VALUES (1,1,1,'PROVISION',1),(2,1,1,'DELETE',1);
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
 CONSTRAINT `pksbxapitypes` PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbxapitypes`
--

LOCK TABLES `sbxapitypes` WRITE;
/*!40000 ALTER TABLE `sbxapitypes` DISABLE KEYS */;
INSERT INTO `sbxapitypes` VALUES (1,'LOCATION'),(2,'SMS'),(3,'USSD'),(4,'PAYMENT'),(5,'CREDIT'),(6,'WALLET'),(7,'PROVISION');
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
  CONSTRAINT `pksbxprrequesstlog` PRIMARY KEY (`provision_request_log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbxprrequesstlog`
--

LOCK TABLES `sbxprrequesstlog` WRITE;
/*!40000 ALTER TABLE `sbxprrequesstlog` DISABLE KEYS */;
INSERT INTO `sbxprrequesstlog` VALUES (1,'QUERY_APPLICABLE','+9471123',1,NULL,NULL,NULL,NULL,'2016-09-07'),(2,'QUERY_APPLICABLE','+94773524308',1,NULL,NULL,NULL,NULL,'2016-09-07'),(3,'QUERY_APPLICABLE','el:+94773524308',1,NULL,NULL,NULL,NULL,'2016-09-07');
/*!40000 ALTER TABLE `sbxprrequesstlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sbxprservices`
--

DROP TABLE IF EXISTS `sbxprservices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbxprservices` (
  `id` int(11) NOT NULL,
  `code` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  `description` varchar(150) DEFAULT NULL,
  `charge` float DEFAULT NULL,
  `tag` varchar(45) DEFAULT NULL,
  `value` varchar(45) DEFAULT NULL,
  CONSTRAINT `pksbxprservices` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbxprservices`
--

LOCK TABLES `sbxprservices` WRITE;
/*!40000 ALTER TABLE `sbxprservices` DISABLE KEYS */;
INSERT INTO `sbxprservices` VALUES (1,'SRV0001','ROAM5G','ROAMING','ServiceDescription',10,'count','25'),(2,'SRV0002','FBDATA','DATA','ServiceDescription',0,'limit','1000');
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
  `message` text,
  `apitypeid` int(11) DEFAULT NULL,
  CONSTRAINT `pksbxresponsemessage` PRIMARY KEY (`id`), 
  CONSTRAINT `fk01sbxresponsemessage` FOREIGN KEY (`categoryid`) REFERENCES `sbxresponsemessagecategory` (`id`),
  CONSTRAINT `fk02sbxresponsemessage` FOREIGN KEY (`apitypeid`) REFERENCES `sbxapitypes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbxresponsemessage`
--

LOCK TABLES `sbxresponsemessage` WRITE;
/*!40000 ALTER TABLE `sbxresponsemessage` DISABLE KEYS */;
INSERT INTO `sbxresponsemessage` VALUES (1,1,'Service Not Available',7);
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
   CONSTRAINT `pksbxresponsemessagecategory` PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbxresponsemessagecategory`
--

LOCK TABLES `sbxresponsemessagecategory` WRITE;
/*!40000 ALTER TABLE `sbxresponsemessagecategory` DISABLE KEYS */;
INSERT INTO `sbxresponsemessagecategory` VALUES (1,'General Error'),(2,'Service Error'),(3,'Policy Error');
/*!40000 ALTER TABLE `sbxresponsemessagecategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sbxstatus`
--

DROP TABLE IF EXISTS `sbxstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sbxstatus` (
  `id` int(11) NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  CONSTRAINT `pksbxstatus` PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sbxstatus`
--

LOCK TABLES `sbxstatus` WRITE;
/*!40000 ALTER TABLE `sbxstatus` DISABLE KEYS */;
INSERT INTO `sbxstatus` VALUES (1,'Pending'),(2,'Failed'),(3,'Success'),(4,'NotActive');
/*!40000 ALTER TABLE `sbxstatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `send_sms_to_application`
--

DROP TABLE IF EXISTS `send_sms_to_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `send_sms_to_application` (
  `sms_id` int(11) NOT NULL AUTO_INCREMENT,
  `effect_date` date DEFAULT NULL,
  `destination_address` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `sender_address` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`sms_id`),
  KEY `FKEBE4BF499E083448` (`user_id`),
  CONSTRAINT `FKEBE4BF499E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `send_sms_to_application`
--

LOCK TABLES `send_sms_to_application` WRITE;
/*!40000 ALTER TABLE `send_sms_to_application` DISABLE KEYS */;
INSERT INTO `send_sms_to_application` VALUES (1,'2016-08-24','7555','Hello World','94773524308',1),(2,'2016-08-24','7555','Hello World','94773524308',1),(3,'2016-08-30','7555','Hello World','94773524308',1),(4,'2016-08-30','7555','Hello World','94773524308',1);
/*!40000 ALTER TABLE `send_sms_to_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sender_address`
--

DROP TABLE IF EXISTS `sender_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sender_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `shortcode` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKB79857EA9E083448` (`user_id`),
  CONSTRAINT `FKB79857EA9E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sender_address`
--

LOCK TABLES `sender_address` WRITE;
/*!40000 ALTER TABLE `sender_address` DISABLE KEYS */;
INSERT INTO `sender_address` VALUES (1,'test number','7555',1);
/*!40000 ALTER TABLE `sender_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms`
--

DROP TABLE IF EXISTS `sms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deliveryStatus` varchar(255) DEFAULT NULL,
  `maxNotifications` varchar(255) DEFAULT NULL,
  `notificationDelay` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1BD599E083448` (`user_id`),
  CONSTRAINT `FK1BD599E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms`
--

LOCK TABLES `sms` WRITE;
/*!40000 ALTER TABLE `sms` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_delivery_status`
--

DROP TABLE IF EXISTS `sms_delivery_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_delivery_status` (
  `transaction_id` varchar(255) NOT NULL,
  `sender_address` varchar(255) DEFAULT NULL,
  `delivery_status` varchar(255) DEFAULT NULL,
  `effect_date` date DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `FK_sycg0sik20gocmm2v5oqwh2o8` (`user_id`),
  CONSTRAINT `FK_sycg0sik20gocmm2v5oqwh2o8` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_delivery_status`
--

LOCK TABLES `sms_delivery_status` WRITE;
/*!40000 ALTER TABLE `sms_delivery_status` DISABLE KEYS */;
INSERT INTO `sms_delivery_status` VALUES ('smstran-1','tel:7555','DeliveredToTerminal','2016-08-24',1),('smstran-12','tel:7555','DeliveredToTerminal','2016-08-30',1),('smstran-13','tel:7555','DeliveredToTerminal','2016-08-30',1),('smstran-14','tel:7555','DeliveredToTerminal','2016-08-30',1),('smstran-15','tel:7555','DeliveredToTerminal','2016-08-30',1),('smstran-16','tel:7555','DeliveredToTerminal','2016-08-30',1),('smstran-17','tel:7555','DeliveredToTerminal','2016-08-30',1),('smstran-4','tel:7555','DeliveredToTerminal','2016-08-24',1),('smstran-6','tel:7555','DeliveredToTerminal','2016-08-24',1),('smstran-8','tel:7555','DeliveredToTerminal','2016-08-30',1);
/*!40000 ALTER TABLE `sms_delivery_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_delivery_subscription`
--

DROP TABLE IF EXISTS `sms_delivery_subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_delivery_subscription` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `sender_address` varchar(225) DEFAULT NULL,
  `sub_status` int(11) NOT NULL DEFAULT '0',
  `notify_url` varchar(225) DEFAULT NULL,
  `filter` varchar(225) DEFAULT NULL,
  `callbackdata` varchar(225) DEFAULT NULL,
  `clientcorrelator` varchar(225) DEFAULT NULL,
  `request` longtext,
  PRIMARY KEY (`id`),
  KEY `FK_adwhr1k8dr8pdh9osopmeg6b6` (`user_id`),
  CONSTRAINT `FK_adwhr1k8dr8pdh9osopmeg6b6` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_delivery_subscription`
--

LOCK TABLES `sms_delivery_subscription` WRITE;
/*!40000 ALTER TABLE `sms_delivery_subscription` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_delivery_subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_subscription`
--

DROP TABLE IF EXISTS `sms_subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_subscription` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sub_number` varchar(255) DEFAULT NULL,
  `sub_status` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA48A3E439E083448` (`user_id`),
  CONSTRAINT `FKA48A3E439E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sms_subscription`
--

LOCK TABLES `sms_subscription` WRITE;
/*!40000 ALTER TABLE `sms_subscription` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smsparam`
--

DROP TABLE IF EXISTS `smsparam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `smsparam` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `deliveryStatus` varchar(45) DEFAULT NULL,
  `maxNotifications` varchar(11) DEFAULT NULL,
  `notificationDelay` varchar(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `created` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `lastupdated` varchar(255) DEFAULT NULL,
  `lastupdated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smsparam`
--

LOCK TABLES `smsparam` WRITE;
/*!40000 ALTER TABLE `smsparam` DISABLE KEYS */;
INSERT INTO `smsparam` VALUES (1,'DeliveredToTerminal','1000','10',1,NULL,NULL,NULL,NULL),(2,'DeliveredToTerminal','1000','10',2,NULL,NULL,NULL,NULL),(3,'DeliveredToTerminal','1000','10',3,NULL,NULL,NULL,NULL),(4,'DeliveredToTerminal','1000','10',4,NULL,NULL,NULL,NULL),(5,'DeliveredToTerminal','1000','10',5,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `smsparam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smstransactionlog`
--

DROP TABLE IF EXISTS `smstransactionlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `smstransactionlog` (
  `sms_id` int(11) NOT NULL AUTO_INCREMENT,
  `effect_date` date DEFAULT NULL,
  `addresses` varchar(255) DEFAULT NULL,
  `callback_data` varchar(255) DEFAULT NULL,
  `client_correlator` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `notify_url` varchar(255) DEFAULT NULL,
  `sender_address` varchar(255) DEFAULT NULL,
  `sender_name` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `batchsize` int(11) DEFAULT NULL,
  `criteria` varchar(255) DEFAULT NULL,
  `notificationFormat` varchar(255) DEFAULT NULL,
  `trnstatus` varchar(255) DEFAULT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  `request_id` varchar(255) DEFAULT NULL,
  `txntype` int(11) DEFAULT NULL,
  PRIMARY KEY (`sms_id`),
  KEY `FK2A1D0F729E083448` (`user_id`),
  CONSTRAINT `FK2A1D0F729E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smstransactionlog`
--

LOCK TABLES `smstransactionlog` WRITE;
/*!40000 ALTER TABLE `smstransactionlog` DISABLE KEYS */;
INSERT INTO `smstransactionlog` VALUES (1,'2016-08-24','[tel:+94773524308]','some-data-useful-to-the-requester','123456:1472042340971SM11001','Test1','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-1',NULL,1),(2,'2016-08-24',NULL,NULL,NULL,NULL,NULL,'7555',NULL,1,2,NULL,NULL,'success',NULL,NULL,2),(3,'2016-08-24',NULL,NULL,NULL,NULL,NULL,'tel:7555',NULL,1,0,NULL,NULL,'success',NULL,'smstran-1',5),(4,'2016-08-24','[tel:+94773524308]','some-data-useful-to-the-requester','123456:1472042912982SM11002','Test1','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-4',NULL,1),(5,'2016-08-24',NULL,'doSomething()',NULL,NULL,'http://www.yoururl.here/notifications/DeliveryInfoNotification','7555',NULL,1,0,'Vote','JSON','success',NULL,NULL,3),(6,'2016-08-24','[tel:+94773524308]','some-data-useful-to-the-requester','123456:1472043111467SM11003','Test1','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-6',NULL,1),(7,'2016-08-24',NULL,NULL,NULL,NULL,NULL,'tel:7555',NULL,1,0,NULL,NULL,'success',NULL,'smstran-6',5),(8,'2016-08-30','[tel:+94773524308]','some-data-useful-to-the-requester','123456:1472555967935SM11002','Hello World','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-8',NULL,1),(9,'2016-08-30',NULL,NULL,NULL,NULL,NULL,'tel:7555',NULL,1,0,NULL,NULL,'success',NULL,'smstran-8',5),(10,'2016-08-30',NULL,NULL,NULL,NULL,NULL,'7555',NULL,1,1,NULL,NULL,'success',NULL,NULL,2),(11,'2016-08-30',NULL,NULL,NULL,NULL,NULL,'7555',NULL,1,2,NULL,NULL,'success',NULL,NULL,2),(12,'2016-08-30','[tel:+94773524308]','some-data-useful-to-the-requester','123456:1472556483791SM11003','Test1','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-12',NULL,1),(13,'2016-08-30','[tel:+94773524308]','some-data-useful-to-the-requester','123456:1472557262623SM11004','Hello World','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-13',NULL,1),(14,'2016-08-30','[tel:+94773524308]','some-data-useful-to-the-requester','123456:1472557268910SM11005','Hello World','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-14',NULL,1),(15,'2016-08-30','[tel:+94773524308]','some-data-useful-to-the-requester','123456:1472557601440SM11006','Hello World','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-15',NULL,1),(16,'2016-08-30','[tel:+94773524308]','some-data-useful-to-the-requester','1234567890:1472557624395SM11007','Hello World','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-16',NULL,1),(17,'2016-08-30','[tel:+94773524308]','some-data-useful-to-the-requester','1234567890:1472557645748SM11008','Hello World','http://application.example.com/notifications/DeliveryInfoNotification/Test','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-17',NULL,1);
/*!40000 ALTER TABLE `smstransactionlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscribe_sms_request`
--

DROP TABLE IF EXISTS `subscribe_sms_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscribe_sms_request` (
  `subscribe_id` int(11) NOT NULL AUTO_INCREMENT,
  `effect_date` date DEFAULT NULL,
  `callback_data` varchar(255) DEFAULT NULL,
  `client_correlator` varchar(255) DEFAULT NULL,
  `criteria` varchar(255) DEFAULT NULL,
  `destination_address` varchar(255) DEFAULT NULL,
  `notification_format` varchar(255) DEFAULT NULL,
  `notify_url` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`subscribe_id`),
  KEY `FKC8368A349E083448` (`user_id`),
  CONSTRAINT `FKC8368A349E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscribe_sms_request`
--

LOCK TABLES `subscribe_sms_request` WRITE;
/*!40000 ALTER TABLE `subscribe_sms_request` DISABLE KEYS */;
INSERT INTO `subscribe_sms_request` VALUES (1,'2016-08-24','doSomething()','12345','Vote','7555','JSON','http://www.yoururl.here/notifications/DeliveryInfoNotification',1);
/*!40000 ALTER TABLE `subscribe_sms_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL,
  `user_status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_lqjrcobrh9jc8wpcar64q1bfh` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin',1),(2,'AuxWithoutAggregate',1),(3,'AuxTestUser',1),(4,'AuxLogoutUser',1),(5,'AutInvSP1',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ussd_subscription`
--

DROP TABLE IF EXISTS `ussd_subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ussd_subscription` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `callbackData` varchar(255) DEFAULT NULL,
  `clientCorrelator` varchar(255) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `destinationAddress` varchar(255) DEFAULT NULL,
  `effect_date` date DEFAULT NULL,
  `notifyUrl` varchar(255) DEFAULT NULL,
  `resourceUrl` varchar(255) DEFAULT NULL,
  `subscriptionId` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `subStatus` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ec81kos30iygc5p1gcfq0m9md` (`user_id`),
  CONSTRAINT `FK_ec81kos30iygc5p1gcfq0m9md` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ussd_subscription`
--

LOCK TABLES `ussd_subscription` WRITE;
/*!40000 ALTER TABLE `ussd_subscription` DISABLE KEYS */;
/*!40000 ALTER TABLE `ussd_subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ussd_transactions`
--

DROP TABLE IF EXISTS `ussd_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ussd_transactions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actionStatus` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `callbackData` varchar(255) DEFAULT NULL,
  `clientCorrelator` varchar(255) DEFAULT NULL,
  `effect_date` date DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `notifyUrl` varchar(255) DEFAULT NULL,
  `sessionId` varchar(255) DEFAULT NULL,
  `shortCode` varchar(255) DEFAULT NULL,
  `ussdAction` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_f6e3wvsa0gqeft93d6r8uo4qx` (`user_id`),
  CONSTRAINT `FK_f6e3wvsa0gqeft93d6r8uo4qx` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ussd_transactions`
--

LOCK TABLES `ussd_transactions` WRITE;
/*!40000 ALTER TABLE `ussd_transactions` DISABLE KEYS */;
/*!40000 ALTER TABLE `ussd_transactions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-09-09 15:33:15
