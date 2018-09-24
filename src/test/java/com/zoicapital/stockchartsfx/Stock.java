package com.zoicapital.stockchartsfx;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Stock {
    @JSONField(format="yyyy-MM-dd")
    private Date date;
    private String exchange;

    private String code;

    private String name;

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
     * 开盘价
     */
    private Double open;

    /**
     * 收盘价
     */
    private Double close;

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
     * 前一是收盘价
     */
    private Double preClose;


    private String stockStatus;

    /**
     * 换手率
     */
    private Double turnoverRatio;

    /**
     *  总股本
     */
    private Double volume;


    /**
     * 行业
     */
    private String industry;


    /**
     * 主要业务
     */
    private String mainBusiness;

    /**
     *  业绩同比增长率(营业收入)
     */
    private Double  majoGrow;

    /**
     * 业绩同比增长率(净利润)
     */
    private Double netIncreaseRate;


    /**
     * 相关概念
     */
    private List<String> relatedConcept;

    /**
     *
     */
    private Integer followNum;


    @Override
    public String toString() {
        return "Stock{" +
                "exchange='" + exchange + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", amplitudeRatio=" + amplitudeRatio +
                ", asset=" + asset +
                ", capitalization=" + capitalization +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", netChange=" + netChange +
                ", netChangeRatio=" + netChangeRatio +
                ", preClose=" + preClose +
                ", stockStatus='" + stockStatus + '\'' +
                ", turnoverRatio=" + turnoverRatio +
                ", volume=" + volume +
                ", industry='" + industry + '\'' +
                ", mainBusiness='" + mainBusiness + '\'' +
                ", majoGrow=" + majoGrow +
                ", netIncreaseRate=" + netIncreaseRate +
                ", relatedConcept=" + relatedConcept +
                ", followNum=" + followNum +
                '}';
    }
}
