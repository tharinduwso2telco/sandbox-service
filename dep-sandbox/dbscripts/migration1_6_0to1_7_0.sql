USE `sandbox`;

INSERT INTO `sbxapiservicecalls` VALUES (14,3,'InitUSSD'),(15,2,'SendSMS'),(16,2,'ReceivingSMS'),(17,2,'QuerySMS'),(18,2,'SubscribeToSMSDelivery'),(19,2,'SubscribeToApplication'),(20,2,'StopSubscriptionDelivery'),(21,2,'StopSubscribeToApplication');

UPDATE `sbxapitypes` SET `apiname`='SMSMESSAGING' WHERE `id`='2';


CREATE TABLE IF NOT EXISTS `ussd_applications` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `userid` int(11) NOT NULL,
    `shortCode` varchar(255) NOT NULL,
    `keyword` varchar(255) DEFAULT NULL,
     PRIMARY KEY (`id`),
     CONSTRAINT `fk01ussd_application` FOREIGN KEY (`userid`) REFERENCES `user` (`id`)
);



