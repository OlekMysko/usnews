package com.us.news.newsservice.model.api_response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class Article {
    private Source source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private Date publishedAt;
    private String content;
}

