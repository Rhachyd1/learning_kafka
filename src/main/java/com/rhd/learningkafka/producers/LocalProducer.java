package com.rhd.learningkafka.producers;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalProducer implements Producer {
    private Properties properties;

   
    private String topic;
    private String url;
    private KafkaProducer<String, String> producer;
    private static final Logger log = LoggerFactory.getLogger(ProducerExamples.class.getSimpleName());
    private Callback callback;


    public LocalProducer(Properties properties){
        this.properties = properties;
        this.producer = new KafkaProducer<String, String>(properties);
        this.callback = new ProducerCallback(log);
    }

    @Override
    public KafkaProducer<String, String> getProducer(){
        return this.producer;
    }

    @Override
    public void produce() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'produce'");
    }

    public void sendWithoutCallback() {
        // ProducerRecord
        ProducerRecord<String, String> ProducerRecord = new ProducerRecord<>("kafka_funcionando", "Bom dia Meu Amigo Tenente!");

        this.producer.send(ProducerRecord);
        this.producer.flush();
        this.producer.close();
    }

    public void sendWithCallback() {
        // ProducerRecord
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("demo_java", "hello world");

        // properties.setProperty("batch.size", "400");
        // NEVER DO THIS IN PRODUCTION! -> properties.setProperty("partitioner.class", RoundRobinPartitioner.class.getName());

        this.producer.send(producerRecord, this.callback);
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
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);

                producer.send(producerRecord, this.callback);
            }
        }

        producer.flush();
        producer.close();
    }
}
