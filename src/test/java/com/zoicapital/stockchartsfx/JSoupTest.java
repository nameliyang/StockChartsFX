package com.zoicapital.stockchartsfx;

import com.ly.quant.Article;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JSoupTest {
    static final  ExecutorService executorService = Executors.newFixedThreadPool(10);
    public static void main(String[] args) throws IOException, InterruptedException {

        String start = "09-11";
        String end = "09-11";

        List<Map<String, String>> stockCodes = StockSpider.getStockCodes(Stream.of('2', '5').collect(Collectors.toList()));
        stockCodes = stockCodes.stream().filter(map -> !map.get("name").contains("ST")).collect(Collectors.toList());
        final  List<Map<String, String>> tmp = stockCodes;



        List<Map<String,String>> articles = new ArrayList<>();
        CountDownLatch cdl = new CountDownLatch(tmp.size());
        new Thread(()->{
            for(Map<String,String> map:tmp){
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        String code = map.get("code");
                        String name = map.get("name");
                        List<Article> list = new ArrayList<>();
                        try {
                            getArticles(code,start,end,1,list);
                            final AtomicInteger readTotal = new AtomicInteger(0);
                            list.forEach(e ->readTotal.addAndGet(e.getReadCount()));
                            System.out.println(String.format("code:%s,name:%s,articls:%d,redcount:%d",code,name,list.size(),readTotal.get()));
                            Map<String,String> info = new HashMap<>();
                            info.put("code",code);
                            info.put("name",name);
                            info.put("articls",String.valueOf(list.size()));
                            info.put("readTotal",String.valueOf(readTotal));
                            articles.add(info);
                        } catch (IOException e) {
                            System.out.println(code+"--->" +e.getMessage());
                        }finally {
                            cdl.countDown();
                        }
                    }
                });
            }
            executorService.shutdown();
        }).start();

        cdl.await();
        System.out.println("-------------------------------------------");
        System.out.println("-------------------------------------------");
        System.out.println("-------------------------------------------");
        System.out.println("-------------------------------------------");
        System.out.println("-------------------------------------------");
        System.out.println("-------------------------------------------");
        articles.sort((e1,e2)->e1.get("articls").compareTo(e2.get("articls")));
        articles.forEach(e -> System.out.println(e));

    }

    private static void getArticles(String code, String start,String end, Integer pageNum ,List<Article> list) throws IOException {
        if(pageNum>20){
            throw new RuntimeException(code+"太多了");
        }


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
            if(start.compareTo(date)>0  ){
                break;
            }

           if( end.compareTo(date)<=0 && start.compareTo(date)>=0 ){
               Article article = new Article();
               article.setDate(date);
               article.setTitle(titleEle.attributes().get("title"));
               article.setReadCount(Integer.parseInt(readCount.get(0).text()));
               list.add(article);
           }
        }

        if(i==allElements.size()){
            if(CollectionUtils.isEmpty(list)|| list.get(list.size() - 1).getDate().compareTo(start)>= 0){
                getArticles(code,start,end,++pageNum,list);
            }
        }
    }
}
