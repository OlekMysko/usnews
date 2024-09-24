package com.us.news.aggregationservice.service;

import com.us.news.aggregationservice.api_provider.LocationDetailsProvider;
import com.us.news.aggregationservice.api_provider.model.Coordinates;
import com.us.news.aggregationservice.client.ServiceClient;
import com.us.news.aggregationservice.config.ServiceConfig;
import com.us.news.aggregationservice.model.AggregationResultDto;
import com.us.news.common.model.CreateLocationDto;
import com.us.news.common.model.CreatedLocationDto;
import com.us.news.common.model.CreatedNewsDto;
import com.us.news.common.model.ResponseWrapper;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AggregationService {
    private final ServiceClient serviceClient;
    private final ServiceConfig serviceConfig;
    private final LocationDetailsProvider locationDetailsProvider;

    public ResponseWrapper<AggregationResultDto> getLocationWithNews(final UUID locationId) {
        try {
            CreatedLocationDto location = serviceClient.fetchData(serviceConfig.getLocationService().getLocationDetailsById() + locationId,
                    new ParameterizedTypeReference<ResponseWrapper<CreatedLocationDto>>() {
                    }).getData();

            List<CreatedNewsDto> newsList = serviceClient.fetchData(serviceConfig.getNewsService().getNewsByLocationId() + locationId,
                    new ParameterizedTypeReference<ResponseWrapper<List<CreatedNewsDto>>>() {
                    }).getData();

            AggregationResultDto aggregationResult = AggregationResultDto.builder()
                    .location(location)
                    .newsList(newsList)
                    .build();

            return new ResponseWrapper<>(true, "SUCCESS", aggregationResult);
        } catch (NullPointerException e) {
            return new ResponseWrapper<>(false, "Null value encountered: " + e.getMessage(), null);
        } catch (RestClientException e) {
            return new ResponseWrapper<>(false, "Error during service call: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseWrapper<>(false, "Unexpected error: " + e.getMessage(), null);
        }
    }

    public ResponseWrapper<List<CreatedLocationDto>> getAllLocations() {
        try {
            List<CreatedLocationDto> locations = serviceClient.fetchData(
                    serviceConfig.getLocationService().getLocationsUrl(),
                    new ParameterizedTypeReference<ResponseWrapper<List<CreatedLocationDto>>>() {
                    }).getData();

            return new ResponseWrapper<>(true, "SUCCESS", locations);
        } catch (RestClientException e) {
            return new ResponseWrapper<>(false, "Error during service call: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseWrapper<>(false, "Unexpected error: " + e.getMessage(), null);
        }
    }

    public ResponseWrapper<CreatedLocationDto> addLocation(final String locationName) {
        try {
            Coordinates coordinates = fetchCoordinates(locationName);
            CreatedLocationDto createdLocation = serviceClient.fetchData(
                    serviceConfig.getLocationService().getLocationsUrl(),
                    new ParameterizedTypeReference<ResponseWrapper<CreatedLocationDto>>() {
                    },
                    new CreateLocationDto(
                            coordinates.getLocationName(),
                            coordinates.getLocationLatitude(),
                            coordinates.getLocationLongitude())
            ).getData();

            return new ResponseWrapper<>(true, "Location created successfully.", createdLocation);
        } catch (RestClientException e) {
            return new ResponseWrapper<>(false, "Error during service call: " + e.getMessage(), null);
        }
    }

    private Coordinates fetchCoordinates(String locationName) {
        return locationDetailsProvider.fetchCoordinates(locationName).orElseThrow(() -> new RuntimeException("Location not found."));
    }
}