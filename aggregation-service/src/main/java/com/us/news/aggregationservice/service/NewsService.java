package com.us.news.aggregationservice.service;

import com.us.news.common.model.CreatedNewsDto;
import java.util.List;
import java.util.UUID;

public interface NewsService {
    List<CreatedNewsDto> fetchNewsByLocationId(UUID locationId);
}