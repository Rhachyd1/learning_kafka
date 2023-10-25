package com.rhd.learningkafka.properties;

import java.util.Properties;

import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class PropertiesFactory {

    private static Properties  properties = new Properties();

    public static Properties buildAsProducer() {
        
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    public static Properties buildAsConsumer() {
        
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());
        properties.setProperty("group.id", "my-java-application");
        properties.setProperty("auto.offset.reset", "earliest");
        // This is Eager, it will stop everything;
        // properties.setProperty("partition.assingment.strategy",
        // RoundRobinPartitioner.class.getName());

        return properties;
    }

    public Properties create() {
        return properties;
    }
}
