package com.zoicapital.stockchartsfx.active;

import com.google.common.collect.Lists;
import com.ly.quant.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PageParse {

    private static final List<String> tag = Stream.of("新闻", "讨论", "悬赏", "新闻", "问董秘", "公告","置顶","研报").collect(Collectors.toList());

    private static final String BASE_URL = "http://guba.eastmoney.com";
    private static final Integer MAX_PAGE = 100;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(100);
    
    public List<Article> parse(String code, String name) {

        List<Article> articles = Lists.newArrayList();
        int page = 0;
        page:
        for (; page < MAX_PAGE; page++) {
            try {
                Document doc = Jsoup.connect("http://guba.eastmoney.com/list," + code + ",f_" + page + ".html").get();
                Element articlelistnew = doc.getElementById("articlelistnew");
                Elements articleh = articlelistnew.getElementsByClass("articleh");
                String link = null;
                List<Future<Article>> values = new ArrayList<>();
                for (Element element : articleh) {
                    if ("ad_topic".equals(element.attr("id"))) {
                        continue;
                    }
                    Element el3 = element.getElementsByClass("l3").get(0);
                    Elements anEnum = el3.getElementsByTag("em");
                    String title = null;
                    if (anEnum != null && anEnum.size() > 0) {
                        String text = anEnum.get(0).text();
                        if (tag.contains(text)) {
                            continue;
                        }
                    }
                    Element titleEle = el3.getElementsByTag("a").get(0);
                    link = titleEle.attr("href");
                    title = titleEle.attr("title");
                    String readCount = element.getElementsByClass("l1").text();
                    String commentsCount = element.getElementsByClass("l2").text();
                    String author = element.getElementsByClass("l4").text();
                    String updateDate = element.getElementsByClass("l5").text();
                    Article article = new Article(Integer.parseInt(readCount), title, null, code, name);
                    article.setCmtURL(BASE_URL + link);
                    article.setAuthor(author);
                    article.setUpdateDate(updateDate);
                    CommentParser<Article> commentParser = new CommentParser<>(article,element);
                    Future future = executorService.submit(commentParser);
                    values.add(future);
                }
                for(int i = 0;i< values.size();i++){
                    try{
                        Article article = values.get(i).get();
                        if (article.getCreateTime()==null|| article.getCreateTime().compareTo("2018-01-01 00:00:00") < 0) {
                            for(int j= i+1;i<values.size();i++){
                                Future<Article> articleFuture = values.get(j);
                                articleFuture.cancel(true);
                            }
                            break page;
                        }
                        articles.add(article);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
     //   System.out.println("------------------------------->"+page);
        return articles;
    }

    class CommentParser<Article> implements Callable{
        com.ly.quant.Article article;

        Element element;

        public CommentParser(com.ly.quant.Article article, Element element){
            this.article = article;
            this.element = element;
        }


        @Override
        public com.ly.quant.Article call() throws Exception {
            parseComments(null, article);
            return article;
        }
    }


    private void parseComments(String commentsCount, Article article) throws IOException {
        Document doc = Jsoup.connect(article.getCmtURL()).timeout(5000).get();
        Elements zwfbtime = doc.getElementsByClass("zwfbtime");
        if (zwfbtime == null || zwfbtime.size() == 0) {
            return;
        }
        String createText = zwfbtime.get(0).text();
        createText = createText.replaceAll("发表于", "").trim();
        int index = createText.lastIndexOf(" ");
        String time = createText.substring(0, index);
        String media = createText.substring(index + 1, createText.length());
        String content = doc.select("div[id=zwconbody]").text();
        article.setCreateTime(time.trim());
        article.setMedia(media.trim());
        article.setContent(content.trim());
        Elements comments = doc.getElementsByClass("zwli clearfix");
        for (Element element : comments) {
            String name = element.select(".zwlitxt > .zwlianame > .zwnick").text();
            String date = element.select(".zwlitxt > .zwlitime").text().replaceAll("发表于", "").trim();
            String comment = element.select(".zwlitxt > .zwlitext").text();
            //  article.getComments().add(new Comment(date,comment,name));
        }
    }

    public static void main(String[] args) throws IOException {
        PageParse pageParse = new PageParse();
        Article article = new Article();
        article.setCode("");
        article.setCmtURL("http://guba.eastmoney.com/news,000970,781826314.html");
        pageParse.parseComments("", article);
//        PageParse pageParse = necmd
//
//
//
// geParse();
//        List<Article> parse = pageParse.parse("000970", "", 1);
//        parse.forEach(e-> System.out.println(e));
    }
}
