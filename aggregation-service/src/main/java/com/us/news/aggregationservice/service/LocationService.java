package com.us.news.aggregationservice.service;

import com.us.news.aggregationservice.api_provider.model.Coordinates;
import com.us.news.common.model.CreatedLocationDto;
import java.util.List;
import java.util.UUID;

public interface LocationService {
    CreatedLocationDto fetchLocationDetails(UUID locationId);
    List<CreatedLocationDto> fetchAllLocations();
    CreatedLocationDto createLocation(Coordinates coordinates);
    CreatedLocationDto fetchLocationByCoordinates(double lat, double lon);
}