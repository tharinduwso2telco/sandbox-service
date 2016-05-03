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

`http://localhost:8085/ussd/outbound/tel+94773330770`

```json
{
	"outboundUSSDMessageRequest": {
		"address": "tel:+94773330770",
		"shortCode": "tel:1721",
		"keyword": "3456",
		"outboundUSSDMessage": "12",
		"clientCorrelator": "123457",
		"responseRequest": {
			"notifyURL": "<$notify_url>",
			"callbackData": "some-data-useful-to-the-requester"
		},
		"ussdAction": "mtinit"
	}
}
```



