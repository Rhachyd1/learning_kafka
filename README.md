# learning_kafka


Sample project for learning Kafka
    To run as a producer using maven:  mvn exec:java -Dexec.args="PRODUCER"
    To run as a Consumer using maven:  mvn exec:java -Dexec.args="CONSUMER"

    Remember: docker build --file filename --tag tagname .
    Remember: docker container run --name learning-kafka-producer -d --network=learning_kafka_default learning-kafka-producer:latest 
To do List:

    1 - Fix Project Setup - DONE
    2 - create a consumer - 
    3 - make a dockerfile for both consumer and producer - 
    4 - set the docker compose file to run everything - 
    5 - upgrade to run with quarkus (optional) -
    6 - Fix Deprecated methods - 
    7 - Refactor FirstConsumerClient