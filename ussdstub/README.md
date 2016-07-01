# ussdstub is a support utility for testing USSD

### How to build

```bash
mvn clean install
cd target
```
Executable jar will be created in the target directory. Edit `netty-transports.yml` file for custom configurations.

```bash
java -jar ussdstub.jar
```

### Sample JSON request body

HTTP `POST`

`http://localhost:8085/ussd/outbound/tel+94123456789`

***NOTES***: 

1.You will have to set the following header values when making the request. 

`Content-Type: application/json`

2.`notifyURL` is a receiver URL on your application. Any user entered data will be sent to this URL. 


```json
{
	"outboundUSSDMessageRequest": {
		"address": "tel:+94123456789",
		"shortCode": "tel:1721",
		"keyword": "3456",
		"outboundUSSDMessage": "12",
		"clientCorrelator": "123457",
		"responseRequest": {
			"notifyURL": "http://my-receiver-url",
			"callbackData": "some-data-useful-to-the-requester"
		},
		"ussdAction": "mtinit"
	}
}
```



