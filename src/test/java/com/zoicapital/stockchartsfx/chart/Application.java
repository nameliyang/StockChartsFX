package com.zoicapital.stockchartsfx.chart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();


        ScatterChart scaterChart =  new ScatterChart(xAxis,yAxis);
        scaterChart.setData(getChartData());
        scaterChart.setAlternativeColumnFillVisible(false);
        scaterChart.setAlternativeRowFillVisible(true);
        stage.setTitle("Chart Test");
        StackPane root= new StackPane();
        root.getChildren().add(scaterChart);

        stage.setScene(new Scene(root,400,250));
        stage.show();

    }



    public  ObservableList<XYChart.Series<String,Double>> getChartData() {
        double javaValue = 17.56;
        double cvalue = 17.06;
        double cppvalue = 8.25;
        ObservableList<XYChart.Series<String,Double>> answer = FXCollections.observableArrayList();
        XYChart.Series<String,Double> java = new XYChart.Series<>();
        XYChart.Series<String,Double> c = new XYChart.Series<>();
        XYChart.Series<String,Double> cpp =  new XYChart.Series<>();
        for(int i = 2011;i<2021;i++){
            java.getData().add(new XYChart.Data<>(String.valueOf(i),javaValue));
            javaValue = javaValue+4*Math.random() -2;
            c.getData().add(new XYChart.Data<>(String.valueOf(i),cvalue));
            cvalue = cvalue+ Math.random()-.5;
            cpp.getData().add(new XYChart.Data<>(String.valueOf(i),cppvalue));
            cppvalue = cppvalue+4*Math.random() -2;
        }
        answer.addAll(java,c,cpp);
        return answer;
    }
}
