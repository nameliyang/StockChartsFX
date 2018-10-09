package com.zoicapital.stockchartsfx.active;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ActiveStockQuant {
    private static final Logger log = LoggerFactory.getLogger(ActiveStock.class);

    public static void main(String[] args) throws IOException {
        MongoClientURI mongoClientURI = new MongoClientURI(
                "mongodb+srv://liyang:liyang1234%25@cluster0-gla8u.mongodb.net/test?retryWrites=true");
        MongoClient mongoClient = new MongoClient(mongoClientURI);
        MongoDatabase test = mongoClient.getDatabase("test");
        MongoCollection<Document> document = test.getCollection("article");


        File file = new File(ActiveStockQuant.class.getClassLoader().getResource("stocks.txt").getPath());
        List<String> dates = FileUtils.readLines(file);


        dates.forEach((String e) ->{
            String date = e;
            FindIterable<Document> iterable = document.find(new Document("date", date)).sort(new Document("commentCount", -1)).limit(1);
            iterable.forEach((Block<Document>) doc -> {
                String code = doc.getString("code");
                String name = doc.getString("name");
                System.out.println(code+"\t"+name+"\t"+date);
            });
        });



    }

}
