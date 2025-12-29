# kafka_montool
Kafka Monitoring Tool Sanbox to deployment and test anything you want


``Consumer start to test lag``
docker exec -e KAFKA_JMX_OPTS="" -it kafka1 bash

kafka-console-consumer.sh   --bootstrap-server kafka1:9092   --topic test-topic   --group test-group   --from-beginning

curl localhost:8080/lag/test-consumer