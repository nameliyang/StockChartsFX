package com.ly.quant;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class Comment {

    private String date;

    private String content;

    private String author;

    public Comment(String date, String content, String author) {
        this.date = date;
        this.content = content;
        this.author = author;
    }
}
