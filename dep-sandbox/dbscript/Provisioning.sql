USE `dev2_mife_sandbox_hub`;

CREATE TABLE IF NOT EXISTS  `sbtprmsisdnservicessmap` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `numbersid` int(11) NOT NULL,
  `servicesid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
);
INSERT INTO `sbtprmsisdnservicessmap` VALUES (1,1,1),(2,1,2),(3,2,1),(4,1,3),(5,2,2),(6,2,3);

CREATE TABLE IF NOT EXISTS `sbtprprovisionedservices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msisdnservicesmapid` int(11) DEFAULT NULL,
  `clientcorrelator` varchar(100) DEFAULT NULL,
  `clientreferencecode` varchar(45) DEFAULT NULL,
  `notifyurl` varchar(255) DEFAULT NULL,
  `callbackdata` varchar(45) DEFAULT NULL,
  `statusid` int(11) DEFAULT NULL,
  `createddate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);
INSERT INTO `sbtprprovisionedservices` VALUES (3,3,'',NULL,NULL,NULL,1,'2016-09-13 17:53:07'),(5,5,NULL,NULL,NULL,'',3,'2016-09-13 17:54:58'),(6,6,NULL,NULL,NULL,NULL,2,'2016-09-13 17:54:58'),(7,1,'1345:0001:12','REF12345','https://gateway1a.mife.sla-mobile.com.my:8243/provisioning/v1/ProvisionNotification/69','some-data-useful-to-the-requester',6,'2016-10-10 10:40:11'),(8,2,'1345','REF12345','http://application.example.com/notifications/DeliveryInfoNotification','some-data-useful-to-the-requester',3,'2016-09-30 05:36:21');

CREATE TABLE IF NOT EXISTS  `sbtprspexpectmessage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `numberid` int(11) NOT NULL,
  `messageid` int(11) NOT NULL,
  `requesttype` varchar(50) DEFAULT NULL,
  `servicesid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
);
INSERT INTO `sbtprspexpectmessage` VALUES (1,1,1,'PROVISION',1);

CREATE TABLE IF NOT EXISTS `sbxapitypes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiname` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
);
INSERT INTO `sbxapitypes` VALUES (1,'LOCATION'),(2,'SMS'),(3,'USSD'),(4,'PAYMENT'),(5,'CREDIT'),(6,'WALLET'),(7,'PROVISION'),(8,'CUSTOMER');

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
INSERT INTO `sbxprrequesstlog` VALUES (181,'LIST_ACTIVE_PROVISIONED_SERVICES','tel:+94773524308',1,NULL,NULL,NULL,NULL,'2016-09-24'),(182,'DELETE_PROVISION_SERVICE','tel:+94773524308',1,'1345','REF12345','http://application.example.com/notifications/DeliveryInfoNotification','some-data-useful-to-the-requester','2016-09-24'),(183,'DELETE_PROVISION_SERVICE','tel:+94773524308',1,'1345','REF12345','http://application.example.com/notifications/DeliveryInfoNotification','some-data-useful-to-the-requester','2016-09-24');

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
  PRIMARY KEY (`id`)
);
INSERT INTO `sbxprservices` VALUES (1,'SRV0001','ROAM5G','ROAMING','ServiceDescription',10,'count','25',1),(2,'SRV0002','FBDATA','DATA','ServiceDescription',0,'limit','1000',1),(3,'SRV0003','VoIP','VOICE','Voice',20,NULL,NULL,1);

CREATE TABLE IF NOT EXISTS `sbxresponsemessage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `categoryid` int(11) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `message` text,
  `apitypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
INSERT INTO `sbxresponsemessage` VALUES (1,1,'SVC0009','Service Related Error',7);

CREATE TABLE IF NOT EXISTS `sbxresponsemessagecategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
INSERT INTO `sbxresponsemessagecategory` VALUES (1,'SERVICEEXCEPTION'),(2,'POLICYEXCEPTION'),(3,'General Error');

CREATE TABLE IF NOT EXISTS `sbxstatus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(45) DEFAULT NULL,
  `code` varchar(40) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
INSERT INTO `sbxstatus` VALUES (1,'Pending','PRV_PROVISION_PENDING','Provision Transaction is pendign with subsystem'),(2,'Failed','PRV_PROVISION_FAILED','Provisioning failed due to subsystem error'),(3,'Success','PRV_PROVISION_SUCCESS','Successfully provisioned'),(4,'NotActive','PRV_DELETE_NOT_ACTIVE','Service not provisioned for user'),(5,'AlreadyActive','PRV_PROVISION_ALREADY_ACTIVE','Service already active'),(6,'Pending','PRV_DELETE_PENDING','Delete Transaction is pending with subsystem'),(7,'Failed','PRV_DELETE_FAILED','Removal failed due to subsystem error'),(8,'Success','PRV_DELETE_SUCCESS','Successfully removed');
