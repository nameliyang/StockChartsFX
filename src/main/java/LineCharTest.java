import com.zoicapital.stockchartsfx.DailyStock;
import com.ly.quant.StockHistory;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LineCharTest extends Application{


    List<DailyStock> dailyStocks  = null;
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date nowDate = null;
        try {
            nowDate = sdf.parse("2018086");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date before5Date = stepMonth(nowDate,-9);

        try {
            System.out.println("start = "+sdf.format(before5Date));
            System.out.println( "end = "+sdf.format(nowDate));

         //   dailyStocks = Compass.getDateStocks("600113", nowDate, before5Date, sdf);
            dailyStocks=    StockHistory.getDailyStockByTushare("600113","2018-01-01","2018-09-26");
            DailyStock preStock = dailyStocks.get(0);
            for(int i = 1;i<dailyStocks.size();i++){
                DailyStock dailyStock = dailyStocks.get(i);
                Double ema12;
                Double ema26;
                Double dea ;
                Double diff ;
                if(i ==1 ){
                    ema12  =  preStock.getClose()*11/13 + dailyStock.getClose()*2/13;
                    ema26  =  preStock.getClose()*25/27 + dailyStock.getClose()*2/27;
                    diff =   ema12 - ema26;
                    dea   =  0 + diff*  2/10;
                }else{
                    ema12 =   preStock.getEma12()*11/13+ dailyStock.getClose()*2/13;
                    ema26  =  preStock.getEma26()*25/27 + dailyStock.getClose()*2/27;
                    diff = ema12 - ema26;
                    dea   =  preStock.getDea() *8/10 +diff*2/10;
                }
                dailyStock.setDea(dea);
                dailyStock.setDiff(diff);
                dailyStock.setEma12(ema12);
                dailyStock.setEma26(ema26);
                dailyStock.setMacd(diff -dea );
                preStock = dailyStock;
            }

            dailyStocks = dailyStocks.subList(50,dailyStocks.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public   Date stepMonth(Date sourceDate, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(sourceDate);
        c.add(Calendar.MONTH, month);

        return c.getTime();
    }
    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        CategoryAxis categoryAxis = new CategoryAxis();
        NumberAxis numberAxis = new NumberAxis();
        numberAxis.setAutoRanging(true);
        XYChart.Series<String,Number> closeSeries = new XYChart.Series<>();
        buildData(closeSeries);
        XYChart.Series<String,Number> openSeries = new XYChart.Series<>();
//        buildOpenData(openSeries);
        XYChart.Series<String,Number> macdSeries = new XYChart.Series<>();
        buildMacdData(macdSeries);
        ObservableList<XYChart.Series<String,Number>> data  = FXCollections.observableArrayList(closeSeries,macdSeries);

        LineChart<String,Number> lineChart = new LineChart<String,Number> (categoryAxis,numberAxis);
        lineChart.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            }
        });


        lineChart.setData(data);
        lineChart.setCreateSymbols(false);
        Scene scene = new Scene(lineChart,1500,800);
        scene.getStylesheets().add("Chart.css");
        stage.setScene(scene);
        stage.show();

    }

    private void buildMacdData(XYChart.Series<String, Number> data) {
        for(DailyStock dailyStock:dailyStocks){
            Double value  =   0.0;
            if(dailyStock.getDiff()!=null){
                value = dailyStock.getMacd();
            }
            data.getData().add(new XYChart.Data<>(dailyStock.getDate(),value*5));
        }
    }

    private void buildOpenData(XYChart.Series<String, Number> data) {
        for(DailyStock dailyStock:dailyStocks){
            data.getData().add(new XYChart.Data<>(dailyStock.getDate(),Double.parseDouble(dailyStock.getTOPEN())));
        }
    }

    private void buildData(XYChart.Series<String, Number> data) throws IOException {
        for(DailyStock dailyStock:dailyStocks){
            data.getData().add(new XYChart.Data<>(dailyStock.getDate(),Double.parseDouble(dailyStock.getTCLOSE())));
        }
    }

    public static  String code2Symbol(String code){
        List<String> shList = Stream.of("5", "6", "9").collect(Collectors.toList());
        List<String> shList_1 = Stream.of("11", "13").collect(Collectors.toList());
        String symbol = null;
        if(shList.contains(code.substring(0,1)) || shList_1.contains(code)){
            symbol = "sh"+code;
        }else{
            symbol = "sz"+code;
        }
        return symbol;
    }
}


class LineChartWithMarkers<X,Y> extends LineChart {

    private ObservableList<Data<X, Y>> horizontalMarkers;
    private ObservableList<Data<X, Y>> verticalMarkers;

    public LineChartWithMarkers(Axis<X> xAxis, Axis<Y> yAxis) {
        super(xAxis, yAxis);
        horizontalMarkers = FXCollections.observableArrayList(data -> new Observable[] {data.YValueProperty()});
        horizontalMarkers.addListener((InvalidationListener) observable -> layoutPlotChildren());
        verticalMarkers = FXCollections.observableArrayList(data -> new Observable[] {data.XValueProperty()});
        verticalMarkers.addListener((InvalidationListener)observable -> layoutPlotChildren());
    }

    public void addHorizontalValueMarker(Data<X, Y> marker) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (horizontalMarkers.contains(marker)) return;
        Line line = new Line();
        marker.setNode(line );
        getPlotChildren().add(line);
        horizontalMarkers.add(marker);
    }

    public void removeHorizontalValueMarker(Data<X, Y> marker) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (marker.getNode() != null) {
            getPlotChildren().remove(marker.getNode());
            marker.setNode(null);
        }
        horizontalMarkers.remove(marker);
    }

    public void addVerticalValueMarker(Data<X, Y> marker) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (verticalMarkers.contains(marker)) return;
        Line line = new Line();
        marker.setNode(line );
        getPlotChildren().add(line);
        verticalMarkers.add(marker);
    }

    public void removeVerticalValueMarker(Data<X, Y> marker) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (marker.getNode() != null) {
            getPlotChildren().remove(marker.getNode());
            marker.setNode(null);
        }
        verticalMarkers.remove(marker);
    }


    @Override
    protected void layoutPlotChildren() {
        super.layoutPlotChildren();
        for (Data<X, Y> horizontalMarker : horizontalMarkers) {
            Line line = (Line) horizontalMarker.getNode();
            line.setStartX(0);
            line.setEndX(getBoundsInLocal().getWidth());
            line.setStartY(getYAxis().getDisplayPosition(horizontalMarker.getYValue()) + 0.5); // 0.5 for crispness
            line.setEndY(line.getStartY());
            line.toFront();
        }
        for (Data<X, Y> verticalMarker : verticalMarkers) {
            Line line = (Line) verticalMarker.getNode();
            line.setStartX(getXAxis().getDisplayPosition(verticalMarker.getXValue()) + 0.5);  // 0.5 for crispness
            line.setEndX(line.getStartX());
            line.setStartY(0d);
            line.setEndY(getBoundsInLocal().getHeight());
            line.toFront();
        }
    }

}
