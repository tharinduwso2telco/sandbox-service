USE `<EXISTING_SANDBOX_DATABASE>`;

INSERT INTO `sbxapiservicecalls` VALUES (4,6,'MakePayment'),(5,6,'ListPayment'),(6,6,'RefundPayment'),(7,6,'BalanceLookup');

INSERT INTO `sbxattribute` VALUES (16,'clientCorrelatorWallet'),(17,'transactionStatus'),(18,'accountStatus'),(19,'currency'),(20,'payment'),(21,'refund'),(28,'referenceCodeWallet');

INSERT INTO `sbtattributedistribution` VALUES (16,16,4),(17,16,6),(18,17,4),(19,17,6),(20,18,7),(21,19,7),(22,20,4),(23,21,6),(30,28,4),(31,28,6);	
