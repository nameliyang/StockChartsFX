package com.zoicapital.stockchartsfx.active;

import com.alibaba.fastjson.JSONObject;
import com.ly.quant.Article;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.zoicapital.stockchartsfx.Stock;
import com.zoicapital.stockchartsfx.Stocks;
import com.zoicapital.stockchartsfx.active.PageParse;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class ArticleMongo {
    public static void main(String[] args) throws InterruptedException {
        MongoClient mongoClient =  MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.hosts(Arrays.asList(new ServerAddress("localhost", 27017))))
                        .build());
        MongoDatabase test = mongoClient.getDatabase("test");

        MongoCollection<Document> document = test.getCollection("article");

        List<Stock> stocks = new Stocks().getStockList(e ->{
            if(e.getCode().startsWith("2")|| e.getCode().startsWith("1")||e.getCode().startsWith("5")){
                return false;
            }
            return true;
        }).value();
        //600015
        for(Stock stock:stocks){
            PageParse pageParse = new PageParse();
            CountDownLatch cdl = new CountDownLatch(100);
            for(int i = 1;i<=100;i++){
                final int j = i;
                try{
                    new Thread(){
                        @Override
                        public void run() {
                           try{
                               List<Article> articles = pageParse.parse(stock.getCode(), stock.getCode(), j);
                               List<Document> docs = new ArrayList<>();
                               docs = articles.stream().map(a->Document.parse(JSONObject.toJSONString(a))).collect(Collectors.toList());
                               document.insertMany(docs);
                               try {
                                   Thread.sleep(10);
                               } catch (InterruptedException e) {
                                   e.printStackTrace();
                               }
                           } catch (IOException e) {
                               e.printStackTrace();
                           } finally {
                               cdl.countDown();
                           }
                        }
                    }.start();

                }catch (Exception e){
                    e.printStackTrace();
                    break;
                }
            }
            cdl.await();
        }


    }

}
