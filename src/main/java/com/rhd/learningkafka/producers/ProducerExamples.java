package com.rhd.learningkafka.producers;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerExamples {

    private Properties properties;
    private static final Logger log = LoggerFactory.getLogger(ProducerExamples.class.getSimpleName());

    public ProducerExamples(Properties properties) {
        this.properties = properties;
    }

    public void sendWithoutCallback() {
        // KafkaProducer
        KafkaProducer<String, String> producer = new KafkaProducer<>(this.properties);
        // ProducerRecord
        ProducerRecord<String, String> ProducerRecord = new ProducerRecord<>("kafka_funcionando",
                "Bom dia Meu Amigo Tenente!");

        producer.send(ProducerRecord);
        producer.flush();
        producer.close();
    }

    public void sendWithCallback() {
        // KafkaProducer
        KafkaProducer<String, String> producer = new KafkaProducer<>(this.properties);
        // ProducerRecord
        ProducerRecord<String, String> ProducerRecord = new ProducerRecord<>("demo_java", "hello world");

        // properties.setProperty("batch.size", "400");
        // NEVER DO THIS IN PRODUCTION! -> properties.setProperty("partitioner.class",
        // RoundRobinPartitioner.class.getName());

        producer.send(ProducerRecord, new Callback() {

            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    log.info("Received new metadata \n"
                            + "Topic: " + metadata.topic() + "\n"
                            + "Partition: " + metadata.partition() + "\n"
                            + "Offset: " + metadata.offset() + "\n"
                            + "Timestamp: " + metadata.timestamp() + "\n");
                } else {
                    log.error("Error: " + exception.getMessage());
                }

                // throw new UnsupportedOperationException("Unimplemented method
                // 'onCompletion'");
            }

        });
        producer.flush();
        producer.close();
    }

    public void sendWithCallbackAndKeys() {
        // KafkaProducer
        KafkaProducer<String, String> producer = new KafkaProducer<>(this.properties);

        for (int j = 0; j < 2; j++) {

            for (Integer id = 0; id < 10; id++) {

                String topic = "demo_java_with_keys";
                String key = "id_" + id;
                String value = "hello_world_" + id;

                // ProducerRecord
                ProducerRecord<String, String> ProducerRecord = new ProducerRecord<>(topic, key, value);

                producer.send(ProducerRecord, new Callback() {

                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (exception == null) {
                            log.info("Received new metadata \n"
                                    + "Key: " + key + "\n"
                                    + "Topic: " + metadata.topic() + "\n"
                                    + "Partition: " + metadata.partition() + "\n"
                                    + "Offset: " + metadata.offset() + "\n"
                                    + "Timestamp: " + metadata.timestamp() + "\n"
                                    + "------------------------------------------ \n");
                        } else {
                            log.error("Error: " + exception.getMessage());
                        }

                        // throw new UnsupportedOperationException("Unimplemented method
                        // 'onCompletion'");
                    }

                });
            }
        }

        producer.flush();
        producer.close();
    }

}
