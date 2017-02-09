CREATE DATABASE IF NOT EXISTS sandbox;
USE sandbox;


--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL,
  `user_status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_lqjrcobrh9jc8wpcar64q1bfh` (`user_name`)
);


--
-- Table structure for table `sbxapitypes`
--

CREATE TABLE IF NOT EXISTS `sbxapitypes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiname` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
);

--
-- Dumping data for table `sbxapitypes`
--

INSERT INTO `sbxapitypes` VALUES (1,'LOCATION'),(2,'SMS'),(3,'USSD'),(4,'PAYMENT'),(5,'CREDIT'),(6,'WALLET'),(7,'PROVISIONING'),(8,'CUSTOMERINFO');


--
-- Table structure for table `sbxapiservicecalls`
--

CREATE TABLE IF NOT EXISTS `sbxapiservicecalls` (
  `sbxapiservicecallsdid` int(11) NOT NULL AUTO_INCREMENT,
  `apitypesdid` int(11) NOT NULL,
  `service` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`sbxapiservicecallsdid`),
  KEY `fk01sbxapiservicecalls_idx` (`apitypesdid`),
  CONSTRAINT `fk01sbxapiservicecalls` FOREIGN KEY (`apitypesdid`) REFERENCES `sbxapitypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

--
-- Dumping data for table `sbxapiservicecalls`
--

INSERT INTO `sbxapiservicecalls` VALUES (1,8,'GetAttribute'),(2,8,'GetProfile'),(3,7,'GetApplicable'),(4,6,'MakePayment'),(5,6,'ListPayment'),(6,6,'RefundPayment'),(7,6,'BalanceLookup'),(8,5,'ApplyCredit'),(9,5,'PartialRefund'),(10,4,'chargeUser'),(11,4,'RefundUser');



--
-- Dumping data for table `user`
--

INSERT INTO `user` VALUES (1,'admin',1),(2,'AuxWithoutAggregate',1),(3,'AuxTestUser',1),(4,'AuxLogoutUser',1),(5,'AutInvSP1',1),(6,'ABCD',1);

--
-- Table structure for table `charge_amount_request`
--

CREATE TABLE IF NOT EXISTS  `charge_amount_request` (
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
  CONSTRAINT `FKB48C1E939E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

--
-- Dumping data for table `charge_amount_request`
--

INSERT INTO `charge_amount_request` VALUES (1,'2016-04-27',10,'SMS','54321',NULL,'USD','Alien Invaders Game','tel:+94773524308',NULL,NULL,'Game','REF-12345',0.15,'Charged',1,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-1',1,0),(2,'2016-05-02',10,'SMS','4027901',NULL,'USD','Alien Invaders Game','tel:+94123123123',NULL,NULL,'Game','REF-12345',0.15,'Charged',5,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-2',1,0),(3,'2016-05-02',10,'SMS','5024530',NULL,'USD','Alien Invaders Game','tel:+94123123123',NULL,NULL,'Game','REF-12345',0.15,'Charged',5,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-3',1,0),(4,'2016-05-02',10,'SMS','2067403',NULL,'USD','Alien Invaders Game','tel:+94123123123',NULL,NULL,'Game','REF-12345',0.15,'Charged',5,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-4',1,0),(5,'2016-05-02',10,'SMS','3646144',NULL,'USD','Alien Invaders Game','tel:+94123123123',NULL,NULL,'Game','REF-12345',0.15,'Charged',5,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-5',1,0),(6,'2016-05-02',10,'SMS','7069465',NULL,'USD','Alien Invaders Game','tel:+94123123123',NULL,NULL,'Game','REF-12345',0.15,'Charged',5,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-6',1,0),(7,'2016-05-02',10,'SMS','3687508',NULL,'USD','Alien Invaders Game','tel:+94123123123',NULL,NULL,'Game','REF-12345',0.15,'Charged',5,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-7',1,0),(8,'2016-05-02',10,'SMS','0987581',NULL,'USD','Alien Invaders Game','tel:+94123123123',NULL,NULL,'Game','REF-12345',0.15,'Charged',5,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-8',1,1),(9,'2016-05-02',10,'SMS','3641564','ABC','USD','Alien Invaders Game','tel:+94123123123','http://localhost:8080/mifeapiserver/callback.jsp','Example Games Inc','Game','REF-12345',0.15,'Refunded',5,'54321','0000','54321','0000',0,'src-8','0000',0,0,'paytran-9',6,0),(10,'2016-05-02',10,'SMS','9317007',NULL,'USD','Alien Invaders Game','tel:+94123123123',NULL,NULL,'Game','REF-12345',0.15,'Charged',5,NULL,NULL,NULL,NULL,0,NULL,NULL,10.15,0,'paytran-10',1,0);

