package com.rhd.learningkafka;

import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.opensearch.action.admin.indices.create.CreateIndexRequest;
import com.rhd.learningkafka.client.LocalRestClient;
import com.rhd.learningkafka.consumers.ConsumerInterface;
import com.rhd.learningkafka.consumers.wikimedia.WikimediaConsumer;
import com.rhd.learningkafka.producers.wikimedia.WikiMediaChangesProducer;
import com.rhd.learningkafka.properties.PropertiesFactory;

public class Main {

    private final static String CONSUMER = "CONSUMER";
    private final static String PRODUCER = "PRODUCER";
    private static final String CONN_STRING = "http://localhost:9200";

    private final static Boolean MANUAL = true;
    private Properties properties = null;
    

    
    public static void main(String[] args) {
        Main main = new Main();
        if(args[0].toUpperCase().trim().equals(PRODUCER)){            
            main.properties = PropertiesFactory.buildAsProducer();
            WikiMediaChangesProducer wikimedia = new WikiMediaChangesProducer("wikimedia.recentchange", main.properties);
            try {
                wikimedia.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if(args[0].toUpperCase().trim().equals(CONSUMER)){
            
            /*
                Auto commit:
                    main.properties = PropertiesFactory.buildOpenSearchWikimediaConsumer();
         
                Manual commit
                    main.properties = PropertiesFactory.buildOpenSearchWikimediaConsumer(MANUAL);
            */
            main.properties = PropertiesFactory.buildOpenSearchWikimediaConsumer(MANUAL);
           
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(main.properties);
            CreateIndexRequest createIndexRequest = new CreateIndexRequest("wikimedia");
            WikimediaConsumer wikimediaConsumer = new WikimediaConsumer(createIndexRequest);
            wikimediaConsumer.consume(new LocalRestClient(CONN_STRING).createOpenSearchClient(), consumer);
            
            
        }   
    /* 
        Properties propertiesProducer = PropertiesFactory.buildAsProducer();

        LocalProducer producerClient = new LocalProducer(propertiesProducer);
        producerClient.sendWithCallbackAndKeys();
        Properties propertiesConsumer =  PropertiesFactory.buildAsConsumer();
        propertiesConsumer.setProperty("partition.assingment.strategy", CooperativeStickyAssignor.class.getName());

        FirstConsumerClient consumerClient = new FirstConsumerClient(propertiesConsumer);
        consumerClient.consume();
    */    
    }

   
}