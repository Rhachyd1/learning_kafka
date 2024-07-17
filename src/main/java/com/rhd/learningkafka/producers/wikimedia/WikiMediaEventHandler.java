package com.rhd.learningkafka.producers.wikimedia;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;

public class WikiMediaEventHandler implements EventHandler {

    private KafkaProducer<String,String> producer;
    String topic;
    private final Logger log = LoggerFactory.getLogger(WikiMediaEventHandler.class.getSimpleName());

    public WikiMediaEventHandler(KafkaProducer<String, String> producer, String topic){
        this.producer = producer;
        this.topic = topic;
    }

    @Override
    public void onClosed() throws Exception {
        this.producer.close();
    }

    @Override
    public void onComment(String arg0) throws Exception {
        
    }

    @Override
    public void onError(Throwable arg0) {
        log.error("error in stream");
    }

    @Override
    public void onMessage(String event, MessageEvent messageEvent) throws Exception {
        log.info(messageEvent.getData());
        log.info("Problema aqui");
        producer.send(new ProducerRecord<String,String>(topic,messageEvent.getData()));
    }

    @Override
    public void onOpen() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onOpen'");
    }
    
}
