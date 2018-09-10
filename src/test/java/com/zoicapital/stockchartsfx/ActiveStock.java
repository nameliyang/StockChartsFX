package com.zoicapital.stockchartsfx;

import com.brucezee.jspider.Page;
import com.brucezee.jspider.Request;
import com.brucezee.jspider.Result;
import com.brucezee.jspider.Spider;
import com.brucezee.jspider.processor.PageProcessor;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: BG320587
 * @Date: 2018/9/10 12:34
 */
public class ActiveStock{

    private static final Logger logger = LoggerFactory.getLogger(ActiveStock.class);

    public static void main(String[] args) throws Exception {

        Document doc = Jsoup.connect("http://guba.eastmoney.com/list,000970.html").get();
        System.out.println(doc);

//        Spider.create()                                                 //创建爬虫实例
//                .addStartRequests("http://guba.eastmoney.com/list,000970.html")           //添加起始url
//                .setPageProcessor(new HelloWorldPageProcessor())        //设置页面解析器
//                .start();
    }

    public static String  get(String path ) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(inStream,bos);
        String result = new String(bos.toByteArray(), "UTF-8");
        return result;
    }

    public static class HelloWorldPageProcessor implements PageProcessor {
        @Override
        public Result process(Request request, Page page) {
            Result result = new Result();

            //解析HTML采用jsoup框架，详见：https://jsoup.org/

            //解析页面标题
            result.put("title", page.document().title());

            //获取页面上的新的链接地址
            Elements elements = page.document().getElementsByAttributeValue("class","articleh");        //获取所有a标签
            for (int i = 0; i < elements.size(); i++) {
                Element element = elements.get(i);
                Elements allElements = element.getAllElements();
                if(allElements.size()>2){
                    Element contentOuter = element.getAllElements().get(3);
                    String  title = contentOuter.getAllElements().get(0).getElementsByAttribute("title").attr("title");
                    String date = element.getElementsByTag("span").get(4).childNodes().get(0).toString();
                    System.out.println(title+ "-->"+date );
                }

            }
            return result;
        }
    }







}
