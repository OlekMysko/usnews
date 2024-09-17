package com.us.news.newsservice.service;

import com.us.news.common.model.CreatedNewsDto;
import com.us.news.newsservice.model.CreateNewsDto;

import com.us.news.newsservice.model.NewsEntity;
import com.us.news.newsservice.model.NewsMapper;
import com.us.news.common.model.ResponseWrapper;
import com.us.news.newsservice.repository.NewsRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NewsService {
    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    public ResponseWrapper<CreatedNewsDto> addNewNews(CreateNewsDto createNewsDto) {
        NewsEntity entity = newsMapper.mapTo(createNewsDto);
        try {
            NewsEntity savedNews = newsRepository.save(entity);
            return new ResponseWrapper<>(true, "News created successfully.", newsMapper.mapTo(savedNews));
        } catch (Exception e) {
            logger.error("Failed to save news: {}", e.getMessage());
            return new ResponseWrapper<>(false, "Failed to save news.", null);
        }
    }

    public Optional<CreatedNewsDto> findByNewsId(UUID newsId) {
        return newsId == null ? Optional.empty() : newsRepository.findByNewsId(newsId).map(newsMapper::mapTo);
    }

    public ResponseWrapper<List<CreatedNewsDto>> findNewsByLocationId(UUID locationId) {
        List<NewsEntity> newsEntities = newsRepository.findByLocationId(locationId).orElse(Collections.emptyList());
        List<CreatedNewsDto> newsList = newsEntities.stream().map(newsMapper::mapTo).collect(Collectors.toList());

        if (newsList.isEmpty()) {
            return new ResponseWrapper<>(false, "No news found for the specified location ID.", null);
        } else {
            return new ResponseWrapper<>(true, "News retrieved successfully.", newsList);
        }
    }

    public ResponseWrapper<List<CreatedNewsDto>> findAllNews() {
        List<NewsEntity> newsEntities = newsRepository.findAll().orElse(Collections.emptyList());
        List<CreatedNewsDto> newsList = newsEntities.stream().map(newsMapper::mapTo).collect(Collectors.toList());

        if (newsList.isEmpty()) {
            return new ResponseWrapper<>(false, "No news found.", null);
        } else {
            return new ResponseWrapper<>(true, "News retrieved successfully.", newsList);
        }
    }
}
