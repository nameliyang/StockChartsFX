package com.zoicapital.stockchartsfx.mongo;


import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.zoicapital.stockchartsfx.Stock;
import com.zoicapital.stockchartsfx.Stocks;
import org.bson.Document;
import java.util.Arrays;
import java.util.List;

public class MongoTest {

    public static void main(String[] args) {

        MongoClient mongoClient =  MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.hosts(Arrays.asList(new ServerAddress("localhost", 27017))))
                        .build());
        MongoDatabase test = mongoClient.getDatabase("test");
        MongoCollection<Document> col = test.getCollection("stocks");
        List<Stock> stocks = new Stocks().getStockList(e ->{
            if(e.getCode().startsWith("2")|| e.getCode().startsWith("1")||e.getCode().startsWith("5")){
                return false;
            }
            return true;
        }).fillBasicBatch().fillFollowNum().value();
        for (Stock stock:stocks){
            col.insertOne(Document.parse(JSONObject.toJSONString(stock)));
        }
    }
}
