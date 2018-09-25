package com.zoicapital.stockchartsfx.active;

import com.google.common.collect.Lists;
import com.ly.quant.Article;
import com.ly.quant.Comment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PageParse {

    private static final List<String> tag = Stream.of("新闻", "讨论", "悬赏", "新闻","问董秘","公告").collect(Collectors.toList());

    private static final String BASE_URL = "http://guba.eastmoney.com";

    public List<Article> parse(String code ,String name, int page) throws IOException {
        List<Article> articles = Lists.newArrayList();
        if(!(page >0)){
            return articles;
        }
        Document doc = Jsoup.connect("http://guba.eastmoney.com/list,"+code+",f_"+page+".html").get();
        Element articlelistnew = doc.getElementById("articlelistnew");
        Elements articleh = articlelistnew.getElementsByClass("articleh");
        String link = null;
        for (Element element : articleh) {
            if ("ad_topic".equals(element.attr("id"))) {
                continue;
            }
            Element el3 = element.getElementsByClass("l3").get(0);
            Elements anEnum = el3.getElementsByTag("em");
            String title = null;
            if(anEnum!=null&&anEnum.size()>0){
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
            Article article = new Article(Integer.parseInt(readCount),title,null,code,name);
            article.setCmtURL(BASE_URL+link);
            article.setAuthor(author);
            article.setUpdateDate(updateDate);
            articles.add(article);
            parseCommnets(commentsCount, article);
        }
        return  articles;
    }



    private void parseCommnets( String commentsCount, Article article) throws IOException {
        Document doc = Jsoup.connect(article.getCmtURL()).get();
        Elements zwfbtime = doc.getElementsByClass("zwfbtime");
        if(zwfbtime==null||zwfbtime.size()==0){
            return ;
        }
        String createText  =zwfbtime.get(0).text();
        createText = createText.replaceAll("发表于","").trim();
        int index = createText.lastIndexOf(" ");
        String time = createText.substring(0,index);
        String media =  createText.substring(index+1,createText.length());
        String content = doc.select("div[id=zwconbody]").text();
        article.setCreateTime(time.trim());
        article.setMedia(media.trim());
        article.setContent(content.trim());
        Elements comments = doc.getElementsByClass("zwli clearfix");
        for(Element element:comments){
            String name = element.select(".zwlitxt > .zwlianame > .zwnick").text();
            String date = element.select(".zwlitxt > .zwlitime").text().replaceAll("发表于","").trim();
            String comment = element.select(".zwlitxt > .zwlitext").text();
            article.getComments().add(new Comment(date,comment,name));
        }
    }

    public static void main(String[] args) throws IOException {
        PageParse pageParse = new PageParse();
        Article article = new Article();
        article.setCode("");
        article.setCmtURL("http://guba.eastmoney.com/news,000970,781826314.html");
        pageParse.parseCommnets("",article);
//        PageParse pageParse = necmd
//
//
//
// geParse();
//        List<Article> parse = pageParse.parse("000970", "", 1);
//        parse.forEach(e-> System.out.println(e));
    }
}
