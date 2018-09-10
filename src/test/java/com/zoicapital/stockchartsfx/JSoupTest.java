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
        String date = "09-10";
        getArticles("000970",date,list);
    }

    private static void getArticles(String code, String date, List<Article> list) throws IOException {
         String url ="http://guba.eastmoney.com/list,%s.html";
        Document doc =   Jsoup.connect(String.format(url,code)).get();
        Element articles = doc.getElementById("articlelistnew");
        Elements allElements = articles.getElementsByClass("articleh");
        for(int i = 0;i<allElements.size();i++){
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

            Article article = new Article();
            article.setDate(create.get(0).text());
            article.setTitle(titleEle.attributes().get("title"));
            article.setReadCount(Integer.parseInt(readCount.get(0).text()));
        }
    }
}
