#Sandbox Services

##1 System Requirements

- Java SE Development Kit 1.8 
- Apache Maven 3.0.x 
- MySQL (5.6.5+)

To build the product from the source distribution both JDK and Apache Maven are required. 

No need to install Maven if you install by downloading and extracting the binary distribution (as recommended for most users) instead of building from the source code.

##2 Install

###2.1 Database Setup

MySQL Server’s time zone setting should be set to UTC time zone as ‘+00:00'.

The database script relevant for this particular release version can be found at /dbscripts folder.

- If the sandbox DataBase is going to be set-up for the first time then refer the sql script with the name of dep-sandbox<<version>>.sql like "dep-sandbox1_3_0.sql"

- If the existing sandbox Database is going to be used then refer the sql script with name of migration upgrade version like "migration1_2_0to1_3_0.sql"

DB tables can be created through running the script under the selection of particular sandbox Database.

Note: If there is no any migration versioned script under dbscripts folder, then that means there is no any db upgrade with particular release version.

###2.2 Configuration Setup

Folder path:	/deploy/config.yml

####2.2.1 Database Configuration

user: database username

password: database password

url: url for database driver, for example jdbc:mysql://localhost/sandbox?useSSL=false

####2.2.2 Log Configuration

- Level:

Logback logging level. ‘INFO’ Designates informational messages that highlight the progress of the application at coarse-grained level. 

- Loggers:

io.dropwizard for INFO

com.wso2telco for DEBUG

- Appenders:

Locations to where the logging messages should be displayed or written

-- type:
Console / File

-- threshold: 
The lowest level of events to print to the console/ write to the file.

-- timeZone: 
The time zone to which event timestamps will be converted.
 
-- target: 
The name of the standard stream to which events will be written.
Can be stdout or stderr. 

-- currentLogFilename: 
The folder location where current events are logged can be mentioned under this tag
for eg :- log/sandbox_service.log 

-- archive: 
Whether or not to archive old events in separate files.
  
-- archivedLogFilenamePattern: 
Required if archive is true.The filename pattern for archived files.%d is replaced with the date in yyyy-MM-dd format, and the fact that it ends with.gz indicates the file will be gzipped as it’s archived.Likewise, filename patterns which end in .zip will be filled as they are archived.

-- archivedFileCount: 
The number of archived files to keep.
Must be between 1 and 50. 

-- logFormat: 
Logback pattern with which events will be formatted.


##3 Build the Service

Run the following Maven command. This will create the fat jar dep-sanbox-<VERSION>.jar in the target directory.

```
mvn clean install
```
This fat jar is a jar file that contains sanbox microservice as well as all its dependencies.

##4 Run the Service

In order to get the service up and running, execute the following command.

```
java -jar target/dep-sanbox-<VERSION>.jar server deploy/config.yml
```
The rest calls should be exposed to SP publicly through LB as this is a standalone micro service which will cater apis : provisioning,customerInfo,credit upto now.

##5 Swagger Annotations

