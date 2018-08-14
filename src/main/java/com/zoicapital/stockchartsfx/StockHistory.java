package com.zoicapital.stockchartsfx;

import com.csvreader.CsvReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StockHistory {

    public static void main(String[] args) throws IOException {
        String code = "600326";
        String start = "20170806";
        String end = "20180806";
        List<DailyStock> dailyStockList = getDailyStocks(code, start, end);
        System.out.println(dailyStockList);
    }

    public static List<DailyStock> getDailyStocks(String code, String start, String end) throws IOException {
        String sysbmol = "";
        List<String> shList = Stream.of("5", "6", "9").collect(Collectors.toList());
        List<String> shList_1 = Stream.of("11", "13").collect(Collectors.toList());
        String symbol = null;
        if(shList.contains(code.substring(0,1)) || shList_1.contains(code)){
            symbol = "0";
        }else{
            symbol = "1";
        }
        String urlStr = "http://quotes.money.163.com/service/chddata.html?code="+symbol+code+"&start="+start+"&end="+end+
                    "&fields=TCLOSE;HIGH;LOW;TOPEN;LCLOSE;CHG;PCHG;TURNOVER;VOTURNOVER;VATURNOVER;TCAP;MCAP";
        URL url = new URL(urlStr);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setConnectTimeout(3*1000);

        InputStream inputStream = urlConnection.getInputStream();
        //FileUtils.copyInputStreamToFile(inputStream,new File("D:/test.xls"));

        CsvReader csvReader = new CsvReader(inputStream, Charset.forName("GBK"));
        // 读表头
        System.out.println(csvReader.readHeaders());
        List<DailyStock> dailyStockList = new ArrayList<DailyStock>();
        while (csvReader.readRecord()){
            DailyStock stock = new DailyStock();
            String close = csvReader.get("收盘价");
            String high = csvReader.get("最高价");
            String low = csvReader.get("最低价");
            String open = csvReader.get("开盘价");
            String date = csvReader.get("日期");
            stock.setDate(date);
            stock.setHIGH(high);
            stock.setLOW(low);
            stock.setTOPEN(open);
            stock.setTCLOSE(close);
            dailyStockList.add(stock);
        }
        dailyStockList = dailyStockList.stream().sorted((s1,s2)->s1.getDate().compareTo(s2.getDate())).collect(Collectors.toList());
        return dailyStockList;
    }
}
