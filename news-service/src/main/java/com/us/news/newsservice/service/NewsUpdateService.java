package com.us.news.newsservice.service;

import com.us.news.common.model.LocationNameIdDto;
import com.us.news.common.model.ResponseWrapper;
import com.us.news.newsservice.api_provider.NewsProvider;
import com.us.news.newsservice.client.ServiceClient;
import com.us.news.newsservice.client.ServiceConfig;
import com.us.news.newsservice.model.NewsMapper;
import com.us.news.newsservice.model.api_response.Article;
import com.us.news.newsservice.repository.NewsRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NewsUpdateService {
    private final ServiceClient serviceClient;
    private final ServiceConfig serviceConfig;
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final NewsProvider newsProvider;

    @Scheduled(fixedRate = 300000)
    public void updateNewsForAllLocations() {

        List<LocationNameIdDto> locations = getAllLocationsNamesList().getData();
        for (LocationNameIdDto location : locations) {
            List<Article> newsList = newsProvider.fetchNews(location.getLocationName());
            saveNewsToDatabase(location.getLocationId(), newsList);
        }
    }

    private void saveNewsToDatabase(UUID locationId, List<Article> newsList) {
        for (Article article : newsList) {
            if (article.getTitle() != null && !article.getTitle().isEmpty() &&
                    article.getContent() != null && !article.getContent().isEmpty()) {
                boolean exists = newsRepository.findByTitleAndLocationId(article.getTitle(), locationId).isPresent();
                if (!exists) {
                    newsRepository.save(newsMapper.mapTo(article, locationId));
                }
            }
        }
    }

    private ResponseWrapper<List<LocationNameIdDto>> getAllLocationsNamesList() {
        try {
            List<LocationNameIdDto> locations = serviceClient.fetchData(
                    serviceConfig.getLocationService().getGetLocationsNamesAndIdsList(),
                    new ParameterizedTypeReference<ResponseWrapper<List<LocationNameIdDto>>>() {
                    }).getData();

            return new ResponseWrapper<>(true, "SUCCESS", locations);
        } catch (RestClientException e) {
            return new ResponseWrapper<>(false, "Error during service call: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseWrapper<>(false, "Unexpected error: " + e.getMessage(), null);
        }
    }
}