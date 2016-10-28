USE `<EXISTING_SANDBOX_DATABASE>`;

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

INSERT INTO `sbxapitypes` VALUES (1,'LOCATION'),(2,'SMS'),(3,'USSD'),(4,'PAYMENT'),(5,'CREDIT'),(6,'WALLET'),(7,'PROVISION');

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

INSERT INTO `sbtprmsisdnservicessmap` VALUES (1,1,1),(2,1,2),(3,2,1),(4,1,3),(5,2,2),(6,2,3),(7,3,3),(8,3,2),(9,3,1);

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



