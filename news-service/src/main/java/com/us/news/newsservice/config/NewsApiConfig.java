package com.us.news.newsservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "news-api")
public class NewsApiConfig {
    private String newsApiKey;
    private String newsApiUrl;
}
