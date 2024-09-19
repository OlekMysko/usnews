package com.us.news.newsservice.api_provider;

import com.us.news.newsservice.model.api_response.Article;

import java.util.List;

public interface NewsProvider {
    List<Article> fetchNews(String locationName);
}
