package com.zoicapital.stockchartsfx.active;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.zoicapital.stockchartsfx.Stock;
import com.zoicapital.stockchartsfx.stock.Stocks;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class CheckTest {
    static final MongoClientURI mongoClientURI = new MongoClientURI(
            "mongodb+srv://liyang:liyang1234%25@cluster0-gla8u.mongodb.net/test?retryWrites=true");
    static final MongoClient mongoClient = new MongoClient(mongoClientURI);
    static final MongoDatabase test = mongoClient.getDatabase("test");

    public static void download() throws IOException {
        org.jsoup.nodes.Document document = Jsoup.connect("http://stockpage.10jqka.com.cn/600570/#dzjy").get();
        Elements trading_w_syl = document.getElementsByClass("new_trading fl");
        System.out.println(trading_w_syl.get(0));
    }


    public static void main(String[] args) throws IOException {
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load("https://baidu.com");

//        List<Stock> stocks = new Stocks().getStockList(e -> {
//            if (e.getCode().startsWith("2") || e.getCode().startsWith("1") || e.getCode().startsWith("5") || e.getCode().startsWith("9") || e.getName().contains("ST")) {
//                return false;
//            }
//            return true;
//        }).value();
//
//        for(int i = 0;i<stocks.size();i++){
//            if(stocks.get(i).getCode().equals("600511")){
//                System.out.println("index ="+i+" count:"+stocks.size());
//            }
//        }

        //preCode:600511国药股份 600512 腾达建设
//        MongoCollection<Document> article = test.getCollection("article");
//
//        String codeName = stocks.get(0).getCode()+stocks.get(0).getName();
//        for(Stock stock:stocks){
//            Document query = new Document("code",stock.getCode());
//            FindIterable<Document> documents = article.find(query);
//            MongoCursor<Document> iterator = documents.iterator();
//            if(iterator.hasNext()){
//                codeName = stock.getCode()+stock.getName();
//                continue;
//            }else{
//                System.out.println("preCode:"+codeName+" "+stock.getCode()+" "+ stock.getName());
//                break;
//            }
//        }





    }
}