--
-- Table structure for table `locationparam`
--

CREATE TABLE IF NOT EXISTS  `locationparam` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `altitude` varchar(255) DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `loc_ret_status` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `locationparam_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

--
-- Dumping data for table `locationparam`
--

INSERT INTO `locationparam` VALUES (1,'2341','45244444','135131','Retrieved',1);

--
-- Table structure for table `locationtransactionlog`
--

CREATE TABLE IF NOT EXISTS `locationtransactionlog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `requested_accuracy` double DEFAULT NULL,
  `tran_oper_status` varchar(255) DEFAULT NULL,
  `effect_date` date DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `locationtransactionlog_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

--
-- Dumping data for table `locationtransactionlog`
--

INSERT INTO `locationtransactionlog` VALUES (1,'tel:+94773524308',1000,'Retrieved','2016-04-27',1),(2,'tel:+94773524308',1000,'Retrieved','2016-04-27',1),(3,'tel:+94773524308',1000,'Retrieved','2016-04-27',1),(4,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(5,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(6,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(7,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(8,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(9,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(10,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(11,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(12,'tel:+94773524308',1000,'Retrieved','2016-08-24',4),(13,'tel:+94773524308',1000,'Retrieved','2016-08-24',4),(14,'tel:+94773524308',1000,'Retrieved','2016-08-24',1),(15,'tel:+94773524308',1000,'Retrieved','2016-08-29',1),(16,'tel:+94773524308',1000,'Retrieved','2016-08-29',1),(17,'tel:+94773524308',1000,'Retrieved','2016-08-30',1),(18,'tel:+94773524308',1000,'Retrieved','2016-08-30',1),(19,'tel:+94773524308',1000,'Retrieved','2016-08-30',1),(20,'tel:+94773524308',1000,'Retrieved','2016-08-31',1),(21,'tel:+94773524308',1000,'Retrieved','2016-08-31',1),(22,'tel:+94773524308',1000,'Retrieved','2016-08-31',1),(23,'tel:+94773524308',1000,'Retrieved','2016-09-08',1),(24,'tel:+94773524308',1000,'Retrieved','2016-09-08',1),(25,'tel:+94773524308',1000,'Retrieved','2016-09-20',1),(27,'tel:+94773524308',1000,'Retrieved','2016-09-20',1),(28,'tel:94773524308',1000,'Retrieved','2016-10-14',1),(29,'tel:94773524308',1000,'Retrieved','2016-10-14',1);

--
-- Table structure for table `mobileidapiencoderequest`
--

CREATE TABLE IF NOT EXISTS `mobileidapiencoderequest` (
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
);

--
-- Table structure for table `numbers`
--

CREATE TABLE IF NOT EXISTS `numbers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(255) DEFAULT NULL,
  `imsi` varchar(255) DEFAULT NULL,
  `mnc` int(5) DEFAULT '0',
  `mcc` int(5) DEFAULT '0',
  `num_balance` double DEFAULT NULL,
  `reserved_amount` double NOT NULL DEFAULT '0',
  `num_description` varchar(255) DEFAULT NULL,
  `num_status` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK88C28E4A9E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

--
-- Dumping data for table `numbers`
--

INSERT INTO `numbers` VALUES (1,'94773524308',4343,3413,13414,989.85,0,'test number',1,1),(2,'94123123123',1344,134413,13413,1000,0,'testAuxenta',1,5),(3,'94771564812',134134,13431,1343,1000,0,'dfaf',1,1);

--
-- Table structure for table `payment_gen`
--

CREATE TABLE IF NOT EXISTS `payment_gen` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `delivery_status` varchar(255) DEFAULT NULL,
  `max_pay_amount` varchar(255) DEFAULT NULL,
  `max_tx_perday` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKA4347D979E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

--
-- Table structure for table `payment_transaction`
--

