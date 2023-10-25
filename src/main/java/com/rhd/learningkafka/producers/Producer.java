package com.rhd.learningkafka.producers;

import org.apache.kafka.clients.producer.KafkaProducer;

/**
     * acks=0 -> Will loose data; Use when is Ok to loose data;
     * acks=1 -> Kafka default
     * acks=all(-1) -> Kafka for 3.0 
     */
public interface Producer {
    public void produce() throws Exception;
    public KafkaProducer<String, String> getProducer();
}
