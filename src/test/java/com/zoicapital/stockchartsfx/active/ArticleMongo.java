package com.zoicapital.stockchartsfx.active;

import com.alibaba.fastjson.JSONObject;
import com.ly.quant.Article;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.zoicapital.stockchartsfx.Stock;
import com.zoicapital.stockchartsfx.stock.Stocks;
import org.apache.commons.lang.time.DateUtils;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ArticleMongo {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(1);

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

        List<Stock> stocks = new Stocks().getStockList(e -> {
            if (e.getCode().startsWith("2") || e.getCode().startsWith("1") || e.getCode().startsWith("5") || e.getName().contains("ST")) {
                return false;
            }
            return true;
        }).value();

        stocks = stocks.stream().filter(e->e.getCode().equals("000970")).collect(Collectors.toList());
        PageParse pageParse = new PageParse();

        //stocks = stocks.s tream().filter(e->e.getCode().equals("600015")).collect(Collectors.toList());
        //600015
        for (int i = 0; i < 1000; i++) {
            final Stock stock = stocks.get(i);
            List<Article> articles = pageParse.parse(stock.getCode(), stock.getName());
            groupDateArticle(articles);
            List<Document> docs;
            docs = articles.stream().map(a -> Document.parse(JSONObject.toJSONString(a))).collect(Collectors.toList());

            document.insertMany(docs);
            System.out.println("==============================>" + (i * 100 / stocks.size()));
        }
    }

    private static void groupDateArticle(List<Article> articles) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Iterator<Article> iterator = articles.iterator();
        while(iterator.hasNext()){
            Article article = iterator.next();
            if(article.getCreateTime()==null){
                iterator.remove();
            }
            String createTime = article.getCreateTime();
            article.setCreateTime(sdf.format(new Date()));
            article.setDate(createTime.substring(0,10));
        }

        Map<String, List<Article>> groupMap = articles.stream().collect(Collectors.groupingBy(Article::getDate, TreeMap::new,Collectors.toList()));

        List<Article> rtnAticle = new ArrayList<>();
        Set<Map.Entry<String, List<Article>>> entries = groupMap.entrySet();
        for(Map.Entry<String,List<Article>> entry:entries){
            String key = entry.getKey();
            List<Article> value = entry.getValue();
            rtnAticle.add(collectList(key,value));
        }
    }

    private static Article collectList(String date,List<Article> value) {
        Integer readCount = new Integer(0);
        Integer commentCount = new Integer(0);
        for(int i = 0;i<value.size();i++){
            Article article = value.get(i);
            readCount+=article.getReadCount();
            commentCount+=article.getCommentCount();
        }
        Article article = new Article();
        article.setDate(date);
        article.setCommentCount(commentCount);
        article.setReadCount(readCount);
        return article;
    }

}
