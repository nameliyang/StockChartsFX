package com.zoicapital.stockchartsfx.active;

import com.alibaba.fastjson.JSONObject;
import com.ly.quant.StockClient;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.zoicapital.stockchartsfx.DailyStock;
import com.zoicapital.stockchartsfx.Stock;
import com.zoicapital.stockchartsfx.Stocks;
import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ActiveStock{

    public static void main(String[] args) throws IOException {
        List<Stock> stocks = new Stocks().getStockList(e ->{
            if(e.getCode().startsWith("2")|| e.getCode().startsWith("1")||e.getCode().startsWith("5")){
                return false;
            }
            return true;
        }).value();
        MongoCollection<Document> docs = test.getCollection("stocks");
        for(int i = 0;i< stocks.size() ;i++){
            if(stocks.get(i).getCode().equals("600016")){
                break;
            }
            List<DailyStock> dailyStocks = builder(stocks.get(i).getCode(),stocks.get(i).getName());
            System.out.println("insert code: "+stocks.get(i).getCode());
            List<Document> collect = dailyStocks.stream().map(a -> Document.parse(JSONObject.toJSONString(a))).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(collect)){
                docs.insertMany(collect);
            }

        }
    }


    private static final MongoClient mongoClient =  MongoClients.create(
            MongoClientSettings.builder()
                    .applyToClusterSettings(builder ->
                            builder.hosts(Arrays.asList(new ServerAddress("localhost", 27017))))
                    .build());
    private static final MongoDatabase test = mongoClient.getDatabase("test");

    public static  List<DailyStock> builder(String code, String name) throws IOException {
        List<DailyStock> dailyStocks = StockClient.getDailyStock(code, "2017-08-01", "2018-09-24");
        dailyStocks.sort((e1,e2)->e1.getDate().compareTo(e2.getDate()));
        count(dailyStocks,code);
        dailyStocks.forEach(e->e.setName(name));
       return dailyStocks;
    }


    public  static void count(List<DailyStock> dailyStocks, String code){
        for(DailyStock dailyStock:dailyStocks){
            MongoCollection<Document> document = test.getCollection("article");
            BasicDBObject queryObject = new BasicDBObject("createTime",new BasicDBObject("$gt",dailyStock.getDate()+" 00:00:00").append("$lt",dailyStock.getDate()+" 23:59:59"));
            queryObject.append("code",code);
            Long count= document.countDocuments(queryObject);
            dailyStock.setArticleCount(count);
        }
    }
}
