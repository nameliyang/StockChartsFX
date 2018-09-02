package com.zoicapital.stockchartsfx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import org.apache.commons.lang.time.DateUtils;

import java.io.*;
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
    public static void main(String[] args) throws IOException {

        String str = "[{\"date\":\"2018-04-02\",\"open\":12.198,\"close\":12.317,\"high\":12.456,\"low\":12.168,\"volume\":277581.0,\"code\":970},{\"date\":\"2018-04-03\",\"open\":12.129,\"close\":11.811,\"high\":12.129,\"low\":11.712,\"volume\":252955.0,\"code\":970},{\"date\":\"2018-04-04\",\"open\":11.91,\"close\":11.692,\"high\":12.079,\"low\":11.692,\"volume\":153102.0,\"code\":970},{\"date\":\"2018-04-09\",\"open\":11.613,\"close\":12.01,\"high\":12.129,\"low\":11.603,\"volume\":224116.0,\"code\":970},{\"date\":\"2018-04-10\",\"open\":11.96,\"close\":12.178,\"high\":12.267,\"low\":11.91,\"volume\":232593.0,\"code\":970},{\"date\":\"2018-04-11\",\"open\":12.307,\"close\":12.317,\"high\":12.436,\"low\":12.198,\"volume\":276376.0,\"code\":970},{\"date\":\"2018-04-12\",\"open\":12.307,\"close\":12.0,\"high\":12.307,\"low\":12.0,\"volume\":161100.0,\"code\":970},{\"date\":\"2018-04-13\",\"open\":12.109,\"close\":11.91,\"high\":12.188,\"low\":11.871,\"volume\":137343.0,\"code\":970},{\"date\":\"2018-04-16\",\"open\":11.91,\"close\":11.791,\"high\":12.039,\"low\":11.633,\"volume\":133163.0,\"code\":970},{\"date\":\"2018-04-17\",\"open\":11.821,\"close\":11.514,\"high\":12.019,\"low\":11.514,\"volume\":143144.0,\"code\":970},{\"date\":\"2018-04-18\",\"open\":11.672,\"close\":11.851,\"high\":11.95,\"low\":11.325,\"volume\":187942.0,\"code\":970},{\"date\":\"2018-04-19\",\"open\":11.881,\"close\":12.0,\"high\":12.119,\"low\":11.821,\"volume\":228658.0,\"code\":970},{\"date\":\"2018-04-20\",\"open\":11.92,\"close\":11.553,\"high\":11.95,\"low\":11.504,\"volume\":173901.0,\"code\":970},{\"date\":\"2018-04-23\",\"open\":11.504,\"close\":11.256,\"high\":11.613,\"low\":11.058,\"volume\":151241.0,\"code\":970},{\"date\":\"2018-04-24\",\"open\":11.266,\"close\":11.464,\"high\":11.484,\"low\":11.206,\"volume\":113217.0,\"code\":970},{\"date\":\"2018-04-25\",\"open\":11.385,\"close\":11.593,\"high\":11.672,\"low\":11.355,\"volume\":136928.0,\"code\":970},{\"date\":\"2018-04-26\",\"open\":11.603,\"close\":11.196,\"high\":11.613,\"low\":11.196,\"volume\":120322.0,\"code\":970},{\"date\":\"2018-04-27\",\"open\":11.087,\"close\":10.919,\"high\":11.206,\"low\":10.73,\"volume\":165073.0,\"code\":970},{\"date\":\"2018-05-02\",\"open\":10.958,\"close\":10.8,\"high\":10.998,\"low\":10.71,\"volume\":97312.0,\"code\":970},{\"date\":\"2018-05-03\",\"open\":10.849,\"close\":10.958,\"high\":11.008,\"low\":10.611,\"volume\":101459.0,\"code\":970},{\"date\":\"2018-05-04\",\"open\":10.939,\"close\":10.899,\"high\":11.177,\"low\":10.889,\"volume\":95353.0,\"code\":970},{\"date\":\"2018-05-07\",\"open\":10.899,\"close\":11.206,\"high\":11.246,\"low\":10.899,\"volume\":118726.0,\"code\":970},{\"date\":\"2018-05-08\",\"open\":11.236,\"close\":11.206,\"high\":11.256,\"low\":11.107,\"volume\":90740.0,\"code\":970},{\"date\":\"2018-05-09\",\"open\":11.206,\"close\":11.206,\"high\":11.236,\"low\":11.107,\"volume\":99227.0,\"code\":970},{\"date\":\"2018-05-10\",\"open\":11.226,\"close\":11.206,\"high\":11.266,\"low\":11.107,\"volume\":83796.0,\"code\":970},{\"date\":\"2018-05-11\",\"open\":11.216,\"close\":11.801,\"high\":11.841,\"low\":11.117,\"volume\":334922.0,\"code\":970},{\"date\":\"2018-05-14\",\"open\":11.801,\"close\":11.742,\"high\":12.0,\"low\":11.633,\"volume\":228795.0,\"code\":970},{\"date\":\"2018-05-15\",\"open\":12.178,\"close\":11.742,\"high\":12.277,\"low\":11.553,\"volume\":262807.0,\"code\":970},{\"date\":\"2018-05-16\",\"open\":11.623,\"close\":11.583,\"high\":11.821,\"low\":11.504,\"volume\":131525.0,\"code\":970},{\"date\":\"2018-05-17\",\"open\":11.553,\"close\":11.573,\"high\":11.732,\"low\":11.484,\"volume\":86419.0,\"code\":970},{\"date\":\"2018-05-18\",\"open\":11.573,\"close\":11.593,\"high\":11.672,\"low\":11.385,\"volume\":101805.0,\"code\":970},{\"date\":\"2018-05-21\",\"open\":11.643,\"close\":11.762,\"high\":11.851,\"low\":11.643,\"volume\":121315.0,\"code\":970},{\"date\":\"2018-05-22\",\"open\":11.781,\"close\":11.682,\"high\":11.791,\"low\":11.504,\"volume\":92884.0,\"code\":970},{\"date\":\"2018-05-23\",\"open\":11.682,\"close\":11.355,\"high\":11.682,\"low\":11.345,\"volume\":109395.0,\"code\":970},{\"date\":\"2018-05-24\",\"open\":11.355,\"close\":11.266,\"high\":11.484,\"low\":11.246,\"volume\":75848.0,\"code\":970},{\"date\":\"2018-05-25\",\"open\":11.286,\"close\":11.077,\"high\":11.355,\"low\":11.038,\"volume\":95585.0,\"code\":970},{\"date\":\"2018-05-28\",\"open\":11.117,\"close\":10.988,\"high\":11.147,\"low\":10.939,\"volume\":61634.0,\"code\":970},{\"date\":\"2018-05-29\",\"open\":11.018,\"close\":10.988,\"high\":11.087,\"low\":10.909,\"volume\":70161.0,\"code\":970},{\"date\":\"2018-05-30\",\"open\":10.77,\"close\":10.879,\"high\":11.246,\"low\":10.73,\"volume\":175358.0,\"code\":970},{\"date\":\"2018-05-31\",\"open\":10.919,\"close\":10.958,\"high\":11.077,\"low\":10.78,\"volume\":100619.0,\"code\":970},{\"date\":\"2018-06-01\",\"open\":10.958,\"close\":10.75,\"high\":11.028,\"low\":10.691,\"volume\":94664.0,\"code\":970},{\"date\":\"2018-06-04\",\"open\":10.77,\"close\":10.681,\"high\":10.849,\"low\":10.661,\"volume\":68792.0,\"code\":970},{\"date\":\"2018-06-05\",\"open\":10.681,\"close\":10.919,\"high\":10.948,\"low\":10.681,\"volume\":76107.0,\"code\":970},{\"date\":\"2018-06-06\",\"open\":10.948,\"close\":10.958,\"high\":11.087,\"low\":10.859,\"volume\":97208.0,\"code\":970},{\"date\":\"2018-06-07\",\"open\":11.107,\"close\":10.889,\"high\":11.147,\"low\":10.879,\"volume\":84381.0,\"code\":970},{\"date\":\"2018-06-08\",\"open\":10.889,\"close\":10.71,\"high\":10.958,\"low\":10.562,\"volume\":96261.0,\"code\":970},{\"date\":\"2018-06-11\",\"open\":10.681,\"close\":10.621,\"high\":10.8,\"low\":10.572,\"volume\":58618.0,\"code\":970},{\"date\":\"2018-06-12\",\"open\":10.562,\"close\":10.73,\"high\":10.77,\"low\":10.462,\"volume\":76228.0,\"code\":970},{\"date\":\"2018-06-13\",\"open\":10.71,\"close\":10.512,\"high\":10.77,\"low\":10.512,\"volume\":64032.0,\"code\":970},{\"date\":\"2018-06-14\",\"open\":10.611,\"close\":10.78,\"high\":10.889,\"low\":10.581,\"volume\":139108.0,\"code\":970},{\"date\":\"2018-06-15\",\"open\":10.79,\"close\":10.72,\"high\":10.83,\"low\":10.55,\"volume\":110790.0,\"code\":970},{\"date\":\"2018-06-19\",\"open\":10.55,\"close\":9.65,\"high\":10.55,\"low\":9.65,\"volume\":221635.0,\"code\":970},{\"date\":\"2018-06-20\",\"open\":9.64,\"close\":9.59,\"high\":9.69,\"low\":9.3,\"volume\":152280.0,\"code\":970},{\"date\":\"2018-06-21\",\"open\":9.59,\"close\":9.16,\"high\":9.79,\"low\":9.15,\"volume\":142935.0,\"code\":970},{\"date\":\"2018-06-22\",\"open\":9.0,\"close\":9.22,\"high\":9.31,\"low\":8.87,\"volume\":111227.0,\"code\":970},{\"date\":\"2018-06-25\",\"open\":9.22,\"close\":9.04,\"high\":9.39,\"low\":9.03,\"volume\":84841.0,\"code\":970},{\"date\":\"2018-06-26\",\"open\":9.01,\"close\":9.13,\"high\":9.2,\"low\":8.9,\"volume\":91390.0,\"code\":970},{\"date\":\"2018-06-27\",\"open\":9.12,\"close\":9.35,\"high\":9.35,\"low\":9.05,\"volume\":136574.0,\"code\":970},{\"date\":\"2018-06-28\",\"open\":9.27,\"close\":9.12,\"high\":9.41,\"low\":9.11,\"volume\":103648.0,\"code\":970},{\"date\":\"2018-06-29\",\"open\":9.15,\"close\":9.35,\"high\":9.38,\"low\":9.08,\"volume\":96507.0,\"code\":970},{\"date\":\"2018-07-02\",\"open\":9.33,\"close\":9.19,\"high\":9.42,\"low\":9.08,\"volume\":86675.0,\"code\":970},{\"date\":\"2018-07-03\",\"open\":9.17,\"close\":9.31,\"high\":9.34,\"low\":9.01,\"volume\":92072.0,\"code\":970},{\"date\":\"2018-07-04\",\"open\":9.24,\"close\":9.25,\"high\":9.5,\"low\":9.19,\"volume\":94519.0,\"code\":970},{\"date\":\"2018-07-05\",\"open\":9.2,\"close\":8.95,\"high\":9.26,\"low\":8.88,\"volume\":96480.0,\"code\":970},{\"date\":\"2018-07-06\",\"open\":8.97,\"close\":9.06,\"high\":9.12,\"low\":8.73,\"volume\":110346.0,\"code\":970},{\"date\":\"2018-07-09\",\"open\":8.95,\"close\":9.25,\"high\":9.29,\"low\":8.95,\"volume\":96417.0,\"code\":970},{\"date\":\"2018-07-10\",\"open\":9.25,\"close\":9.39,\"high\":9.45,\"low\":9.24,\"volume\":112183.0,\"code\":970},{\"date\":\"2018-07-11\",\"open\":9.59,\"close\":9.43,\"high\":9.85,\"low\":9.39,\"volume\":297478.0,\"code\":970},{\"date\":\"2018-07-12\",\"open\":9.3,\"close\":10.02,\"high\":10.26,\"low\":9.3,\"volume\":361286.0,\"code\":970},{\"date\":\"2018-07-13\",\"open\":10.03,\"close\":10.01,\"high\":10.15,\"low\":9.9,\"volume\":182227.0,\"code\":970},{\"date\":\"2018-07-16\",\"open\":10.0,\"close\":9.97,\"high\":10.04,\"low\":9.82,\"volume\":93745.0,\"code\":970},{\"date\":\"2018-07-17\",\"open\":9.98,\"close\":9.89,\"high\":10.03,\"low\":9.83,\"volume\":105696.0,\"code\":970},{\"date\":\"2018-07-18\",\"open\":9.91,\"close\":9.68,\"high\":9.96,\"low\":9.67,\"volume\":99959.0,\"code\":970},{\"date\":\"2018-07-19\",\"open\":9.77,\"close\":10.2,\"high\":10.2,\"low\":9.57,\"volume\":161023.0,\"code\":970},{\"date\":\"2018-07-20\",\"open\":10.1,\"close\":10.06,\"high\":10.22,\"low\":9.95,\"volume\":177001.0,\"code\":970},{\"date\":\"2018-07-23\",\"open\":9.99,\"close\":10.11,\"high\":10.16,\"low\":9.88,\"volume\":113828.0,\"code\":970},{\"date\":\"2018-07-24\",\"open\":10.11,\"close\":10.21,\"high\":10.39,\"low\":10.01,\"volume\":180564.0,\"code\":970},{\"date\":\"2018-07-25\",\"open\":10.24,\"close\":10.16,\"high\":10.26,\"low\":10.14,\"volume\":87920.0,\"code\":970},{\"date\":\"2018-07-26\",\"open\":10.13,\"close\":9.99,\"high\":10.18,\"low\":9.91,\"volume\":92526.0,\"code\":970},{\"date\":\"2018-07-27\",\"open\":9.95,\"close\":9.94,\"high\":10.02,\"low\":9.85,\"volume\":56428.0,\"code\":970},{\"date\":\"2018-07-30\",\"open\":9.88,\"close\":9.88,\"high\":10.03,\"low\":9.85,\"volume\":64033.0,\"code\":970},{\"date\":\"2018-07-31\",\"open\":9.92,\"close\":9.81,\"high\":9.94,\"low\":9.8,\"volume\":47731.0,\"code\":970},{\"date\":\"2018-08-01\",\"open\":9.87,\"close\":9.62,\"high\":9.92,\"low\":9.59,\"volume\":72607.0,\"code\":970},{\"date\":\"2018-08-02\",\"open\":9.68,\"close\":9.3,\"high\":9.69,\"low\":9.12,\"volume\":99194.0,\"code\":970},{\"date\":\"2018-08-03\",\"open\":9.39,\"close\":9.22,\"high\":9.43,\"low\":9.21,\"volume\":52702.0,\"code\":970},{\"date\":\"2018-08-06\",\"open\":9.29,\"close\":9.0,\"high\":9.29,\"low\":9.0,\"volume\":60032.0,\"code\":970},{\"date\":\"2018-08-07\",\"open\":9.05,\"close\":9.25,\"high\":9.25,\"low\":8.92,\"volume\":67092.0,\"code\":970},{\"date\":\"2018-08-08\",\"open\":9.22,\"close\":9.13,\"high\":9.32,\"low\":9.11,\"volume\":52943.0,\"code\":970},{\"date\":\"2018-08-09\",\"open\":9.15,\"close\":9.34,\"high\":9.41,\"low\":9.12,\"volume\":71706.0,\"code\":970},{\"date\":\"2018-08-10\",\"open\":9.38,\"close\":9.35,\"high\":9.38,\"low\":9.22,\"volume\":45069.0,\"code\":970},{\"date\":\"2018-08-13\",\"open\":9.24,\"close\":9.33,\"high\":9.34,\"low\":9.11,\"volume\":52016.0,\"code\":970},{\"date\":\"2018-08-14\",\"open\":9.29,\"close\":9.4,\"high\":9.56,\"low\":9.22,\"volume\":90103.0,\"code\":970},{\"date\":\"2018-08-15\",\"open\":9.37,\"close\":9.3,\"high\":9.45,\"low\":9.15,\"volume\":55797.0,\"code\":970},{\"date\":\"2018-08-16\",\"open\":9.1,\"close\":9.14,\"high\":9.25,\"low\":9.06,\"volume\":40833.0,\"code\":970},{\"date\":\"2018-08-17\",\"open\":9.2,\"close\":8.9,\"high\":9.25,\"low\":8.87,\"volume\":70314.0,\"code\":970},{\"date\":\"2018-08-20\",\"open\":8.8,\"close\":8.8,\"high\":8.86,\"low\":8.55,\"volume\":87473.0,\"code\":970},{\"date\":\"2018-08-21\",\"open\":8.8,\"close\":8.86,\"high\":8.88,\"low\":8.78,\"volume\":49280.0,\"code\":970},{\"date\":\"2018-08-22\",\"open\":8.86,\"close\":8.66,\"high\":8.86,\"low\":8.63,\"volume\":49146.0,\"code\":970},{\"date\":\"2018-08-23\",\"open\":8.65,\"close\":8.68,\"high\":8.74,\"low\":8.62,\"volume\":59150.0,\"code\":970},{\"date\":\"2018-08-24\",\"open\":8.69,\"close\":8.67,\"high\":8.75,\"low\":8.55,\"volume\":56667.0,\"code\":970},{\"date\":\"2018-08-27\",\"open\":8.68,\"close\":8.85,\"high\":8.88,\"low\":8.66,\"volume\":67372.0,\"code\":970}]";
        JSONObject.parseArray(str);
    }

    public static List<DailyStock> getDailyStocks(String code) throws IOException {
        Date currentDate = Calendar.getInstance().getTime();
        Date date = DateUtils.addDays(currentDate, -150);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("start",new SimpleDateFormat("yyyy-MM-dd").format(date));
        jsonObject.put("end",new SimpleDateFormat("yyyy-MM-dd").format(currentDate));
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


    public static JSONArray doPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type","application/json");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
           }
        }
        result = result.substring(1,result.length()-1).replaceAll("\\\\","");
            return  JSONObject.parseArray(result);
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
