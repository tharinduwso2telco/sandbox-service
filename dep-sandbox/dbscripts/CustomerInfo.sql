USE `<EXISTING_SANDBOX_DATABASE>`;

ALTER TABLE `numbers` 
ADD COLUMN `imsi` varchar(255) DEFAULT '0' AFTER `number`,
ADD COLUMN `mnc` int(5) DEFAULT '0' AFTER `imsi`,
ADD COLUMN `mcc` int(5) DEFAULT '0' AFTER `mnc` ;


INSERT INTO sbxapitypes (id, apiname) VALUES (8, "CUSTOMERINFO");

--
-- Table structure for table `sbxapiservicecalls`
--

CREATE TABLE IF NOT EXISTS `sbxapiservicecalls` (
  `sbxapiservicecallsdid` int(11) NOT NULL AUTO_INCREMENT,
  `apitypesdid` int(11) NOT NULL,
  `service` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`sbxapiservicecallsdid`),
  CONSTRAINT `fk01sbxapiservicecalls` FOREIGN KEY (`apitypesdid`) REFERENCES `sbxapitypes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

--
-- Dumping data for table `sbxapiservicecalls`
--

INSERT INTO `sbxapiservicecalls` VALUES (1,8,'GetAttribute'),(2,8,'GetProfile');

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
  `message-timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk01sbtmessagelog` FOREIGN KEY (`servicenameid`) REFERENCES `sbxapiservicecalls` (`sbxapiservicecallsdid`),
  CONSTRAINT `fk02sbtmessagelog` FOREIGN KEY (`userid`) REFERENCES `user` (`id`)
);


--
-- Table structure for table `sbxattribute`
--

CREATE TABLE IF NOT EXISTS `sbxattribute` (
  `sbxattributedid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`sbxattributedid`)
);

--
-- Dumping data for table `sbxattribute`
--

INSERT INTO `sbxattribute` VALUES (1,'title'),(2,'firstname'),(3,'lastname'),(4,'dob'),(5,'address'),(10,'id_type'),(11,'status'),(12,'owner_type'),(13,'account_type'),(14,'basic'),(15,'billing'),(16,'identification'),(17,'account'),(18,'additional_info'),(19,'id_number');

--
-- Table structure for table `sbtattributedistribution`
-

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

INSERT INTO `sbtattributedistribution` VALUES (1,1,2),(2,2,2),(3,3,2),(4,4,2),(5,5,2),(6,10,2),(7,11,2),(8,12,2),(9,13,2),(10,14,1),(11,15,1),(12,16,1),(13,17,1),(14,18,2),(15,19,2);

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