CREATE TABLE IF NOT EXISTS `payment_transaction` (
  `transaction_id` varchar(255) NOT NULL,
  `effect_date` date DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `currency` varchar(50) DEFAULT NULL,
  `end_user_id` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  CONSTRAINT `FKB48C1E55465448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

--
-- Table structure for table `paymentparam`
--

CREATE TABLE IF NOT EXISTS `paymentparam` (
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
);

--
-- Dumping data for table `paymentparam`
--

INSERT INTO `paymentparam` VALUES (1,NULL,NULL,NULL,NULL,100000000.00,1000,'Charged',1),(2,NULL,NULL,NULL,NULL,15000.00,1000,'Processing',2),(3,NULL,NULL,NULL,NULL,15000.00,1000,'Processing',3),(4,NULL,NULL,NULL,NULL,15000.00,1000,'Processing',4),(5,NULL,NULL,NULL,NULL,10.00,1000,'Charged',5);


--
-- Table structure for table `sbxprservices`
--

CREATE TABLE IF NOT EXISTS `sbxprservices` (
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
  CONSTRAINT `fk01sbxprservice` FOREIGN KEY (`userid`) REFERENCES `user` (`id`)
);

--
-- Dumping data for table `sbxprservices`
--

INSERT INTO `sbxprservices` VALUES (1,'SRV0001','ROAM5G','ROAMING','ServiceDescription',10,'count','25',1),(2,'SRV0002','FBDATA','DATA','ServiceDescription',0,'limit','1000',1),(3,'SRV0003','VoIP','VOICE','Voice',20,NULL,NULL,1),(4,'saf','sfa','asf','afs',4,NULL,NULL,1);

--
-- Table structure for table `sbtprmsisdnservicessmap`
--

CREATE TABLE IF NOT EXISTS `sbtprmsisdnservicessmap` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `numbersid` int(11) NOT NULL,
  `servicesid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk01sbtprmsisdnservicessmap` FOREIGN KEY (`numbersid`) REFERENCES `numbers` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk02sbtprmsisdnservicessmap` FOREIGN KEY (`servicesid`) REFERENCES `sbxprservices` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);
--
-- Table structure for table `sbxstatus`
--

CREATE TABLE IF NOT EXISTS `sbxstatus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(45) DEFAULT NULL,
  `code` varchar(40) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

--
-- Dumping data for table `sbxstatus`
--

INSERT INTO `sbxstatus` VALUES (1,'Pending','PRV_PROVISION_PENDING','Provision Transaction is pendign with subsystem'),(2,'Failed','PRV_PROVISION_FAILED','Provisioning failed due to subsystem error'),(3,'Success','PRV_PROVISION_SUCCESS','Successfully provisioned'),(4,'NotActive','PRV_DELETE_NOT_ACTIVE','Service not provisioned for user'),(5,'AlreadyActive','PRV_PROVISION_ALREADY_ACTIVE','Service already active'),(6,'Pending','PRV_DELETE_PENDING','Delete Transaction is pending with subsystem'),(7,'Failed','PRV_DELETE_FAILED','Removal failed due to subsystem error'),(8,'Success','PRV_DELETE_SUCCESS','Successfully removed');

--
-- Dumping data for table `sbtprmsisdnservicessmap`
--

sbxapiservicecalls
--
-- Table structure for table `sbtprprovisionedservices`
--

CREATE TABLE IF NOT EXISTS `sbtprprovisionedservices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msisdnservicesmapid` int(11) DEFAULT NULL,
  `clientcorrelator` varchar(100) DEFAULT NULL,
  `clientreferencecode` varchar(45) DEFAULT NULL,
  `notifyurl` varchar(255) DEFAULT NULL,
  `callbackdata` varchar(45) DEFAULT NULL,
  `statusid` int(11) DEFAULT NULL,
  `createddate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk01sbtprsbtprprovisionedservices` FOREIGN KEY (`statusid`) REFERENCES `sbxstatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk02sbtprsbtprprovisionedservices` FOREIGN KEY (`msisdnservicesmapid`) REFERENCES `sbtprmsisdnservicessmap` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

--
-- Table structure for table `sbxresponsemessagecategory`
--

CREATE TABLE IF NOT EXISTS `sbxresponsemessagecategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
--
-- Table structure for table `sbxresponsemessage`
--

