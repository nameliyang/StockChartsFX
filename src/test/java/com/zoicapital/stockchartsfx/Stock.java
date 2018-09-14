package com.zoicapital.stockchartsfx;

import lombok.Data;

@Data
public class Stock {

    private String source;

    private String code;

    private String name;

    @Override
    public String toString() {
        return "Stock{" +
                "source='" + source + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
