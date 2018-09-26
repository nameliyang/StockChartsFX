package com.ly.quant;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import com.squareup.okhttp.*;
import com.zoicapital.stockchartsfx.DailyStock;
import org.apache.commons.lang.time.DateUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StockHistory {

    /**
     * code
     * start yyyy-mm-dd
     * end yyyy-mm-dd
     * @param code
     * @param start
     * @param end
     * @return
     */
    public static List<DailyStock> getDailyStockByTushare(String code,String start,String end) throws IOException {
        Date currentDate = Calendar.getInstance().getTime();
        Date date = DateUtils.addDays(currentDate, -150);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("start",start);
        jsonObject.put("end",end );
        JSONArray json = doPost("http://localhost:8383/get_k_data",jsonObject.toJSONString());
        List<DailyStock> stocks = new ArrayList<>();
        for(int i = 0;i<json.size();i++){
            JSONObject object = (JSONObject) json.get(i);
            DailyStock stock = new DailyStock();
            stock.setTOPEN(object.getString("open"));
            stock.setTCLOSE(object.getString("close"));
            stock.setHIGH(object.getString("high"));
            stock.setLOW(object.getString("low"));
            stock.setDate(object.getString("date"));
            stocks.add(stock);
        }
        return stocks;
    }


    public static JSONArray doPost(String url, String param) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url).post(RequestBody.create(MediaType.parse("application/json"),param))
                .build();
        Response responses = client.newCall(request).execute();
        String jsonData = responses.body().string();
        return JSONArray.parseArray(jsonData);
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

    public static List<DailyStock> getDailyStocks(String code, Date nowDate, Date before5Date) {

        return null;
    }
}
