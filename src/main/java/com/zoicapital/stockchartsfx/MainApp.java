package com.zoicapital.stockchartsfx;
/*
Copyright 2014 Zoi Capital, LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */


import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MainApp extends Application implements EventHandler<MouseEvent> {


    private static final Integer day = 60;

    CandleStickChart candleStickChart;

    TextField topen = null;


    TextField tclose = null;

    TextField thigh = null;

    TextField tlow = null;

    List<BarData> barData = buildBars();

    ObservableList<XYChart.Series<String, Number>> dataSeries;

    @Override
    public void start(Stage stage) throws Exception {
        candleStickChart = new CandleStickChart("Stock", buildBars());
        BorderPane borderPane = new BorderPane ();
        ListView listView = new ListView();
        List<Map<String, String>> stockCodes = StockSpider.getStockCodes(Stream.of('2', '5').collect(Collectors.toList()));
        stockCodes = stockCodes.stream().filter(map -> !map.get("name").contains("ST")).collect(Collectors.toList());

        List<String> lists = stockCodes.stream().map(e -> {
             return  e.get("code") + "(" + e.get("name") + ")";
        }).collect(Collectors.toList());

        System.out.println(stockCodes.size());

        final List<Map<String, String>> stCollect = stockCodes.stream().filter(map -> !map.get("name").contains("ST")).collect(Collectors.toList());

        ObservableList<String> strings = FXCollections.observableArrayList(lists);

        listView.setItems(strings);

        listView.setOnMouseClicked( e ->{
            ObservableList selectedItems = listView.getSelectionModel().getSelectedItems();
            String str = (String) selectedItems.get(0);
            String code = str.substring(0,str.indexOf('('));
            try {
                List<DailyStock> dailyStocks = StockHistory.getDailyStocks(code, "20180606", "20180808");
                candleStickChart.update(dailyStocks);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });


        borderPane.setLeft(listView);
        borderPane.setCenter(candleStickChart);
        borderPane.setRight(createGridPane());
        Scene scene = new Scene(borderPane,1500,800);
        scene.getStylesheets().add("/styles/CandleStickChartStyles.css");

        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();

        candleStickChart.setYAxisFormatter(new DecimalAxisFormatter("#0.00"));
        candleStickChart.setMinWidth(900);
    }

    @Override
    public void handle(MouseEvent event) {
        Double  open = Double.parseDouble(topen.getText());
        Double  close  = Double.parseDouble(tclose.getText());
        Double  high = Double.parseDouble(thigh.getText());
        Double  low = Double.parseDouble(tlow.getText());
        BarData barData = this.barData.get(this.barData.size() - 1);
        GregorianCalendar dateTime = (GregorianCalendar) barData.getDateTime().clone();
        dateTime.add(Calendar.MINUTE, 5);
        BarData bar = new BarData(dateTime, open, high, low, close, 1);
        candleStickChart.addBar(bar);
    }

    private GridPane createGridPane() {
        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(3,3,3,100));
        gridpane.setHgap(5);
        gridpane.setVgap(5);
        ColumnConstraints column1 = new ColumnConstraints(50);
        ColumnConstraints column2 = new ColumnConstraints(50, 60, 100);

        column2.setHgrow(Priority.ALWAYS);
        gridpane.getColumnConstraints().addAll(column1, column2);

        Label lopen = new Label("开盘价");
        topen = new TextField("0.0");


        Label lclose = new Label("收盘价");
        tclose = new TextField("0.0");

        Label lhigh = new Label("最高价");
        thigh =new TextField("0.0");

        Label llow = new Label("最低价");
        tlow = new TextField("0.0");
        Button saveButton = new Button("Save");
        saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this);

        GridPane.setHalignment(lopen, HPos.LEFT);
        GridPane.setHalignment(topen, HPos.RIGHT);

        GridPane.setHalignment(saveButton, HPos.RIGHT);

        gridpane.add(lopen, 0, 0);
        gridpane.add(topen, 1, 0);

        gridpane.add(lclose, 0, 1);
        gridpane.add(tclose, 1, 1);

        gridpane.add(lhigh, 0, 2);
        gridpane.add(thigh, 1, 2);

        gridpane.add(llow, 0, 3);
        gridpane.add(tlow, 1, 3);

        gridpane.add(saveButton, 1, 4);

        return gridpane;

    }

    public static Date getDateBefore(Date d,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)-day);
        return now.getTime();
    }

    public List<BarData> buildBars() {
        Date nowDate = new Date();
        Date startDate = getDateBefore(nowDate, day);
        GregorianCalendar now = new GregorianCalendar(startDate.getYear(),startDate.getMonth(),startDate.getDay());
        final List<BarData> bars = new ArrayList<>();

        for (int i = 0; i < day; i++) {

            BarData bar = new BarData((GregorianCalendar) now.clone(), 0, 0, 0, 0, 1);
            now.add(Calendar.DATE, 1);
            bars.add(bar);
        }
        return bars;
    }


    protected double getNewValue(double previousValue) {
        int sign;

        if (Math.random() < 0.5) {
            sign = -1;
        } else {
            sign = 1;
        }
        return getRandom() * sign + previousValue;
    }

    protected double getRandom() {
        double newValue = 0;
        newValue = Math.random() * 10;
        return newValue;
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
