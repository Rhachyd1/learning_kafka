package com.rhd.learningkafka.producers.wikimedia;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import com.rhd.learningkafka.producers.Producer;

public class WikiMediaChangesProducer implements Producer {

    private KafkaProducer<String, String> producer;
    private String topic;
    private final String url = "https://stream.wikimedia.org/v2/stream/recentchange";

    public WikiMediaChangesProducer(String topic, Properties properties) {
        this.producer = new KafkaProducer<String, String>(properties);
        this.topic = topic;
    }

    public void produce() throws InterruptedException {
        EventHandler eventHandler = new WikiMediaEventHandler(producer, topic);
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource = builder.build();
        eventSource.start();
        TimeUnit.MINUTES.sleep(10);
    }

    @Override
    public KafkaProducer<String, String> getProducer() {
        return this.producer;
    }
}
