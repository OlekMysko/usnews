package com.us.news.newsservice.repository;

import com.us.news.newsservice.model.NewsEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NewsRepository {
    Optional<NewsEntity> findByNewsId(UUID newsId);
    NewsEntity save(NewsEntity newsEntity);
    Optional<List<NewsEntity>> findByLocationId(UUID locationId);
    Optional<List<NewsEntity>> findAll();
    Optional<NewsEntity> findByTitleAndLocationId(String title, UUID locationId);
}
