package com.ly.quant;

import lombok.Data;

@Data
public class Article {

    private Integer readCount;

    private String title;

    private String  date;

    private String code ;

    private String name;

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
