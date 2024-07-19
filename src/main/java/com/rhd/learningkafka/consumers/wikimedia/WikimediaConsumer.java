package com.rhd.learningkafka.consumers.wikimedia;

import com.google.gson.JsonParser;
import com.rhd.learningkafka.consumers.ConsumerInterface;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.opensearch.action.admin.indices.create.CreateIndexRequest;
import org.opensearch.action.admin.indices.get.GetIndexRequest;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.bulk.BulkResponse;
import org.opensearch.action.index.IndexRequest;

import org.opensearch.client.RequestOptions;

import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.time.Duration;
import java.util.Collections;

@SuppressWarnings("deprecation")
public class WikimediaConsumer implements ConsumerInterface {

    
    private final static Integer MIN_REQUEST = 0;    
    KafkaConsumer<String, String> consumer;
    CreateIndexRequest indexRequest;
    private static final Logger LOG = LoggerFactory.getLogger(WikimediaConsumer.class.getSimpleName());


    public WikimediaConsumer(CreateIndexRequest indexRequest){
        this.indexRequest = indexRequest;
    }

    @Override
    public void consume(RestHighLevelClient client, KafkaConsumer<String, String> consumer) {
        
        try(client; consumer){
            
                Boolean exists = client.indices()
                    .exists(new GetIndexRequest()
                    .indices("wikimedia"), RequestOptions.DEFAULT);
                
                if(!exists){
                    client.indices().create(this.indexRequest, RequestOptions.DEFAULT);
                }

                consumer.subscribe(Collections.singleton("wikimedia.recentchange"));
                
                while(true){
            
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(3000));
                    Integer recordCount = records.count();
                    LOG.info("Records: "+recordCount);
                    /*Using Bulk Request */
                    BulkRequest bulkRequest = new BulkRequest();

                    /*Strategies for indepotence
                     * 
                     * 1 - id = topic_partition_offset
                     *      Ex: String id = record.topic()+"_"+record.partition()+"_"+record.offset();
                     * 2 - Use the ID of the data itself, in case it have one;
                     *      ex: extract the id from the data itself, either mapping or using the Gson dependency; see WikimediaConsumer class for an example
                     */
                    for(ConsumerRecord<String, String> record: records){
                        /*Without id / indepotence
                        IndexRequest IndexRequest = new IndexRequest("wikimedia").source(record.value(), XContentType.JSON);

                        With ID / Indepotence*/
                        try{
                            IndexRequest indexRequest = new IndexRequest("wikimedia")
                                                            .source(record.value(), XContentType.JSON)
                                                            .id(this.getIdFromData(record.value()));
                            
                                                            //IndexResponse response =  client.index(indexRequest, RequestOptions.DEFAULT);

                            bulkRequest.add(indexRequest);

                           // LOG.info(response.getId()); LOG THIS!
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    
                    if(bulkRequest.numberOfActions() > MIN_REQUEST){
                        BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                        LOG.info("Commited: " + response.getItems().length +" Items");                        
                        Thread.sleep(1000);                        
                    }
                    consumer.commitSync();
                    
                }
            }catch(InterruptedException | IOException e){
                e.printStackTrace();
            }
    }

    @Override
    public void consume() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'consume'");
    }

    public String getIdFromData(String json){
        return JsonParser.parseString(json)
                .getAsJsonObject().get("meta")
                .getAsJsonObject().get("id")
                .getAsString();
    }

}
