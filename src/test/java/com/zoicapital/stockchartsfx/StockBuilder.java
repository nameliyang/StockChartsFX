package com.zoicapital.stockchartsfx;

/**
 * @Author: BG320587
 * @Date: 2018/9/14 12:51
 */
public final class StockBuilder {
    private String exchange;
    private String code;
    private String name;

    private StockBuilder() {
    }

    public static StockBuilder aStock() {
        return new StockBuilder();
    }

    public StockBuilder buildExchange(String source) {
        this.exchange = source;
        return this;
    }

    public StockBuilder buildCode(String code) {
        this.code = code;
        return this;
    }

    public StockBuilder buildName(String name) {
        this.name = name;
        return this;
    }

    public Stock build() {
        Stock stock = new Stock();
        stock.setExchange(exchange);
        stock.setCode(code);
        stock.setName(name);
        return stock;
    }
}
