package com.zoicapital.stockchartsfx.stock;

import com.zoicapital.stockchartsfx.Stock;

/**
 * @Author: BG320587
 * @Date: 2018/9/14 12:56
 */
public interface StockFilter {

    boolean isAccept(Stock stock);
}
