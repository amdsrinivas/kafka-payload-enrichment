# Kafka Payload Enrichment Sample app

## Steps to Setup Kafka
- Download the binary from : https://kafka.apache.org/downloads
- Use the following commands to start zookeeper and kafka servers:
```bash
${kafka_directory}/bin/zookeeper-server-start.sh config/zookeeper.properties
${kafka_directory}/bin/kafka-server-start.sh config/server.properties
```
> When running kafka on wsl2 on windows 10, in order to connect from outside the wsl environment, make the following changes to ${kafka_directory}config/server.properties
```properties
listeners=PLAINTEXT://[::1]:9092
```
This will expose Kafka on ipv6 loop back port, letting us connect from other environments of the same device.

- Once the kafka is running, Use the following commands to create input and output topics:
```bash
${kafka_directory}/bin/kaftopics.sh --create --topic input_topic --bootstrap-server [::1]:9092
${kafka_directory}/bin/kaftopics.sh --create --topic output_topic --bootstrap-server [::1]:9092
```

- use the following commands to open console to the input and output topics:
```bash
# Use this shell to send messages to the application.
${kafka_directory}/bin/kafka-console-producer.sh --bootstrap-server [::1]:9092 --topic input_topic
# Use this shell to check the messages produced by the application.
${kafka_directory}/bin/kafka-console-consumer.sh --bootstrap-server [::1]:9092 --topic output_topic
```

- The application uses file based H2 DB which will be created under root of the project. Username and password can be configured from application.properties under "resources" directory.
	- Console will be available under : http://localhost:8080/h2-console
- The REST API is mocked using http://wiremock.org/. API is available at : http://amdsrinivas.mocklab.io/population/<city_name>

### A sample flow of the application
```
Send message like : {"city" : "berlin"} to the input topic.
Application queries Country from H2 DB and population from REST API.
It publishes message like {"city":"berlin","country":"GERMANY","population":3645000} to the output topic.
```

