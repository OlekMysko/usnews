package com.us.news.aggregationservice.service;

import com.us.news.aggregationservice.model.AggregationResultDto;
import com.us.news.common.model.CreatedLocationDto;
import com.us.news.common.model.ResponseWrapper;
import java.util.List;
import java.util.UUID;

public interface AggregationService {
    ResponseWrapper<AggregationResultDto> getLocationWithNews(UUID locationId);
    ResponseWrapper<List<CreatedLocationDto>> getAllLocations();
    ResponseWrapper<CreatedLocationDto> addLocation(String locationName);
    ResponseWrapper<AggregationResultDto> getLocationWithNewsByCoordinates(double lat, double lon);
}