package com.rhd.learningkafka.producers;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;

public class ProducerCallback implements Callback {
    private static Logger log;


    public ProducerCallback(Logger logger){        
        log = logger;
    }

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

}
