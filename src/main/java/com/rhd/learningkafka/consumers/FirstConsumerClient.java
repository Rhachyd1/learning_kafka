package com.rhd.learningkafka.consumers;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.opensearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstConsumerClient implements ConsumerInterface {

    private Properties properties;
    private static final Logger log = LoggerFactory.getLogger(FirstConsumerClient.class.getSimpleName());
    private KafkaConsumer<String, String> consumer;

    public FirstConsumerClient(Properties properties) {
        this.properties = properties;
        this.consumer = new KafkaConsumer<>(this.properties);
    }

    public void setupRuntime() {
        final Thread consumerThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.info("This is a Shutdown");
                consumer.wakeup();
                try {
                    consumerThread.join();
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
        });
    }

    @Override
    public void consume() {
        while (true) {
            // poll for data
            log.info("polling");
            ConsumerRecords<String, String> records = this.consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                log.info("Key: " + record.key() + " | " + "Value: " + record.value());
                log.info("Partition: " + record.partition() + " | " + "Offset: " + record.offset());
            }
        }
    }

    public ConsumerInterface s1etupClient() {
        // subscribe for topic
        this.consumer.subscribe(Arrays.asList("demo_java_with_keys"));
        this.setupRuntime();
        try {
            this.consume();
        } catch (WakeupException e) {
            log.info("Expected. Shutting down");
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            this.consumer.close();
            log.info("Consumer shutdown");
        }
        return this;
    }

    @Override
    public void consume(RestHighLevelClient client, KafkaConsumer<String, String> consumer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'consume'");
    }

    

    
}
