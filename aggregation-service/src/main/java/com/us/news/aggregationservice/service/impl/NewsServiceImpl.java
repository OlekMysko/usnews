package com.us.news.aggregationservice.service.impl;

import com.us.news.aggregationservice.client.ServiceClient;
import com.us.news.aggregationservice.config.ServiceConfig;
import com.us.news.common.model.CreatedNewsDto;
import com.us.news.common.model.ResponseWrapper;
import com.us.news.aggregationservice.service.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final ServiceClient serviceClient;
    private final ServiceConfig serviceConfig;

    @Override
    public List<CreatedNewsDto> fetchNewsByLocationId(UUID locationId) {
        return serviceClient.fetchData(serviceConfig.getNewsService().getNewsByLocationId() + locationId,
                new ParameterizedTypeReference<ResponseWrapper<List<CreatedNewsDto>>>() {}).getData();
    }
}