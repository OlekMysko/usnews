package com.us.news.newsservice.api_provider;

import com.us.news.newsservice.config.NewsApiConfig;
import com.us.news.newsservice.model.api_response.Article;
import com.us.news.newsservice.model.api_response.NewsApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class NewsApiProvider implements NewsProvider {
    private final RestTemplate restTemplate;
    private final NewsApiConfig apiConfig;

    @Override
    public List<Article> fetchNews(String locationName) {
        String url = apiConfig.getNewsApiUrl() + "?q=" + locationName.trim().replace(" ", "+") + "&apiKey=" + apiConfig.getNewsApiKey();
        ResponseEntity<NewsApiResponse> response = restTemplate.getForEntity(url, NewsApiResponse.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody()).getArticles();
        }
        return Collections.emptyList();
    }
}
