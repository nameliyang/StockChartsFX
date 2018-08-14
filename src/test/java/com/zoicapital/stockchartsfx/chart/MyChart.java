package com.zoicapital.stockchartsfx.chart;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class MyChart extends XYChart<Number,Number> {


    public MyChart(){
        this(new NumberAxis(),new NumberAxis());
    }


    public MyChart(NumberAxis  numberAxisAxis, NumberAxis yAxis) {
        super(numberAxisAxis, yAxis);
    }

    public static void main(String[] args) {

    }

    @Override
    protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {

    }

    @Override
    protected void dataItemRemoved(Data<Number, Number> item, Series<Number, Number> series) {

    }

    @Override
    protected void dataItemChanged(Data<Number, Number> item) {

    }

    @Override
    protected void seriesAdded(Series<Number, Number> series, int seriesIndex) {

    }

    @Override
    protected void seriesRemoved(Series<Number, Number> series) {

    }

    @Override
    protected void layoutPlotChildren() {

    }
}
