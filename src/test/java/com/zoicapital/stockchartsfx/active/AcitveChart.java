package com.zoicapital.stockchartsfx.active;

import com.ly.quant.StockClient;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.*;
import com.zoicapital.stockchartsfx.DailyStock;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AcitveChart extends Application{
    private static final MongoClient mongoClient =  MongoClients.create(
            MongoClientSettings.builder()
                    .applyToClusterSettings(builder ->
                            builder.hosts(Arrays.asList(new ServerAddress("localhost", 27017))))
                    .build());
    private static final MongoDatabase test = mongoClient.getDatabase("test");

    public static void main(String[] args) throws IOException {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Line Chart Sample");
        final CategoryAxis xAxis = new CategoryAxis();

        final NumberAxis yAxis = new NumberAxis();

        final LineChart<String,Number> lineChart =
                new LineChart<>(xAxis,yAxis);
        XYChart.Series series0 = new XYChart.Series();
        add(series0);

        XYChart.Series series1 = new XYChart.Series();
        add1(series1);
        lineChart.getData().addAll(series0,series1);

        stage.setScene(new Scene(lineChart));
        stage.show();
    }

    public static  void add(XYChart.Series series0 ) throws IOException {
        List<DailyStock> dailyStocks = StockClient.getDailyStock("000970", "2017-10-30", "2018-09-24");
        dailyStocks.sort((e1,e2)->e1.getDate().compareTo(e2.getDate()));

        List<XYChart.Data> collect = dailyStocks.stream().map(e -> new XYChart.Data(e.getDate(), Double.parseDouble(e.getTCLOSE()))).collect(Collectors.toList());
        series0.getData().setAll(collect);
    }
    public static  void add1(XYChart.Series series0 ) throws IOException {
        List<DailyStock> dailyStocks = StockClient.getDailyStock("000970", "2017-08-01", "2018-09-01");
        dailyStocks.sort((e1,e2)->e1.getDate().compareTo(e2.getDate()));
        count(dailyStocks);
        List<XYChart.Data> collect = dailyStocks.stream().map(e -> new XYChart.Data(e.getDate(),e.getArticleCount())).collect(Collectors.toList());
        series0.getData().setAll(collect);
    }


    public  static void count(List<DailyStock> dailyStocks){
        for(DailyStock dailyStock:dailyStocks){
            MongoCollection<Document> document = test.getCollection("article");
            BasicDBObject queryObject = new BasicDBObject("createTime",new BasicDBObject("$gt",dailyStock.getDate()+" 00:00:00").append("$lt",dailyStock.getDate()+" 23:59:59"));
            Long count= document.countDocuments(queryObject);
            dailyStock.setArticleCount(count);
        }
    }


}
