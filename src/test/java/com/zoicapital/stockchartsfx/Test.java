package com.zoicapital.stockchartsfx;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import sun.misc.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

public class Test {


//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        CategoryAxis axis = new CategoryAxis();
//        NumberAxis yxis = new NumberAxis();
//        LineChart<String,Number> lineChart = new LineChart<>(axis,yxis);
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        series.getData().add(new XYChart.Data<>("20180813",1.5));
//        series.getData().add(new XYChart.Data<>("20180814",1.1));
//        series.getData().add(new XYChart.Data<>("20180816",-2));
//        series.getData().add(new XYChart.Data<>("20180816",-1.5));
//        series.getData().add(new XYChart.Data<>("20180817",0));
//        series.getData().add(new XYChart.Data<>("20180818",1.5));
//        lineChart.getData().add(series);
//        Scene scene = new Scene(lineChart);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//
//    }
    public static void main(String[] args) throws IOException {
        List<String> list = FileUtils.readLines(new File("stockactive.txt"), Charset.forName("utf-8"));
        System.out.println(list);

    }
}


