package com.zoicapital.stockchartsfx.active;

import com.alibaba.fastjson.JSONObject;
import com.ly.quant.Article;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.zoicapital.stockchartsfx.Stock;
import com.zoicapital.stockchartsfx.stock.Stocks;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;

public class ArticleMongo {
    public static void main(String[] args) throws InterruptedException {
//        MongoClient mongoClient =  MongoClients.create(
//                MongoClientSettings.builder()
//                        .applyToClusterSettings(builder ->
//                                builder.hosts(Arrays.asList(new ServerAddress("localhost", 27017))))
//                        .build());
//        MongoDatabase test = mongoClient.getDatabase("test");

        MongoClientURI mongoClientURI = new MongoClientURI(
                "mongodb+srv://liyang:liyang1234%25@cluster0-gla8u.mongodb.net/test?retryWrites=true");
        MongoClient mongoClient = new MongoClient(mongoClientURI);
        MongoDatabase test = mongoClient.getDatabase("test");


        MongoCollection<Document> document = test.getCollection("article");

        List<Stock> stocks = new Stocks().getStockList(e ->{
            if(e.getCode().startsWith("2")|| e.getCode().startsWith("1")||e.getCode().startsWith("5")||e.getName().contains("ST")){
                return false;
            }
            return true;
        }).value();

        PageParse pageParse = new PageParse();

        //stocks = stocks.stream().filter(e->e.getCode().equals("600015")).collect(Collectors.toList());
        //600015
        for(int i = 0;i<stocks.size();i++){
            Stock stock = stocks.get(i);
            List<Article> articles = pageParse.parse(stock.getCode(), stock.getCode());
            List<Document> docs ;
            docs = articles.stream().map(a->Document.parse(JSONObject.toJSONString(a))).collect(Collectors.toList());
            document.insertMany(docs);
            System.out.println("---------------------------------"+(i*100 / stocks.size()));
        }

    }

}
