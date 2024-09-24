package com.us.news.aggregationservice.service.impl;

import com.us.news.aggregationservice.api_provider.LocationDetailsProvider;
import com.us.news.aggregationservice.api_provider.model.Coordinates;
import com.us.news.aggregationservice.client.ServiceClient;
import com.us.news.aggregationservice.config.ServiceConfig;
import com.us.news.aggregationservice.ex.LocationNotFoundException;
import com.us.news.common.model.CreatedLocationDto;
import com.us.news.common.model.CreateLocationDto;
import com.us.news.common.model.ResponseWrapper;
import com.us.news.aggregationservice.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final ServiceClient serviceClient;
    private final ServiceConfig serviceConfig;
    private final LocationDetailsProvider locationDetailsProvider;

    @Override
    public CreatedLocationDto fetchLocationDetails(UUID locationId) {
        return serviceClient.fetchData(serviceConfig.getLocationService().getLocationDetailsById() + locationId,
                new ParameterizedTypeReference<ResponseWrapper<CreatedLocationDto>>() {}).getData();
    }

    @Override
    public List<CreatedLocationDto> fetchAllLocations() {
        return serviceClient.fetchData(serviceConfig.getLocationService().getLocationsUrl(),
                new ParameterizedTypeReference<ResponseWrapper<List<CreatedLocationDto>>>() {}).getData();
    }

    @Override
    public CreatedLocationDto createLocation(Coordinates coordinates) {
        return serviceClient.fetchData(serviceConfig.getLocationService().getLocationsUrl(),
                new ParameterizedTypeReference<ResponseWrapper<CreatedLocationDto>>() {},
                new CreateLocationDto(coordinates.getLocationName(), coordinates.getLocationLatitude(), coordinates.getLocationLongitude())).getData();
    }

    @Override
    public CreatedLocationDto fetchLocationByCoordinates(double lat, double lon) {
        String location = locationDetailsProvider.fetchLocationByCoordinates(lat, lon)
                .orElseThrow(() -> new LocationNotFoundException("lat: " + lat + ", lon: " + lon));
        ResponseWrapper<CreatedLocationDto> responseWrapper = serviceClient.fetchData(serviceConfig.getLocationService().getLocationsUrl() + "/" + location,
                new ParameterizedTypeReference<>() {});
        if (responseWrapper == null || !responseWrapper.isSuccess()) {
            return addNewLocation(location, lat, lon);
        }
        return responseWrapper.getData();
    }

    private CreatedLocationDto addNewLocation(String locationName, double lat, double lon) {
        CreateLocationDto newLocation = new CreateLocationDto(locationName, lat, lon);
        ResponseWrapper<CreatedLocationDto> responseWrapper = serviceClient.fetchData(serviceConfig.getLocationService().getLocationsUrl(),
                new ParameterizedTypeReference<ResponseWrapper<CreatedLocationDto>>() {}, newLocation);
        if (responseWrapper != null && responseWrapper.isSuccess()) {
            return responseWrapper.getData();
        } else {
            throw new RuntimeException("Failed to create location: " + locationName);
        }
    }
}