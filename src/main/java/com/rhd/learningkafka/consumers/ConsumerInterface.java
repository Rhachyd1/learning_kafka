package com.rhd.learningkafka.consumers;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.opensearch.client.RestHighLevelClient;

public interface ConsumerInterface {
   
   public void consume();
   public void consume( RestHighLevelClient client, KafkaConsumer<String, String> consumer);
}
