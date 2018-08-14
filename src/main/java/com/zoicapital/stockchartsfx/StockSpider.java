package com.zoicapital.stockchartsfx;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liyang
 */
public class StockSpider {

    public static void main(String[] args) throws IOException {

        List<Map<String, String>> stockCodes = getStockCodes(Stream.of('2', '5').collect(Collectors.toList()));
        for(Map<String,String> stocks :stockCodes){
            System.out.println(stocks);
        }
        System.out.println(stockCodes.size());

        final List<Map<String, String>> stCollect = stockCodes.stream().filter(map -> !map.get("name").contains("ST")).collect(Collectors.toList());
        System.out.println(stCollect.size());



    }


    public static List<Map<String,String>> getStockCodes(List<Character> filter){
        String str = doGet("http://quote.eastmoney.com/stocklist.html");
        List<Map<String,String>> mapList = new ArrayList<>();

        List<String> codes = getSubUtil(str, " <li><a target=\"_blank\" href=\".*\">(.*)</a></li>");


        Set<String> test = new HashSet<>();
        codes.forEach(code -> {
            Map<String,String> codeInfo  = getCode(code);
            String codeStr = codeInfo.get("code");
            boolean isFilter = false;
            for(Character c :filter){
                if(codeStr.startsWith(String.valueOf(c))){
                   isFilter = true;
                   break;
                }
            }
            if(!isFilter){
                mapList.add(codeInfo);
            }
        });
        return mapList;
    }

    public static Map<String,String> getCode(String codeStr){
        int index = codeStr.indexOf('(');
        String name = codeStr.substring(0,index);
        String code =  codeStr.substring(index+1,codeStr.length()-1);
        Map<String,String> maps = new HashMap<String,String>();
        maps.put("name",name);
        maps.put("code",code);
        return maps;
//        String ls=new  String();
//        Pattern pattern = Pattern.compile("(?<=\\()(.+?)(?=\\))");
//        Matcher matcher = pattern.matcher(managers);
//        if(matcher.find())
//            return matcher.group();
//        return null;
    }

    public static List<String> getSubUtil(String soap,String rgex){
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(rgex);
        // 匹配的模式
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            int i = 1;
            list.add(m.group(i));
            i++;
        }
        return list;
    }


    public static String doGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "GBK"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }
}
