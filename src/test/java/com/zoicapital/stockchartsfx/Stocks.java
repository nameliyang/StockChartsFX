package com.zoicapital.stockchartsfx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zoicapital.stockchartsfx.bean.Basic;
import com.zoicapital.stockchartsfx.stock.StockFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cglib.beans.BeanCopier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Stocks {

    private   List<Stock> stockList;


    public   Stocks  getStockList(StockFilter stockFilter) {
        try {
            stockList = getStocks();
            stockList.forEach(e->e.setDate(new Date()));
            if(stockFilter!=null){
                stockList  = stockList.stream().filter(e -> stockFilter.isAccept(e)).collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new RuntimeException("",e);
        }

        return this;
    }

    public   Stocks  getStockList( ) throws IOException {
        getStockList(null);
        return this;
    }

    public   List<Stock> getStocks() throws IOException {
        String url = "http://quote.eastmoney.com/stocklist.html";
        Document doc = Jsoup.connect(url).get();
        Element element = doc.getElementById("quotesearch");
        Elements ul = element.getElementsByTag("ul");
        //sh
        Elements shStocks = ul.get(0).getElementsByTag("li");
        List<Stock> listStock = new ArrayList<>();

        for (Element shStock : shStocks) {
            String codeValue = shStock.getElementsByAttributeValue("target", "_blank").get(0).text();
            String[] codeValueArray = parseCodeName(codeValue);
            listStock.add(StockBuilder.aStock().buildCode(codeValueArray[1]).buildName(codeValueArray[0]).buildExchange("sh").build());
        }
        //sz
        Elements szStocks = ul.get(1).getElementsByTag("li");
        for (Element szStock : szStocks) {
            String codeValue = szStock.getElementsByAttributeValue("target", "_blank").get(0).text();
            String[] codeValueArray = parseCodeName(codeValue);
            listStock.add(StockBuilder.aStock().buildCode(codeValueArray[1]).buildName(codeValueArray[0]).buildExchange("sz").build());
        }
        return listStock;
    }

    private   String[] parseCodeName(String codeName) {
        String[] rtn = new String[2];
        int index = codeName.indexOf('(');
        rtn[0] = codeName.substring(0, index);
        rtn[1] = codeName.substring(index + 1, codeName.length() - 1);
        return  rtn;
    }

    public  List<Stock>  value(){
        return stockList;
    }


    public   Stocks  fillBasicBatch()  {
        for(Stock stock:stockList){
            Basic basic = getBasic(stock);
            if(basic==null){
                continue;
            }
            BeanCopier.create(Basic.class,Stock.class,false).copy(basic,stock,null);
        }
        return this;
    }

    public Stocks fillFollowNum( ){
        for(Stock stock:stockList){
            Integer followNum = getFollowNum(stock);
            stock.setFollowNum(followNum);
        }
        return this;
    }


    private Basic getBasic(Stock stock){
        Response response;
        String url = "https://gupiao.baidu.com/api/rails/stockbasicbatch?from=pc&os_ver=1&cuid=xxx&vv=100&format=json&stock_code="+stock.getExchange()+stock.getCode()+"&timestamp="+System.currentTimeMillis();
        Request request = new Request.Builder().url(url).build();
        //创建OkHttpClient对象：目的是通过OkHttpClient初始化Call对象
        OkHttpClient client = new OkHttpClient();
        //通过初始化Call对象，来实现网络连接
        Call call = client.newCall(request);
        JSONObject data = null;
        try{
            response = call.execute();
            String  content= response.body().string();
            JSONObject json  = (JSONObject) JSONObject.parse(content);
            JSONArray array =  json.getJSONArray("data");
            if(array.size()==0){
                return null;
            }
            data  = (JSONObject) json.getJSONArray("data").get(0);
            if(data==null){
                System.out.println(" is null?");
            }
        }catch (Exception e){
            System.out.println(e);
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e1) {
            }
            return getBasic(stock);
        }
        return data.toJavaObject(Basic.class);
    }

    public Integer getFollowNum(Stock stock) {
        String url = "https://gupiao.baidu.com/api/rails/stockfollownum?from=pc&os_ver=1&cuid=xxx&vv=100&format=json&stock_code=" + stock.getExchange() + stock.getCode() + "&timestamp=" + System.currentTimeMillis();
        String content = null;
        Integer data = null;
        try {
            content = request(url);
            JSONObject object = (JSONObject) JSONObject.parse(content);
            data = object.getInteger("data");
        } catch (IOException e) {
            try {
                TimeUnit.SECONDS.sleep(3);
                return getFollowNum(stock);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        return data;
    }

    public String  request(String url ) throws IOException {
        Request request = new Request.Builder().url(url).build();
        //创建OkHttpClient对象：目的是通过OkHttpClient初始化Call对象
        OkHttpClient client = new OkHttpClient();
        //通过初始化Call对象，来实现网络连接
        Call call = client.newCall(request);
        JSONObject data = null;
        Response response = call.execute();
        String  content= response.body().string();
        return content;
    }


    public static void main(String[] args) {
        List<Stock> stocks = new Stocks().getStockList(e ->{
            if(e.getCode().startsWith("2")|| e.getCode().startsWith("1")||e.getCode().startsWith("5")){
                return false;
            }
            return true;
        }).fillBasicBatch().fillFollowNum().value();
        stocks.forEach(e-> System.out.println(e));
//        Stock stock = new Stock();
//        stock.setExchange("sz");
//        stock.setCode("000970");
//        Integer content = new Stocks().getFollowNum(stock);
//        System.out.println(content);
    }


}
