package com.us.news.aggregationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.us.news.aggregationservice.api_provider.LocationDetailsProvider;
import com.us.news.aggregationservice.api_provider.model.Coordinates;
import com.us.news.aggregationservice.client.ServiceClient;
import com.us.news.aggregationservice.config.ServiceConfig;
import com.us.news.aggregationservice.ex.LocationNotFoundException;
import com.us.news.aggregationservice.model.AggregationResultDto;
import com.us.news.aggregationservice.util.ThrowingSupplier;
import com.us.news.common.model.CreateLocationDto;
import com.us.news.common.model.CreatedLocationDto;
import com.us.news.common.model.CreatedNewsDto;
import com.us.news.common.model.ResponseWrapper;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

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

        } catch (RestClientResponseException e) {
            ObjectMapper objectMapper = new ObjectMapper();
            Object details = parseJson(() -> objectMapper.readValue(e.getResponseBodyAsString(), Object.class), e.getResponseBodyAsString());

            return new ResponseWrapper<>(
                    false,
                    "Error during service call: " + e.getStatusCode().value(),
                    null,
                    details
            );
        }
    }

    private Coordinates fetchCoordinates(String locationName) {
        return locationDetailsProvider
                .fetchCoordinates(locationName)
                .orElseThrow(() -> new LocationNotFoundException(locationName));
    }

    private Object parseJson(ThrowingSupplier<Object> parser, String fallback) {
        try {
            return parser.get();
        } catch (Exception e) {
            return fallback;
        }
    }

    public ResponseWrapper<AggregationResultDto> getLocationWithNewsByCoordinates(final double lat, final double lon) {
        try {
            CreatedLocationDto createdLocation = fetchLocationByCoordinates(lat, lon);
            List<CreatedNewsDto> newsList = fetchNewsByLocationId(createdLocation.getLocationId());
            AggregationResultDto aggregationResult = buildAggregationResult(createdLocation, newsList);
            return new ResponseWrapper<>(true, "SUCCESS", aggregationResult);
        } catch (RestClientException e) {
            return new ResponseWrapper<>(false, "Error during service call: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseWrapper<>(false, "Unexpected error: " + e.getMessage(), null);
        }
    }

    private CreatedLocationDto fetchLocationByCoordinates(final double lat, final double lon) {
        String location = locationDetailsProvider
                .fetchLocationByCoordinates(lat, lon)
                .orElseThrow(() -> new LocationNotFoundException("lat: " + lat + ", lon: " + lon));
        try {
            ResponseWrapper<CreatedLocationDto> responseWrapper = serviceClient.fetchData(
                    serviceConfig.getLocationService().getLocationsUrl() + "/" + location
                    , new ParameterizedTypeReference<>() {
                    });
            if (responseWrapper == null || !responseWrapper.isSuccess()) {
                return addNewLocation(location, lat, lon);
            }
            return responseWrapper.getData();
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                return addNewLocation(location, lat, lon);
            }
            throw new LocationNotFoundException("Request failed with status code: " + e.getStatusCode().value() + " and message: " + e.getMessage());
        }
    }

    private List<CreatedNewsDto> fetchNewsByLocationId(final UUID locationId) {
        return serviceClient.fetchData(serviceConfig.getNewsService().getNewsByLocationId() + locationId,
                new ParameterizedTypeReference<ResponseWrapper<List<CreatedNewsDto>>>() {
                }).getData();
    }

    private AggregationResultDto buildAggregationResult(final CreatedLocationDto location,
                                                        final List<CreatedNewsDto> newsList) {
        return AggregationResultDto.builder()
                .location(location)
                .newsList(newsList)
                .build();
    }

    private CreatedLocationDto addNewLocation(String locationName, double lat, double lon) {
        CreateLocationDto newLocation = new CreateLocationDto(locationName, lat, lon);

        ResponseWrapper<CreatedLocationDto> responseWrapper = serviceClient.fetchData(
                serviceConfig.getLocationService().getLocationsUrl(),
                new ParameterizedTypeReference<>() {
                },
                newLocation
        );

        if (responseWrapper != null && responseWrapper.isSuccess()) {
            return responseWrapper.getData();
        } else {
            throw new RuntimeException("Failed to create location: " + locationName);
        }
    }
}