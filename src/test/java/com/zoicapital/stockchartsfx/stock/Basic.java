package com.zoicapital.stockchartsfx.stock;


import lombok.Data;

@Data
public class Basic {

    /**
     *  振幅
     */
    private  Double amplitudeRatio;

    private Double  asset;


    /**
     *  流通市值
     */
    private Double capitalization;


    /**
     * 收盘价
     */
    private Double close;


    private String exchange;


    private Integer followNum;

    /**
     * 最高价
     */
    private Double high;

    /**
     * 最低价
     */
    private Double low;


    /**
     *  涨跌
     */
    private Double netChange;

    /**
     * 涨幅率
     */
    private Double netChangeRatio;

    /**
     * 开盘价
     */
    private Double open;

    /**
     * 前一是收盘价
     */
    private Double preClose;

    private String stockCode;

    private String stockName;


    private String stockStatus;


    /**
     * 换手率
     */
    private Double turnoverRatio;


    /**
     *  总股本
     */
    private Double volume;


}
