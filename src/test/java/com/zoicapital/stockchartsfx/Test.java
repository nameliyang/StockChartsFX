package com.zoicapital.stockchartsfx;

import com.zoicapital.stockchartsfx.stock.StockFilter;
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

        List<Stock> stockList = getStockList(e ->{
            if(e.getCode().startsWith("2")|| e.getCode().startsWith("1")||e.getCode().startsWith("5")){
                return false;
            }
            return true;
        });

        stockList.forEach(e ->System.out.println(e));


        System.out.println(stockList.size());
    }

    public static List<Stock> getStockList(StockFilter stockFilter) throws IOException {
        List<Stock> stockList = getStockList();
        return stockList.stream().filter(e -> stockFilter.isAccept(e)).collect(Collectors.toList());
    }


    public static List<Stock>  getStockList() throws IOException {
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
            listStock.add(StockBuilder.aStock().buildCode(codeValueArray[1]).buildName(codeValueArray[0]).buildSource("sh").build());
        }
        //sz
        Elements szStocks = ul.get(1).getElementsByTag("li");
        for (Element szStock : szStocks) {
            String codeValue = szStock.getElementsByAttributeValue("target", "_blank").get(0).text();
            String[] codeValueArray = parseCodeName(codeValue);
            listStock.add(StockBuilder.aStock().buildCode(codeValueArray[1]).buildName(codeValueArray[0]).buildSource("sz").build());
        }
        return listStock;
    }

    public static String[] parseCodeName(String codeName) {
        String[] rtn = new String[2];
        int index = codeName.indexOf('(');
        rtn[0] = codeName.substring(0, index);
        rtn[1] = codeName.substring(index + 1, codeName.length() - 1);
        return rtn;
    }

}




