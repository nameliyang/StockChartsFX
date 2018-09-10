package com.ly.quant;

import lombok.Data;

@Data
public class Article {
    private Integer readCount;

    private String title;

    private String  date;

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }
}
