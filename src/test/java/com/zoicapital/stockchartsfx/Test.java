package com.zoicapital.stockchartsfx;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.*;
import com.zoicapital.stockchartsfx.stock.StockFilter;
import com.zoicapital.stockchartsfx.stock.Stocks;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test {






    public static void main(String[] args) throws IOException {
        List<Stock> stocks = new Stocks().getStockList(e -> {
            if (e.getCode().startsWith("2") || e.getCode().startsWith("1") || e.getCode().startsWith("5") || e.getName().contains("ST")) {
                return false;
            }
            return true;
        }).value();
        System.out.println(stocks.size());
//        List<Stock> stockList = getStockList(e ->{
//            if(e.getCode().startsWith("2")|| e.getCode().startsWith("1")||e.getCode().startsWith("5")){
//                return false;
//            }
//            return true;
//        });
//
//        stockList.forEach(e ->System.out.println(e));
//
//        System.out.println(stockList.size());
    }


}




