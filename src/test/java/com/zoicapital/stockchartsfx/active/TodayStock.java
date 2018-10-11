package com.zoicapital.stockchartsfx.active;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: BG320587
 * @Date: 2018/10/11 13:14
 */
public class TodayStock {

    public static void main(String[] args) throws IOException {

        String[] ind = {"股票名称","今日开盘价","昨日收盘价","当前价格","今日最高价","今日最低价","买一报价","卖一报价","成交股数",
                "成交金额","买一股数","买一报价","买二股数","买二报价","买三股数","买三报价","买四股数","买四报价","买五股数","买五报价",
                "卖一股数","卖一报价","卖二股数","卖二报价","卖三股数","卖三报价","卖四股数","卖四报价","卖五股数","卖五报价","当前日期","当前时间"};
        String ss = getHtmlConentByUrl("http://hq.sinajs.cn/list=sz002230");//你要访问的页面
        String[] fcs = ss.split("\"");          //按"号分割字符
        String scs = fcs[1];
        String[] tcs = scs.split(",");          //按,号分割字符
        System.out.println("open:"+ tcs[1]);
        System.out.println("close:"+tcs[3]);
        System.out.println("high:"+tcs[4]);
        System.out.println("close:"+tcs[5]);
//        for(int i=0;i<tcs.length-1;i++)
//            System.out.println(ind[i] + "： " +tcs[i]);
    }

    public static String getHtmlConentByUrl(String ssourl)
    {
        try
        {
            URL url = new URL(ssourl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setInstanceFollowRedirects(false);
            con.setUseCaches(false);
            con.setAllowUserInteraction(false);
            con.connect();
            StringBuffer sb = new StringBuffer();
            String line = "";
            BufferedReader URLinput = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            while ((line = URLinput.readLine()) != null)
            {
                sb.append(line);
            }
            con.disconnect();

            return sb.toString().toLowerCase();
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
