# Token Pool Service

## 1. Introduction 
  

The current Token Regeneration process between two OAuth2 secured entities (Hub-GW, Hub-Hub, etc.) is maintained by the two entities exchanging access tokens and the tokens being refreshed at predefined intervals where the regeneration is done only at the instance of failure. This leads to the unavailability window during the time it takes to refresh on failure. So this Token Pool Service is designed to overcome the above described scenario.

This service will have a pool of tokens in the hub, per GW, which has the ability for continuous monitoring of token pool to refresh any token that has an expiration time closer to or above the token refresh interval.

  

## 2.  Install 
  

### 2.1 System Requirements

- Java SE Development Kit 1.8 
- Apache Maven 3.0.x 

To build the product from the source distribution both JDK and Apache Maven are required. 

If you are installing by downloading and extracting the binary distribution (as recommended for most users) instead of building from the source code, you do not need to install Maven.


### 2.2 Database Setup
 

MySQL Server’s time zone setting should be set to UTC time zone as ‘+00:00'

The database script relevant for this service can be found at /dbscripts folder.

DB can be created through running the script which in result will create schema named ‘token_service’ with three tables included namely ‘tstevent’,’tsttoken’ and ’tsxevent’.



### 2.3 Metadata  

DB consists mainly of owner and token details with event tracking. The description for each attribute can be found at  /design/ERD_doc.pdf

Owner :

In a scenario of “Hub” needs to trigger api calls at operator it need to have a valid operator generated access-token/refresh token at hub. Then using the above access token hub can trigger southbound /operator apis.In this scenario the “who” is operator name.

One owner can have only one valid token per each tokenauth in ‘tsttoken’ table.

There should be at least one record in ‘tsxwho’ table in order to start this server. Or otherwise the server will terminate along with exception throwing namely “Unable to start the server. TPS0006:No Valid token owner defined at the db”

Sample Insertion SQL:

“ INSERT INTO `tsxwho` (`ownerid`, `tokenurl`, `defaultconnectionresettime`, `isvalid`) VALUES ('owner2', 'https://localhost:8243/token', '4000', 1);”
  

### 2.4 Configuration Setup

Folder path:/deploy/config.yml

  

#### 2.4.1 Database Configuration 

user: database username
password: database password
url: url for database driver, by default jdbc:mysql://localhost/token_service
 

#### 2.4.2 Server Configuration


- applicationConnectors: 
A set of connectors which will handle application requests. 

- adminConnectors: 
A set of connectors which will handle admin requests. 

- port: 
TCP/IP port on which to listen for incoming connections.
Built-in default is an HTTP connector listening on port 8181, override if needed

- bindHost: 
The hostname to bind to.
 

#### 2.4.3 Log configuration

  
  

Level:

Logback logging level. ‘INFO’ Designates informational messages that highlight the progress of the application at coarse-grained level.

  

Loggers:

io.dropwizard for INFO

com.wso2telco for DEBUG

Appenders:

Locations to where the logging messages should be displayed or written

- type:
Console / File

- Threshold: 
The lowest level of events to print to the console/ write to the file.

- timeZone: 
The time zone to which event timestamps will be converted.
 
- Target: 
The name of the standard stream to which events will be written.
Can be stdout or stderr. 

- currentLogFilename: 
The filename where current events are logged.

- archive: 
Whether or not to archive old events in separate files.
  
- archivedLogFilenamePattern: 
Required if archive is true.The filename pattern for archived files.%d is replaced with the date in yyyy-MM-dd format, and the fact that it ends with.gz indicates the file will be gzipped as it’s archived.Likewise, filename patterns which end in .zip will be filled as they are archived.

- archivedFileCount: 
The number of archived files to keep.
Must be between 1 and 50. 

- logFormat: 
Logback pattern with which events will be formatted.  


#### 2.4.4 Others:
  
- waitingTimeForToken: 
Maximum waiting time to obtain a token from the pool in milliseconds.
Default value is 200 ms 

- retryAttempt:   
Number of attempts for getting a valid token 

- isMaster: true #server mode (master/slave) 
This is a master instance which has both read & write operations with DB.
It can be changed false so that it will be slave instance with read only permission.

- tokenReadretrAttempts: 
Number of retry attempts for waiting to get valid token
Used only in slave mode 

- tokenReadretrAfter: 
Number of ms read db for valid token ,default is 1 min.
Used only in slave mode 

- refreshWakeUpLeadTime: 
The lead time to trigger Token refresh process  before its default validity period expires
Default value is 5000 ms 

## 3. Features 

### 3.1 APIs: 


- GET:		/tokenservice/{ownerID} 

API to retrieve a valid access token of a particular owner by passing the associated owner id
  

- PUT:		/tokenservice/restart/{ownerId} 

This will restart the pool for a particular owner
  

- PUT:		/tokenservice/refresh/{ownerId}/{tokenID} 

This will enable the regeneration process of access token using the existing refresh token for a given owner
  

- DELETE: 	/tokenservice/{ownerID}/{tokenID} 

Delete a particular access token of a given owner 
  
  

### 3.2 Swagger Annotations:  

[Swagger](http://swagger.io/getting-started/) is a standard, language-agnostic interface to REST APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection.

  
In order to retrieve Swagger definitions of this microservice, go to http://&lt;host&gt;:&lt;port&gt;/swagger?path=&lt;service_base_path&gt;.

For example [http://localhost:8181/swagger?path=tokenservice](http://localhost:8181/swagger?path=tokenservice)  in default configuration.
