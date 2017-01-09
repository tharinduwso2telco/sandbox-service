USE `<EXISTING_SANDBOX_DATABASE>`;

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

INSERT INTO `sbxapiservicecalls` VALUES (8,5,'ApplyCredit'),(9,5,'PartialRefund');

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

INSERT INTO `sbxattribute` VALUES (22,'sms'),(23,'data'),(24,'minutes'),(25,'referenceCodeCredit'),(26,'clientCorrelator'),(27,'applyCredit');

--
-- Table structure for table `sbtattributedistribution`

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

INSERT INTO `sbtattributedistribution` VALUES (24,22,8),(25,23,8),(26,24,8),(27,25,8),(28,26,8),(29,27,8);	

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



