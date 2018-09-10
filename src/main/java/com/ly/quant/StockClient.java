package com.ly.quant;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.*;
import com.zoicapital.stockchartsfx.DailyStock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class StockClient {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final  String url = "http://localhost:8383/get_k_data";


    public static List<DailyStock> getDailyStock(String code,String start,String end){
        List<DailyStock> stocks = new ArrayList<>();
        OkHttpClient httpClient = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("start", start);
        jsonObject.put("end",end);
        RequestBody requestBody = RequestBody.create(JSON, jsonObject.toJSONString());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            try (ResponseBody body = response.body()) {
                String content = body.string();
                JSONArray array = JSONObject.parseArray(content);
                for(int i = 0;i<array.size();i++){
                    JSONObject object = (JSONObject) array.get(i);
                    DailyStock stock = new DailyStock();
                    stock.setTOPEN(object.getString("open"));
                    stock.setTCLOSE(object.getString("close"));
                    stock.setHIGH(object.getString("high"));
                    stock.setLOW(object.getString("low"));
                    stock.setDate(object.getString("date"));
                    stocks.add(stock);
                }
                calcMACD(stocks);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stocks;
    }

    private static void calcMACD(List<DailyStock> stocks) {
        DailyStock preStock = stocks.get(0);
        for(int i = 1;i<stocks.size();i++){
            DailyStock dailyStock = stocks.get(i);
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
    }

    public static void main(String[] args) {
        List<DailyStock> dailyStock = getDailyStock("000970", "2017-07-01", "2018-09-01");
        Double money = 100000.0;
        List<DailyStock> stocks = dailyStock.subList(50,dailyStock.size());
        Double nowMoney = 100000.0;
        Double  stockCount = 0.0;
        Double stockMoney = 0.0;
        Strategy s = new Strategy();
        boolean isBuy = false;
        boolean isSell = false;
        s.add(stocks.get(0));
        for(int i = 1;i<stocks.size();i++){
            DailyStock stock     = stocks.get(i);
            s.add(stock);
            if(isSell){
                isSell = false;
                nowMoney = nowMoney + stockCount* Double.parseDouble(stock.getTOPEN());
                stockCount = 0.0;
                stockMoney = 0.0;
                System.out.println("sell date:"+stock.getDate() +"\tstockCount: "+stockCount+"\tstockyMoney:"+stockMoney+"\tnowMoney:"+nowMoney+"\ttotal:"+(stockMoney+nowMoney));
                continue;
            }

            if(isBuy){
                isBuy = false;
                Double buyMoney = nowMoney*0.1;
                nowMoney = nowMoney  - buyMoney;
                if(nowMoney<0){
                    System.out.println("没有钱了 sotckcount="+stock);
                    break;
                }
                stockCount = stockCount+  buyMoney / Double.parseDouble(stock.getTOPEN());
                stockMoney = stockCount *  Double.parseDouble(stock.getTCLOSE());
                System.out.println("buy date:"+stock.getDate() +"\tstockCount: "+stockCount+"\tstockyMoney:"+stockMoney+"\tnowMoney:"+nowMoney+"\ttotal:"+(stockMoney+nowMoney));
            }
            if(s.isBuy(stock)){
                isBuy = true;
                continue;
            }
            if(stockCount>0 && s.isSeller(stock)){
                isSell = true;
            }
            if(mostLoss(money,stockMoney)){
                break;
            }
        }
    }

    public static  boolean mostLoss(Double initMoney,Double nowValue){
        if(nowValue<0){
            throw new RuntimeException("");
        }
//        if(nowValue*100/initMoney<=90){
//            return true;
//        }
        return false;
    }

}