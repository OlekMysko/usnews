package com.us.news.newsservice.model.api_response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class NewsApiResponse {
    private String status;
    private int totalResults;
    private List<Article> articles;
}

