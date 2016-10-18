#Sandbox Services

##1. Provision Service

###1.1 Introduction

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

###1.2 System Requirements

- Java SE Development Kit 1.8 
- Apache Maven 3.0.x 
- MySQL (5.6.5+)

To build the product from the source distribution both JDK and Apache Maven are required. 

No need to install Maven if you install by downloading and extracting the binary distribution (as recommended for most users) instead of building from the source code.

###1.3 Install

####1.3.1 Database Setup

MySQL Server’s time zone setting should be set to UTC time zone as ‘+00:00'.

The database script relevant for this particular service can be found at /dbscripts folder with the name of Provisioning.sql

DB tables can be created through running the script under the selection of particular sandbox Database.

####1.3.2 Configuration Setup

Folder path:	/deploy/config.yml

#####1.3.2.1 Database Configuration

user: database username

password: database password

url: url for database driver, for example jdbc:mysql://localhost/sandbox?useSSL=false

#####1.3.2.2 Log Configuration

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
The filename where current events are logged.

-- archive: 
Whether or not to archive old events in separate files.
  
-- archivedLogFilenamePattern: 
Required if archive is true.The filename pattern for archived files.%d is replaced with the date in yyyy-MM-dd format, and the fact that it ends with.gz indicates the file will be gzipped as it’s archived.Likewise, filename patterns which end in .zip will be filled as they are archived.

-- archivedFileCount: 
The number of archived files to keep.
Must be between 1 and 50. 

-- logFormat: 
Logback pattern with which events will be formatted.


###1.4 Build the Service

Run the following Maven command. This will create the fat jar dep-sanbox-1.0.2-SNAPSHOT.jar in the target directory.

```
mvn clean install
```
This fat jar is a jar file that contains sanbox microservice as well as all its dependencies.

###1.5 Run the Service

In order to get the service up and running, execute the following command.

```
java -jar target/dep-sanbox-1.0.2-SNAPSHOT.jar server deploy/config.yml
```

###1.6 API Features

####1.6.1 API features with postman testing

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
- Add new services for user

Request :

Type - POST

Request URI-
```
http://<host>:<port>/provisioning/{v1}/config/service

- Enable user defined services for given MSISDN

Request :

Type - POST

Request URI-
```
http://<host>:<port>/provisioning/{v1}/config/{msisdn}/service/{serviceCode}

- Retrieve user defined services

Request :

Type - GET

Request URI-
```
http://<host>:<port>/provisioning/{v1}/config/service

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


####1.6.2 Swagger Annotations

[Swagger](http://swagger.io/getting-started/) is a standard, language-agnostic interface to REST APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection.


In order to retrieve Swagger definitions of this microservice, go to http://&lt;host&gt;:&lt;port&gt;/swagger

For example [http://localhost:8181/swagger](http://localhost:8181/swagger)  in default configuration.
