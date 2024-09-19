package com.us.news.newsservice.model;

import com.us.news.newsservice.model.api_response.Article;
import org.springframework.stereotype.Component;
import com.us.news.common.model.CreatedNewsDto;

import java.util.UUID;

@Component
public class NewsMapper {
    public NewsEntity mapTo(CreateNewsDto createNewsDto) {
        return NewsEntity.builder()
                .title(createNewsDto.getTitle())
                .content(createNewsDto.getContent())
                .locationId(createNewsDto.getLocationId())
                .build();
    }

    public CreatedNewsDto mapTo(NewsEntity newsEntity) {
        return CreatedNewsDto.builder()
                .newsId(newsEntity.getNewsId())
                .title(newsEntity.getTitle())
                .content(newsEntity.getContent())
                .locationId(newsEntity.getLocationId())
                .build();
    }

    public NewsEntity mapTo(Article article, UUID location) {
        return NewsEntity.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .locationId(location)
                .build();
    }
}