CREATE TABLE IF NOT EXISTS `sbxresponsemessage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `categoryid` int(11) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `message` text,
  `apitypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk01sbxresponsemessage` FOREIGN KEY (`categoryid`) REFERENCES `sbxresponsemessagecategory` (`id`),
  CONSTRAINT `fk02sbxresponsemessage` FOREIGN KEY (`apitypeid`) REFERENCES `sbxapitypes` (`id`)
);

--
-- Table structure for table `sbtprspexpectmessage`
--

CREATE TABLE IF NOT EXISTS `sbtprspexpectmessage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `numberid` int(11) NOT NULL,
  `messageid` int(11) NOT NULL,
  `requesttype` varchar(50) DEFAULT NULL,
  `servicesid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk01sbtprspexpectmessage` FOREIGN KEY (`numberid`) REFERENCES `numbers` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk02sbtprspexpectmessage` FOREIGN KEY (`messageid`) REFERENCES `sbxresponsemessage` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk03sbtprspexpectmessage` FOREIGN KEY (`servicesid`) REFERENCES `sbxprservices` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);


--
-- Table structure for table `sbxattribute`
--

CREATE TABLE IF NOT EXISTS  `sbxattribute` (
  `sbxattributedid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`sbxattributedid`)
);


--
-- Dumping data for table `sbxattribute`
--

INSERT INTO `sbxattribute` VALUES (1,'title'),(2,'firstName'),(3,'lastName'),(4,'dob'),(5,'address'),(6,'identificationType'),(7,'status'),(8,'ownerType'),(9,'accountType'),(10,'basic'),(11,'billing'),(12,'identification'),(13,'account'),(14,'additionalInfo'),(15,'identificationNumber'),(16,'clientCorrelatorWallet'),(17,'transactionStatus'),(18,'accountStatus'),(19,'currency'),(20,'payment'),(21,'refund'),(22,'sms'),(23,'data'),(24,'minutes'),(25,'referenceCodeCredit'),(26,'clientCorrelator'),(27,'applyCredit'),(28,'referenceCodeWallet'),(29,'makePayment'),(30,'clientCorrelatorPayment'),(31,'referenceCodePayment'),(32,'refundUser');
--
-- Table structure for table `sbtattributedistribution`
--

CREATE TABLE IF NOT EXISTS `sbtattributedistribution` (
  `sbtattributedistributiondid` int(11) NOT NULL AUTO_INCREMENT,
  `attributedid` int(11) NOT NULL,
  `apiservicecallsdid` int(11) NOT NULL,
  PRIMARY KEY (`sbtattributedistributiondid`),
  CONSTRAINT `fk01sbtattributedistribution` FOREIGN KEY (`attributedid`) REFERENCES `sbxattribute` (`sbxattributedid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk02sbtattributedistribution` FOREIGN KEY (`apiservicecallsdid`) REFERENCES `sbxapiservicecalls` (`sbxapiservicecallsdid`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

--
-- Dumping data for table `sbtattributedistribution`
--

INSERT INTO `sbtattributedistribution` VALUES (1,1,2),(2,2,2),(3,3,2),(4,4,2),(5,5,2),(6,6,2),(7,7,2),(8,8,2),(9,9,2),(10,10,1),(11,11,1),(12,12,1),(13,13,1),(14,14,2),(15,15,2),(16,16,4),(17,16,6),(18,17,4),(19,17,6),(20,18,7),(21,19,7),(22,20,4),(23,21,6),(24,22,8),(25,23,8),(26,24,8),(27,25,8),(28,26,8),(29,27,8),(30,28,4),(31,28,6),(32,29,10),(33,30,10),(34,31,10),(35,32,11),(36,30,11),(37,31,11);


--
-- Table structure for table `sbxattributevalue`
--

CREATE TABLE IF NOT EXISTS `sbxattributevalue` (
  `sbxattributevaluedid` int(11) NOT NULL AUTO_INCREMENT,
  `attributedistributiondid` int(11) NOT NULL,
  `tobject` varchar(45) NOT NULL,
  `ownerdid` varchar(45) NOT NULL,
  `value` text,
  PRIMARY KEY (`sbxattributevaluedid`),
  CONSTRAINT `fk01sbxattributevalue` FOREIGN KEY (`attributedistributiondid`) REFERENCES `sbtattributedistribution` (`sbtattributedistributiondid`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

--
-- Table structure for table `sbxprrequesstlog`
--

CREATE TABLE IF NOT EXISTS `sbxprrequesstlog` (
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
);



--
-- Table structure for table `send_sms_to_application`
--

CREATE TABLE IF NOT EXISTS `send_sms_to_application` (
  `sms_id` int(11) NOT NULL AUTO_INCREMENT,
  `effect_date` date DEFAULT NULL,
  `destination_address` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `sender_address` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`sms_id`),
  CONSTRAINT `FKEBE4BF499E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

--
-- Dumping data for table `send_sms_to_application`
--

INSERT INTO `send_sms_to_application` VALUES (1,'2016-08-24','7555','Hello World','94773524308',1),(2,'2016-08-24','7555','Hello World','94773524308',1),(3,'2016-08-30','7555','Hello World','94773524308',1),(4,'2016-08-30','7555','Hello World','94773524308',1);

--
-- Table structure for table `sender_address`
--

CREATE TABLE IF NOT EXISTS `sender_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `shortcode` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKB79857EA9E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

--
-- Dumping data for table `sender_address`
--

INSERT INTO `sender_address` VALUES (1,'test number','7555',1);


--
-- Table structure for table `sms`
--

CREATE TABLE IF NOT EXISTS `sms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deliveryStatus` varchar(255) DEFAULT NULL,
  `maxNotifications` varchar(255) DEFAULT NULL,
  `notificationDelay` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK1BD599E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

--
-- Table structure for table `sms_delivery_status`
--

CREATE TABLE IF NOT EXISTS `sms_delivery_status` (
  `transaction_id` varchar(255) NOT NULL,
  `sender_address` varchar(255) DEFAULT NULL,
  `delivery_status` varchar(255) DEFAULT NULL,
  `effect_date` date DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  CONSTRAINT `FK_sycg0sik20gocmm2v5oqwh2o8` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

--
-- Dumping data for table `sms_delivery_status`
--

INSERT INTO `sms_delivery_status` VALUES ('smstran-1','tel:7555','DeliveredToTerminal','2016-08-24',1),('smstran-12','tel:7555','DeliveredToTerminal','2016-08-30',1),('smstran-13','tel:7555','DeliveredToTerminal','2016-08-30',1),('smstran-14','tel:7555','DeliveredToTerminal','2016-08-30',1),('smstran-15','tel:7555','DeliveredToTerminal','2016-08-30',1),('smstran-16','tel:7555','DeliveredToTerminal','2016-08-30',1),('smstran-17','tel:7555','DeliveredToTerminal','2016-08-30',1),('smstran-4','tel:7555','DeliveredToTerminal','2016-08-24',1),('smstran-6','tel:7555','DeliveredToTerminal','2016-08-24',1),('smstran-8','tel:7555','DeliveredToTerminal','2016-08-30',1);


--
-- Table structure for table `sms_delivery_subscription`
--

CREATE TABLE IF NOT EXISTS `sms_delivery_subscription` (
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
  CONSTRAINT `FK_adwhr1k8dr8pdh9osopmeg6b6` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

--
-- Table structure for table `sms_subscription`
--

CREATE TABLE IF NOT EXISTS `sms_subscription` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sub_number` varchar(255) DEFAULT NULL,
  `sub_status` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKA48A3E439E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

--
-- Table structure for table `smsparam`
--

CREATE TABLE IF NOT EXISTS `smsparam` (
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
);

--
-- Dumping data for table `smsparam`
--


INSERT INTO `smsparam` VALUES (1,'DeliveredToTerminal','1000','10',1,NULL,NULL,NULL,NULL),(2,'DeliveredToTerminal','1000','10',2,NULL,NULL,NULL,NULL),(3,'DeliveredToTerminal','1000','10',3,NULL,NULL,NULL,NULL),(4,'DeliveredToTerminal','1000','10',4,NULL,NULL,NULL,NULL),(5,'DeliveredToTerminal','1000','10',5,NULL,NULL,NULL,NULL);


--
-- Table structure for table `smstransactionlog`
--

CREATE TABLE IF NOT EXISTS `smstransactionlog` (
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
  CONSTRAINT `FK2A1D0F729E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ;

--
-- Dumping data for table `smstransactionlog`
--

INSERT INTO `smstransactionlog` VALUES (1,'2016-08-24','[tel:+94773524308]','some-data-useful-to-the-requester','123456:1472042340971SM11001','Test1','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-1',NULL,1),(2,'2016-08-24',NULL,NULL,NULL,NULL,NULL,'7555',NULL,1,2,NULL,NULL,'success',NULL,NULL,2),(3,'2016-08-24',NULL,NULL,NULL,NULL,NULL,'tel:7555',NULL,1,0,NULL,NULL,'success',NULL,'smstran-1',5),(4,'2016-08-24','[tel:+94773524308]','some-data-useful-to-the-requester','123456:1472042912982SM11002','Test1','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-4',NULL,1),(5,'2016-08-24',NULL,'doSomething()',NULL,NULL,'http://www.yoururl.here/notifications/DeliveryInfoNotification','7555',NULL,1,0,'Vote','JSON','success',NULL,NULL,3),(6,'2016-08-24','[tel:+94773524308]','some-data-useful-to-the-requester','123456:1472043111467SM11003','Test1','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-6',NULL,1),(7,'2016-08-24',NULL,NULL,NULL,NULL,NULL,'tel:7555',NULL,1,0,NULL,NULL,'success',NULL,'smstran-6',5),(8,'2016-08-30','[tel:+94773524308]','some-data-useful-to-the-requester','123456:1472555967935SM11002','Hello World','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-8',NULL,1),(9,'2016-08-30',NULL,NULL,NULL,NULL,NULL,'tel:7555',NULL,1,0,NULL,NULL,'success',NULL,'smstran-8',5),(10,'2016-08-30',NULL,NULL,NULL,NULL,NULL,'7555',NULL,1,1,NULL,NULL,'success',NULL,NULL,2),(11,'2016-08-30',NULL,NULL,NULL,NULL,NULL,'7555',NULL,1,2,NULL,NULL,'success',NULL,NULL,2),(12,'2016-08-30','[tel:+94773524308]','some-data-useful-to-the-requester','123456:1472556483791SM11003','Test1','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-12',NULL,1),(13,'2016-08-30','[tel:+94773524308]','some-data-useful-to-the-requester','123456:1472557262623SM11004','Hello World','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-13',NULL,1),(14,'2016-08-30','[tel:+94773524308]','some-data-useful-to-the-requester','123456:1472557268910SM11005','Hello World','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-14',NULL,1),(15,'2016-08-30','[tel:+94773524308]','some-data-useful-to-the-requester','123456:1472557601440SM11006','Hello World','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-15',NULL,1),(16,'2016-08-30','[tel:+94773524308]','some-data-useful-to-the-requester','1234567890:1472557624395SM11007','Hello World','http://application.example.com/notifications/DeliveryInfoNotification','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-16',NULL,1),(17,'2016-08-30','[tel:+94773524308]','some-data-useful-to-the-requester','1234567890:1472557645748SM11008','Hello World','http://application.example.com/notifications/DeliveryInfoNotification/Test','tel:7555',NULL,1,0,NULL,NULL,'success','smstran-17',NULL,1);

--
-- Table structure for table `subscribe_sms_request`
--

CREATE TABLE IF NOT EXISTS `subscribe_sms_request` (
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
  CONSTRAINT `FKC8368A349E083448` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

--
-- Dumping data for table `subscribe_sms_request`
--

INSERT INTO `subscribe_sms_request` VALUES (1,'2016-08-24','doSomething()','12345','Vote','7555','JSON','http://www.yoururl.here/notifications/DeliveryInfoNotification',1);


--
-- Table structure for table `ussd_subscription`
--

CREATE TABLE IF NOT EXISTS `ussd_subscription` (
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
  CONSTRAINT `FK_ec81kos30iygc5p1gcfq0m9md` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

--
-- Table structure for table `ussd_transactions`
--

CREATE TABLE IF NOT EXISTS `ussd_transactions` (
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
  CONSTRAINT `FK_f6e3wvsa0gqeft93d6r8uo4qx` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

--
-- Table structure for table `sbtmessagelog`
--

CREATE TABLE IF NOT EXISTS `sbtmessagelog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `request` text,
  `servicenameid` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  `reference` varchar(100) DEFAULT NULL,
  `value` varchar(100) DEFAULT NULL,
  `messagetimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk01sbtmessagelog` FOREIGN KEY (`servicenameid`) REFERENCES `sbxapiservicecalls` (`sbxapiservicecallsdid`),
  CONSTRAINT `fk02sbtmessagelog` FOREIGN KEY (`userid`) REFERENCES `user` (`id`)
);

