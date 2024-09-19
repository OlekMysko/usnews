package com.us.news.newsservice.repository;

import com.us.news.newsservice.model.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaNewsRepository extends JpaRepository<NewsEntity, UUID> {
    List<NewsEntity> findByLocationId(UUID locationId);
    NewsEntity findByTitleAndLocationId(String title, UUID locationId);
}
