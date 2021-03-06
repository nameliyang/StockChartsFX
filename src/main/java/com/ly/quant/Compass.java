package com.ly.quant;

import com.zoicapital.stockchartsfx.DailyStock;
import com.zoicapital.stockchartsfx.StockSpider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Compass {


    public static void main(String[] args) throws IOException {

        List<Map<String, String>> allStock = getAllStock();
        for (Map stock : allStock) {
            System.out.println(stock);
        }
        Date  end = new Date();
        Date start = stepMonth(end, -11);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<DailyStock> dateStocks = getDateStocks("000970",sdf.format(start), sdf.format(end));
        if(dateStocks.size()>36){
            dateStocks = dateStocks.subList(30,dateStocks.size());
        }


        for(int i = 0;i<dateStocks.size();i++){
            doStagegy(dateStocks.get(i));
        }
    }

    static boolean goOn = true;
    private static void doStagegy(DailyStock dailyStock) {
        Double macd = dailyStock.getMacd();
        if(macd<0 && !goOn){
            goOn = true;
        }
        if(goOn){

        }
        System.out.println(dailyStock);
    }

    public static List<DailyStock>    getDateStocks(String code, String start, String end) throws IOException {
        List<DailyStock> dailyStocks = StockHistory.getDailyStockByTushare(code,start,end);

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
        return dailyStocks;
    }

    public static List<Map<String, String>> getAllStock() {
        List<Map<String, String>> stockCodes = StockSpider.getStockCodes(Stream.of('2', '5').collect(Collectors.toList()));
        stockCodes = stockCodes.stream().filter(map -> !map.get("name").contains("ST")).collect(Collectors.toList());

        List<String> lists = stockCodes.stream().map(e -> {
            return e.get("code") + "(" + e.get("name") + ")";
        }).collect(Collectors.toList());
        System.out.println(stockCodes.size());
        final List<Map<String, String>> stCollect = stockCodes.stream().filter(map -> !map.get("name").contains("ST")).collect(Collectors.toList());
        return stCollect;
    }

    public static Date stepMonth(Date sourceDate, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(sourceDate);
        c.add(Calendar.MONTH, month);

        return c.getTime();
    }
}
