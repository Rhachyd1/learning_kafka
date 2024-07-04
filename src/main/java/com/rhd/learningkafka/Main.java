package com.rhd.learningkafka;

import java.util.Properties;
import com.rhd.learningkafka.producers.wikimedia.WikiMediaChangesProducer;
import com.rhd.learningkafka.properties.PropertiesFactory;

public class Main {

    private final static String CONSUMER = "CONSUMER";
    private final static String PRODUCER = "PRODUCER";
    private Properties properties = null;
    public static void main(String[] args) {
        Main main = null;
        if(args[0].equals(PRODUCER)){
            main = new Main();
            main.properties = PropertiesFactory.buildAsProducer();
            WikiMediaChangesProducer wikimedia = new WikiMediaChangesProducer("wikimedia.recentchange", main.properties);
            try {
                wikimedia.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if(args[0].equals(CONSUMER)){
            main = new Main();
            main.properties = PropertiesFactory.buildAsConsumer();
        }   

        // Properties propertiesProducer = PropertiesFactory.buildAsProducer();

        // LocalProducer producerClient = new LocalProducer(propertiesProducer);
        // producerClient.sendWithCallbackAndKeys();

        // Properties propertiesConsumer =  PropertiesFactory.buildAsConsumer();
        // propertiesConsumer.setProperty("partition.assingment.strategy", CooperativeStickyAssignor.class.getName());

        // FirstConsumerClient consumerClient = new FirstConsumerClient(propertiesConsumer);
        // consumerClient.consumerClient();
        
    }

}