package com.zoicapital.stockchartsfx;

import com.ly.quant.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JSoupTest {

    public static void main(String[] args) throws IOException {
        List<Article> list = new ArrayList<>();
        String start = "09-11";
        String end = "09-11";
        getArticles("601330",start,end,1,list);
        list.forEach(e ->{
            System.out.println(e);
        });
    }

    private static void getArticles(String code, String start,String end, Integer pageNum ,List<Article> list) throws IOException {
        String url ="http://guba.eastmoney.com/list,%s,f_%s.html";
        Document doc =   Jsoup.connect(String.format(url,code,pageNum)).get();
        Element articles = doc.getElementById("articlelistnew");
        Elements allElements = articles.getElementsByClass("articleh");
        int i = 0;
        for( ;i<allElements.size();i++){
            Element elements= allElements.get(i);
            Elements title = elements.getElementsByClass("l3");
            Elements readCount = elements.getElementsByClass("l1");
            Elements create = elements.getElementsByClass("l6");
            if(title.size()==0){
                continue;
            }
            //讨论 活动  新闻
            Elements tlhd = title.get(0).getElementsByTag("em");
            if(tlhd.size()!=0){
                continue;
            }
            Element titleEle = title.select("a[title]").get(0);

            String date = create.get(0).text();
            if(start.compareTo(date)>0 ||end.compareTo(date)<0 ){
                break;
            }
            Article article = new Article();
            article.setDate(date);
            article.setTitle(titleEle.attributes().get("title"));
            article.setReadCount(Integer.parseInt(readCount.get(0).text()));
            list.add(article);
        }

        if(i==allElements.size()){
            Article article = list.get(list.size() - 1);
            if(article.getDate().compareTo(start)>= 0){
                getArticles(code,start,end,++pageNum,list);
            }
        }
    }
}