[Swagger](http://swagger.io/getting-started/) is a standard, language-agnostic interface to REST APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection.


In order to retrieve Swagger definitions of this microservice, go to http://&lt;host&gt;:&lt;port&gt;/swagger

For example [http://localhost:8181/swagger](http://localhost:8181/swagger)  in default configuration.

##6 API Features

Please use header key as "sandbox" and value as username to invoke below given rest calls for authentication if not routing through APIM.(if not using sandbox token)

###6.1. User Service

- Manage Numbers

This servivce is used to define user specific number for sandbox usage.

Request :

Type - POST

Request URI:
```
http://<host>:<port>/user/managenumber
```
Request Body :
```
{
  "number": "",
  "numberBalance": 0,
  "reservedAmount": 0,
  "description": "",
  "status": 1,
  "imsi": "",
  "mcc": 0,
  "mnc": 0
}
```

Response :

200 OK will be returned if the service is successfully added for the user.
Unless 400 Bad Request will be returned

- Get available APITypes

This service can be used to list out all the apis that are currently available.

Request :

Type - GET

Request URI:
```
http://<host>:<port>/user/apiType
```

Response :
```
{
  "apiTypes": [
    "LOCATION",
    "SMS",
    "USSD",
    "PAYMENT",
    "CREDIT",
    "WALLET",
    "PROVISIONING",
    "CUSTOMERINFO"
  ]
}
```

- Get available API specific ServiceTypes

This service can be used to list out all the rest service calls under specific api that are currently available. 
So that the apis that are listed through above service can be used here as parameter to retrieve associated serviceTypes

Request :

Type - GET

Request URI:
```
http://<host>:<port>/user/{apiType}/serviceType
```

Response :
```
{
  "apiServiceCallTypes": [
    "GetAttribute",
    "GetProfile"
  ]
}
```

- Get available APIServiceType specific Attributes

This service can be used to list out all the attributes that are defined under each service call of specific api.
So that, above mentioned two services can be used to set the parameters of this particular service.

Request :

Type - GET

Request URI:
```
http://<host>:<port>/user/{apiType}/{serviceType}/attribute
```

Response :
```

{
  "attributes": [
    "title",
    "firstname",
    "lastname",
    "dob",
    "address",
    "id_type",
    "id_status",
    "owner_type",
    "account_type",
    "additional_info",
    "id_number"
  ]
}
```


###6.2. Provision Service

####6.2.1 Introduction

Provision service will provide the Service providers a list of provision services available for the given MSISDN and 
based on the services available service providers can provision and unprovision the services. Basically provision API supports
four operations.

- Query Applicable - List the applicable services for a given MSISDN
- Provision Service - Provision a service on to the specified MSISDN
- Remove Service - Remove a provisioned service from a MSISDN
- List service by customer - List the provisioned services for a given MSISDN

Provisioning Related User Configurations can also be done through rest service calls. Configuration for provisioning API has three operations.

- Add new services for user
- Enable user defined services for given MSISDN
- Retrieve user defined services

####6.2.2 API features with postman testing

- Query Applicable- List the applicable services for a given MSISDN


Request :

Type - GET

Resource URI:
```
http://<host>:<port>/provisioning/v1/{msisdn}
```

Response :
```
{
  "serviceList": {
    "serviceInfo": [
      {
        "serviceCode": "SRV0003",
        "serviceType": "SMS",
        "description": "Mobitel SMS Description",
        "serviceCharge": 20
      },
      {
        "serviceCode": "SRV0001",
        "serviceType": "ROAMING",
        "description": "ServiceDescription",
        "serviceCharge": 10
      }
    ],
    "currencyCode": currencyCode,
    "resourceURL": "http://<host>:<port>/provisioning/v1/{msisdn}/list/applicable"
  }
}
```

- Provision Service - Provision a service on to the specified MSISDN

Request :

Type - POST

Resource URI :
```
http://<host>:<port>/provisioning/v1/{msisdn}
```
Request Body :
```
{
	"serviceProvisionRequest":{
		"serviceCode":"SRV0001",
		"clientCorrelator": "clientCorrelator",
		"clientReferenceCode" : "clientReferenceCode",
		"callbackReference":{
			"notifyURL":"notifyURL",
			"callbackData":"callbackData"
		}
	}
}
```

Response :

```
{
  "serviceProvisionResponse": {
    "serviceCode": "SRV0001",
    "clientCorrelator": "clientCorrelator",
    "clientReferenceCode": "clientReferenceCode",
    "serverReferenceCode": "serverReferenceCode",
    "callbackReference": {
      "notifyURL": "notifyURL",
      "callbackData": "callbackData",
      "resourceURL": "http://<host>:<port>/provisioning/v1/{msisdn}"
    },
    "transactionStatus": "AlreadyActive"
  }
}
```

- Remove Service - Remove a provisioned service from a MSISDN

Request : 

Type - POST

Request URI:
```
http://<host>:<port>/provisioning/v1/{msisdn}/remove
```
Request Body :
```
{
	"serviceRemoveRequest":{		
				"serviceCode": "SRV0001",
				"clientCorrelator": "clientCorrelator",
				"clientReferenceCode" : "clientReferenceCode",
				"callbackReference" : {
        					"notifyURL": "notifyURL",
            				"callbackData": "callbackData"
    			}
    }
}
```

Response :
```
{
  "serviceRemoveResponse": {
    "serviceCode": "SRV0001",
    "clientCorrelator": "clientCorrelator",
    "clientReferenceCode": "clientReferenceCode",
    "serverReferenceCode": "serverReferenceCode",
    "callbackReference": {
      "notifyURL": "notifyURL",
      "callbackData": "callbackData",
      "resourceURL": "http://<host>:<port>/provisioning/v1/{msisdn}/remove"
    },
    "transactionStatus": "Pending"
  }
}
```

- List service by customer - List the provisioned services for a given MSISDN

Request :

Type - GET

Request URI-
```
http://<host>:<port>/provisioning/v1/{msisdn}/list/active
```

Response :
```
{
  "serviceList": {
    "serviceInfo": [
      {
        "serviceCode": "SRV0001",
        "description": "ServiceDescription",
        "serviceInfo": [
          {
            "tag": "count",
            "value": "25"
          }
        ],
        "timeStamp": "timestamp"
      },
      {
        "serviceCode": "SRV0002",
        "description": "ServiceDescription",
        "serviceInfo": [
          {
            "tag": "limit",
            "value": "1000"
          }
        ],
        "timeStamp": "timestamp"
      }
    ],
    "resourceURL": "http://<host>:<port>/provisioning/v1/{msisdn}/list/active"
  }
}
```


####6.2.3 Provisioning API Related User Configurations postman testing


- Add new services for user

This service gives the flexibity for the user to define whatever the service that should be added in order to get use of it.
The defined services through this will be used as Provisioning API service call data. 

Request :

Type - POST

Request URI-
```
http://<host>:<port>/provisioning/{v1}/config/service
```
Request Body :
```
{
    "serviceCode": "sample1",
    "serviceName": "sample2",
    "serviceType": "sample3",
     "description": "sample4",
    "serviceCharge": 100
}
```
Response :
200 OK will be returned if the service is successfully added for the user.
Unless 400 Bad Request will be returned


- Retrieve user defined services

This service is used to list out all the user defined services used for the provisioning api.

Request :

Type - GET

Request URI-
```
http://<host>:<port>/provisioning/{v1}/config/service
```

Response :
```

{
  "serviceInfoList": [
    {
      "serviceCode": "SRV0001",
      "serviceType": "ROAMING",
      "description": "ServiceDescription",
      "serviceCharge": 10
    },
    {
      "serviceCode": "SRV0002",
      "serviceType": "DATA",
      "description": "ServiceDescription",
      "serviceCharge": 0
    }
  ]
}
```

- Enable user defined services for given MSISDN

This service is used to make the user defined services to be enabled for the MSISDN. So the list applicable service call will use the data inserted through this config call while retriving.
User can get user defined services from above mentioned get call and use this service to add the retrieved service to enable it for given MSISDN

Request :

Type - POST

Request URI-
```
http://<host>:<port>/provisioning/{v1}/config/{msisdn}/service/{serviceCode}
```
Response :
200 OK will be returned if the service is successfully added for the user.
Unless 400 Bad Request will be returned

###6.2.4 Current Limitations for Service Provider

- Provision and Un-Provision Service calls are designed to give "Pending" as default Transaction Status. So "Success" status cannot be generated for both Provision and Un-Provision.

- Un-Provision cannot be invoked from SP side due to the above mentioned limitation (In order to invoke Un-Provision, the Database should have successfully provisioned data.)

- Notification Trigger is not implemented so that Service Provider's notify url will not be notified at all for sandbox.

- Provisioning API and it's configurations can only be catered through sandbox microservice (Swagger), other existing non-api specific thing such as Manage Numbers should be done in existing flow from Sandbox UI.





###6.3 Customer Info Service


####6.3.1 Introduction

Customer Info service will provide the Service providers a list of customer info services available for the given MSISDN/IMSI. Basically Customer Info API supports
2 operations.

- Get Profile - Get a customer’s basic profile information 
- Get Attributes- Get a customer’s basic profile information and registered schema

####6.3.2 CustomerInfo API Related User Pre-Configurations postman testing

The customerinfo configurations are made to respond irrespective of different msisdn added by a specific user. So the configured details will be stored against user, not the msisdn. Trying with different msisdn to set details will overwrite the existing records of specific user. 

Use the below provided seperate rest calls to insert records seperately for get profile & get attribute call. So it will be stored per service call configuration per user.

- Post values for APIServiceType specific Attributes

This service can be used to insert values for the attributes that are using for customerinto rest service.
The values that are inserted using this service will be retrieved when actual CutomerInfo API GET service calls are invoked.

- Add new Profiles for user

Request :

Type - POST

Request URI:
```
http://<host>:<port>/customerinfo/{v1}/config/customer/profile
```
Request Body :
```
{
		"title": "Mr",
        "firstName": "Bilbo",
        "lastName": "Baggins",
        "dob": "2006/10/21",
        "identificationType": "PP",
        "identificationNumber": "PP12345DC",
        "accountType": "Postpaid",
        "ownerType": "Paymaster",
        "status": "Confirmed",
        "address":{
			"line1": "Bag End",
			"line2": "",
			"line3": "",
			"city": "The Shire",
			"country": "Middle Earth"
		},
        "additionalInfo":[{
			"tag": "creditLimit",
			"value": "2500"
	},{
			"tag": "creditLimit",
			"value": "2500"
	}]
}
```
Response :
200 OK with body as below, if the service is successfully added for the user.

```
{
  "status": "Successfully Updated user profile!!!"
}
```
Unless 400 Bad Request will be returned

- Add new Attributes for user

Request :

Type - POST

Request URI:
```
http://<host>:<port>/customerinfo/{v1}/config/customer/attribute
```
Request Body :
```
{
		"basic": {
			"title": "Mr",
			"firstName": "Bilbo",
			"lastName": "Baggins",
			"dob": "2006/10/21",
			"address": {
				"line1": "Bag End",
				"line2": "",
				"line3": "",
				"city": "The Shire",
				"country": "Middle Earth"
			}
		},
		"billing": {
			"creditLimit": "2500",
			"balance": "1000",
			"outStanding": "0",
			"currency": "LKR"
		},
		"identification": {
			"type": "Passport",
			"number": "N123456",
			"expiry": "2026/01/01"
		},
		"account": {
			"type": "Postpaid",
			"status": "Active"
		}
}
```
Response :

200 OK with body as below, if the attribute is successfully added for the user.
```
{
  "status": "Successfully Updated user attribute!!!"
}
```
Unless 400 Bad Request will be returned

Note : Service Provider should be aware of the mandatory/optional parameters according to specification so that they will be validated while data insertion in customerInfo configuration service.

####6.3.3 API features with postman testing

- Get Profile- Get a customer’s basic profile information

Request :

Type - GET

Request URI:
```
http://<host>:<port>/customerinfo/v1/customer/profile?msisdn={msisdn}&imsi={imsi}&mnc={mnc}&mcc={mcc}&onBehalfOf={onBehalfOf}&purchaseCategoryCode={PurchaseCategoryCode}&requestIdentifier={Unique Request ID}
```

Response :
```
{
  "Customer": {
        "msisdn": "123456789",
        "imsi": "0987654321", 
        "title": "Mr",
        "firstName": "Bilbo",
        "lastName": "Baggins",
        "dob": "21/10/2006",
        "identificationType": "PP",
        "identificationNumber": "PP12345DC",
        “onBehalfOf”:”my_Merchant”,
		“purchaseCategoryCode”:”Game”,
        "accountType": "Postpaid",
        "ownerType": "Paymaster",
        "status": "Confirmed",
        “requestIdentifier”:”REQ12345678”,
		“responseIdentifier”:”RES12345678”,
        "address":{
			"line1": "Bag End",
			"line2": "",
			"line3": "",
			"city": "The Shire",
			"country": "Middle Earth"
		},
        "additionalInfo":[{
			"tag": "creditLimit",
			"value": "2500"
	},{
			"tag": "creditLimit",
			"value": "2500"
	}],
       "resourceURL": "http://<host>:<port>/customerinfo/v1/customer/profile/{UniqueID}"
  }
}
```

- Get Attributes- Get a customer’s basic profile information and registered schema

Request :

Type - GET

Request URI:
```
http://<host>:<port>/customerinfo/v1/customer/attribute?msisdn={msisdn}&imsi={imsi}&schema={schema1,schema2,schema3,schema4}&mnc={mnc}&mcc={mcc}&onBehalfOf={OnBehalfOf}&purchaseCategoryCode={purchaseCategoryCode}&requestIdentifier={Unique Request ID}
```

Response :
```
{
	"Customer": {
		"msisdn": "123456789",
		"imsi": "0987654321",
		“onBehalfOf”:”my_Merchant”,
		“purchaseCategoryCode”:”Game”,
		“requestIdentifier”:”REQ12345678”,
		“responseIdentifier”:”RES12345678”,
		"basic": {
			"title": "Mr",
			"firstName": "Bilbo",
			"lastName": "Baggins",
			"dob": "2006/10/21",
			"address": {
				"line1": "Bag End",
				"line2": "",
				"line3": "",
				"city": "The Shire",
				"country": "Middle Earth"
			}
		},
		"billing": {
			"creditLimit": "2500",
			"balance": "1000",
			"outStanding": "0",
			"currency": "LKR"
		},
		"identification": {
			"type": "Passport",
			"number": "N123456",
			"expiry": "2026/01/01"
		},
		"account": {
			"type": "Postpaid",
			"status": "Active"
		},
		"resourceURL": "http://<host>:<port>/customerinfo/v1/customer/attribute/{UniqueID}"
	}
}
```

####6.3.4 Current Limitations for Service Provider

purchaseCategoryCode is currently not maintained in DB level of sandbox, where it is actually maintained for validation at HUB level. So the validation for this parameter will pass in sandbox, eventhough it fails at HUB.


###6.4 Credit Service


####6.4.1 Introduction

Credit service will provide the Service providers a list of credit services available for the given MSISDN. Basically Credit API supports 2 operations.

- Apply a Credit - Apply a credit
- Partial Refund - Perform a partial refund


####6.4.2 API features with postman testing

- Apply a Credit - Apply a credit

Request :

Type - POST

Request URI:
```
http://<host>:<port>/credit/{v1}/credit/{msisdn}/apply 
```

Request Body :
```
{
	"creditApplyRequest":{
		"type":"type",
		"amount":0,
		"clientCorrelator":"clientCorrelator",
		"reasonForCredit":"reasonForCredit",
		"merchantIdentification":"merchantIdentification",
		"receiptRequest":{
			"notifyURL":"notifyURL",
			"callbackData":"callbackData"
		},
	"referenceCode":"12345"
	}
}
```

Response :
```
{
  "creditApplyResponse": {
    "type": "type",
    "amount": 0,
    "clientCorrelator": "clientCorrelator",
    "reasonForCredit": "reasonForCredit",
    "merchantIdentification": "merchantIdentification",
    "status": "status",
    "receiptResponse": {
      "notifyURL": "notifyURL",
      "callbackData": "callbackData",
      "resourceURL": "http://<host>:<port>/credit/{v1}/credit/{msisdn}/apply"
    },
	"referenceCode":"12345",
	"serverReferenceCode":"REF_00001"
  }
}
```

- Partial Refund - Perform a partial refund

Request :

Type - POST

Request URI:
```
http://<host>:<port>/{v1}/credit/{msisdn}/refund 
```

Request Body :
```
{
	"refundRequest":{
		"amount":0,
		"clientCorrelator":"clientCorrelator",
		"reasonForRefund":"reasonForRefund",
		"merchantIdentification":"merchantIdentification",
		"serverTransactionReference":"serverTransactionReference",
		"receiptRequest":{
			"notifyURL":"notifyURL",
			"callbackData":"callbackData"
		}
	}
}
```

Response :
```
{
  "refundResponse": {
    "amount": 0,
    "serverTransactionReference": "serverTransactionReference",
    "clientCorrelator": "clientCorrelator",
    "reasonForRefund": "reasonForRefund",
    "merchantIdentification": "merchantIdentification",
    "receiptResponse": {
      "notifyURL": "notifyURL",
      "callbackData": "callbackData",
      "resourceURL": "http://<host>:<port>/{v1}/credit/{msisdn}/refund"
    }
  }
}
```


###6.5 Wallet Service

####6.5.1 Introduction
Wallet service will provide the Service providers a list of wallet services available for the given MSISDN and based on the services available service provider can make/refund payment. Basically Wallet API supports 4 operations.

- Make Payment - Charge a subscriber for a service provided by a the Service Provider
- List Transactions - Return all the transactions of the end user for the calling application
- Refund - Refund an end user
- Balance lookup - Check the account balance of an end user

####6.5.2 API features with postman testing

- Make Payment - Charge a subscriber for a service provided by a the Service Provider

Request : 

Type - POST

Request URI:
```
http://<host>:<port>/wallet/{v1}/transaction/{endUserId}/payment
```
Request Body :
```
{
	"makePayment": {
		"clientCorrelator": "54321",
		"endUserId": "tel:+00123456789",
		"paymentAmount": {
			"chargingInformation": {
				"amount": "2",
				"currency": "LKR",
				"description": "Alien Invaders Game"
			},
			"chargingMetaData": {
				"onBehalfOf": "Example Games Inc",
				"purchaseCategoryCode": "Game",
				"channel": "Mobile"
			}
		},
		"referenceCode": "REF-12345",
		“notifyURL”: “http: //myapplication/mynotifyurl”
	}
}
```

Response :
```
{
	"makePayment": {
		"clientCorrelator": "54321",
		"endUserId": "tel:+00123456789",
		"paymentAmount": {
			"chargingInformation": {
				"amount": "2",
				"currency": "LKR",
				"description": "Alien Invaders Game"
			},
			"chargingMetaData": {
				"onBehalfOf": "Example Games Inc",
				"purchaseCategoryCode": "Game",
				"channel": "Mobile"
			}
		},
		"referenceCode": "REF-12345",
		“serverReferenceCode”: “WALLET_REF_XX67675XX”,
		“notifyURL”: “http: //myapplication/mynotifyurl”,
		“transactionOperationStatus”: “Charged”
	}
}
```

- List Transactions - Return all the transactions of the end user for the calling application

Request :

Type - GET

Request URI-
```
http://<host>:<port>/wallet/{v1}/transaction/{endUserId}/list
```

Response :
```
{
	"paymentTransactionList": {
		"amountTransaction": [{
			"endUserId": " tel:+00123456789",
			"paymentAmount": {
				"chargingInformation": {
					"amount": "9",
					"currency": "USD",
					"description": "Alien Invaders"
				}
			},
			"referenceCode": "REF-ASM600-239238",
			"serverReferenceCode": "tx-a3c0e4e006da40a8a5b5-045972478cc3",
			"transactionOperationStatus": "Charged"
		}, {
			"endUserId": " tel:+00123456789",
			"paymentAmount": {
				"chargingInformation": {
					"amount": "6",
					"currency": "USD",
					"description": " Snakes Alive "
				}
			},
			"referenceCode": "REF-ASM600-2392344",
			"serverReferenceCode": "tx-a3c0e4e006da60a8a5b5-044972478cc3",
			"transactionOperationStatus": "Charged"
		}],
		"resourceURL": ” < Resource URL > ”
	}
}

```

- Refund - Refund an end user

Request : 

Type - POST

Request URI:
```
http://<host>:<port>/wallet/{v1}/transaction/{endUserId}/refund
```
Request Body :
```
{
	"refundTransaction": {
		"clientCorrelator": "54321",
		"endUserId": "tel:+00123456789",
		“originalReferenceCode”: ”ABC - 12345” "originalServerReferenceCode": " WALLET_REF_XX67675XX",
		"paymentAmount": {
			"chargingInformation": {
				"amount": "10",
				"currency": "USD",
				"description": "Alien Invaders Game"
			},
			"chargingMetaData": {
				"onBehalfOf": "Example Games Inc",
				"purchaseCategoryCode": "Game",
				"channel": "WAP"
			}
		},
		"referenceCode": "REF-12345"
	}
}
```

Response :
```
{
	"refundTransaction": {
		"clientCorrelator": "54321",
		"endUserId": "tel:+00123456789",
		“originalReferenceCode”: ”ABC - 12345” "originalServerReferenceCode": " WALLET_REF_XX67675XX",
		"paymentAmount": {
			"chargingInformation": {
				"amount": "10",
				"currency": "USD",
				"description": "Alien Invaders Game"
			},
			"chargingMetaData": {
				"onBehalfOf": "Example Games Inc",
				"purchaseCategoryCode": "Game",
				"channel": "WAP"
			}
		},
		"referenceCode": "REF-12345",
		"serverReferenceCode": "ABC-123",
		"resourceURL": "<Resource URL>",
		"transactionOperationStatus": "Refunded"
	}
}
```

- Balance lookup - Check the account balance of an end user

The account status and currency should configure using "Add account information for msisdn" in wallet api related user configuration before call "Balance lookup" service

Request :

Type - GET

Request URI-
```
http://<host>:<port>/wallet/{v1}/transaction/{endUserId}/balance
```

Response :
```
{
	"accountBalance": {
		"endUserId": "tel:+00123456789",
		"serverReferenceCode": " WALLET_REF_XX67675XX",
		"accountInfo": {
			"accountStatus": "Active",
			"accountCurrency": "LKR",
			"accountBalance": “500.45”
		}
		"resourceURL": "<Resource URL>"
	}
}

```

####6.5.3 Wallet API Related User Configurations postman testing

- Retrieve and Post values for Transaction

This service can be used to get and insert values for the attributes that are using for wallet rest service.
The values that are inserted using this service will be retrieved when actual Wallet API GET service call is invoked.

- Get transaction status for msisdn

Request :

Type - GET

Request URI:
```
http://<host>:<port>/wallet/{v1}/config/{apiType}/{serviceType}/getTransactionStatus
```
Response :
```
{
  "status": "Refused"
}
```
- Get account status for msisdn

Type - GET

Request URI:
```
http://<host>:<port>/wallet/{v1}/config{apiType}/{serviceType}/getAccountStatus
```
Response :
```
{
	"accountStatus": [
		"ACTIVE",
		"SUSPENDED",
		"TERMINATED"
	]
}
```

- Add transaction status for msisdn

Request :

Type - POST

Request URI:
```
http://<host>:<port>/wallet/{v1}/config/{endUserId}/addTransactionStatus
```
Request Body :
```
{
	"serviceCall": "MakePayment",
	"status": "Refused"
}
```
Response :
200 OK will be returned if the service is successfully added for the msisdn.
Unless 400 Bad Request will be returned

- Add account information for msisdn

Request :

Type - POST

Request URI:
```
http://<host>:<port>/wallet/{v1}/config/{endUserId}/addAccountInfo
```
Request Body :
```
{
	"currency": "LKR",
	"status": "ACTIVE"
}
```
Response :
200 OK will be returned if the service is successfully added for the msisdn.
Unless 400 Bad Request will be returned

