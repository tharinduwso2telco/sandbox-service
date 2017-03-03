USE `sandbox`;

INSERT INTO `sbxapiservicecalls` VALUES (10,4,'ChargeUser'),(11,4,'RefundUser'),(12,4,'ListPayment'),(13,1,'Location');

INSERT INTO `sbxattribute` VALUES (29,'patialRefund'),(30,'makePayment'),(31,'clientCorrelatorPayment'),(32,'referenceCodePayment'),(33,'refundUser');

INSERT INTO `sbtattributedistribution` VALUES (32,29,9),(33,26,9),(34,25,9),(35,30,10),(36,31,10),(37,32,10),(38,33,11),(39,31,11),(40,32,11);

ALTER TABLE `sbtmessagelog` ADD (
`status` int(11) DEFAULT 0,
   `type` int(11) DEFAULT 0
 );