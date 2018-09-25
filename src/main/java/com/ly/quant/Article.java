package com.ly.quant;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class Article {

    private Integer readCount;

    private String title;

    private String  date;

    private String code ;

    private String name;

    private String cmtURL;

    private String author;


    private String updateDate;

    private String createDate;

    private String content;

    private String createTime;

    private String media;
    private List<Comment> comments = new ArrayList<>();

    public Article() {
    }

    public Article(Integer readCount, String title, String date, String code, String name) {
        this.readCount = readCount;
        this.title = title;
        this.date = date;
        this.code = code;
        this.name = name;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }
}
