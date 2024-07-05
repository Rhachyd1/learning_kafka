package com.rhd.learningkafka;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.opensearch.action.admin.indices.create.CreateIndexRequest;
import org.opensearch.action.admin.indices.get.GetIndexRequest;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.xcontent.XContentType;
import org.opensearch.index.fielddata.ordinals.GlobalOrdinalsIndexFieldData.Consumer;

import com.rhd.learningkafka.consumers.FirstConsumerClient;
import com.rhd.learningkafka.consumers.wikimedia.WikimediaConsumer;
import com.rhd.learningkafka.producers.wikimedia.WikiMediaChangesProducer;
import com.rhd.learningkafka.properties.PropertiesFactory;

public class Main {

    private final static String CONSUMER = "CONSUMER";
    private final static String PRODUCER = "PRODUCER";
    private Properties properties = null;
    public static void main(String[] args) {
        Main main = null;
        if(args[0].toUpperCase().trim().equals(PRODUCER)){
            main = new Main();
            main.properties = PropertiesFactory.buildAsProducer();
            WikiMediaChangesProducer wikimedia = new WikiMediaChangesProducer("wikimedia.recentchange", main.properties);
            try {
                wikimedia.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if(args[0].toUpperCase().trim().equals(CONSUMER)){
            main = new Main();
            main.properties = PropertiesFactory.buildOpenSearchWikimediaConsumer();
            
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(main.properties);
            RestHighLevelClient client = WikimediaConsumer.createOpenSearchClient();
            CreateIndexRequest createIndexRequest = new CreateIndexRequest("wikimedia");
            

            try(client; consumer){
                Boolean exists = client.indices().exists(new GetIndexRequest().indices("wikimedia"), RequestOptions.DEFAULT);
                //.exists(new GetIndexRequest("wikimedia"), RequestOptions.DEFAULT );
                if(!exists){
                    client.indices().create(createIndexRequest, RequestOptions.DEFAULT);

                }else{

                }
                consumer.subscribe(Collections.singleton("wikimedia.recentchange"));
                while(true){
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(3000));
                    Integer recordCount = records.count();
                    for(ConsumerRecord<String, String> record: records){
                        IndexRequest IndexRequest = new IndexRequest("wikimedia").source(record.value(), XContentType.JSON);
                        IndexResponse response =  client.index(IndexRequest, RequestOptions.DEFAULT);
                        System.out.println(response.getId());
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
            }
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