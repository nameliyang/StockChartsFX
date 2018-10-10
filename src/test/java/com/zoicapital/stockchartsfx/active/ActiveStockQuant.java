package com.zoicapital.stockchartsfx.active;

import com.ly.quant.StockHistory;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.zoicapital.stockchartsfx.DailyStock;
import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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


        dates.forEach((String e) -> {
            String date = e;
            FindIterable<Document> iterable = document.find(new Document("date", date)).sort(new Document("commentCount", -1)).limit(1);
            iterable.forEach((Block<Document>) doc -> {
                String code = doc.getString("code");
                String name = doc.getString("name").length()==3 ?doc.getString("name")+"　":doc.getString("name");
                name = doc.getString("name").length()==2 ?doc.getString("name")+"　　":name;
                System.out.print(code + "\t" + name + "\t" + date);
                try {
                    int index = dates.indexOf(date);
                    if (index < dates.size() - 1) {
                        if (index + 3 <= (dates.size() - 1)) {
                            String start = dates.get(index );
                            String end = dates.get(index + 3);
                            List<DailyStock> dailyStocks = StockHistory.getDailyStocks(code, start.replaceAll("-",""), end.replaceAll("-",""));
                            if(dailyStocks.size()==4){
                                StringBuilder sb = new StringBuilder();
                                final String close = dailyStocks.get(0).getTCLOSE();
                                if(Double.parseDouble(close)==0.0){
                                    System.out.println();
                                    return;
                                }
                                dailyStocks =  dailyStocks.subList(1,dailyStocks.size());
                                sb.append("\t").append(String.format("%4s",close));
                                List<String> allData = new ArrayList<>();
                                dailyStocks.forEach(d ->{
                                    allData.add(String.format("%4s",d.getTCLOSE()));
                                    sb.append("\t").append(d.getDate()).append("(").append(String.format("%4s",d.getTOPEN())).append(",").append(String.format("%4s",d.getTCLOSE())).append(")");
                                });
                                Boolean buySuccess = Boolean.FALSE;
                                for(String price:allData){
                                    if(Double.parseDouble(price) > Double.parseDouble(close)){
                                        buySuccess = Boolean.TRUE;
                                        break;
                                    }
                                }
                                sb.append("\t").append(buySuccess);
                                System.out.print(sb);
                            }

                        }

                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.out.println();
            });
        });
    }

    public String format(String price){

        return null;
    }

}
