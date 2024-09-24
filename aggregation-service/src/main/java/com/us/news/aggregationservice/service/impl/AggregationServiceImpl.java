package com.us.news.aggregationservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.us.news.aggregationservice.api_provider.LocationDetailsProvider;
import com.us.news.aggregationservice.api_provider.model.Coordinates;
import com.us.news.aggregationservice.ex.LocationNotFoundException;
import com.us.news.aggregationservice.model.AggregationResultDto;
import com.us.news.aggregationservice.service.AggregationService;
import com.us.news.aggregationservice.service.LocationService;
import com.us.news.aggregationservice.service.NewsService;
import com.us.news.aggregationservice.util.ThrowingSupplier;
import com.us.news.common.model.CreatedLocationDto;
import com.us.news.common.model.CreatedNewsDto;
import com.us.news.common.model.ResponseWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AggregationServiceImpl implements AggregationService {
    private final LocationService locationService;
    private final NewsService newsService;
    private final LocationDetailsProvider locationDetailsProvider;

    @Override
    public ResponseWrapper<AggregationResultDto> getLocationWithNews(final UUID locationId) {
        try {
            CreatedLocationDto location = locationService.fetchLocationDetails(locationId);
            List<CreatedNewsDto> newsList = newsService.fetchNewsByLocationId(locationId);
            AggregationResultDto aggregationResult = buildAggregationResult(location, newsList);
            return createSuccessResponse(aggregationResult);
        } catch (NullPointerException e) {
            return createErrorResponse("Null value encountered: " + e.getMessage(), null, AggregationResultDto.class);
        } catch (RestClientException e) {
            return createErrorResponse("Error during service call: " + e.getMessage(), null, AggregationResultDto.class);
        } catch (Exception e) {
            return createErrorResponse("Unexpected error: " + e.getMessage(), null, AggregationResultDto.class);
        }
    }

    @Override
    public ResponseWrapper<List<CreatedLocationDto>> getAllLocations() {
        try {
            List<CreatedLocationDto> locations = locationService.fetchAllLocations();
            return createSuccessResponse(locations);
        } catch (RestClientException e) {
            Type type = createParameterizedType(List.class, CreatedLocationDto.class);
            return createErrorResponse("Error during service call: " + e.getMessage(), null, type);
        } catch (Exception e) {
            Type type = createParameterizedType(List.class, CreatedLocationDto.class);
            return createErrorResponse("Unexpected error: " + e.getMessage(), null, type);
        }
    }

    @Override
    public ResponseWrapper<CreatedLocationDto> addLocation(final String locationName) {
        try {
            Coordinates coordinates = fetchCoordinates(locationName);
            CreatedLocationDto createdLocation = locationService.createLocation(coordinates);
            return createSuccessResponse("Location created successfully.", createdLocation);
        } catch (RestClientResponseException e) {
            Object details = parseResponseDetails(e);
            return createErrorResponse("Error during service call: " + e.getStatusCode().value(), details, CreatedLocationDto.class);
        }
    }

    @Override
    public ResponseWrapper<AggregationResultDto> getLocationWithNewsByCoordinates(final double lat, final double lon) {
        try {
            CreatedLocationDto createdLocation = locationService.fetchLocationByCoordinates(lat, lon);
            List<CreatedNewsDto> newsList = newsService.fetchNewsByLocationId(createdLocation.getLocationId());
            AggregationResultDto aggregationResult = buildAggregationResult(createdLocation, newsList);
            return createSuccessResponse("SUCCESS", aggregationResult);
        } catch (RestClientException e) {
            return createErrorResponse("Error during service call: " + e.getMessage(), null, AggregationResultDto.class);
        } catch (Exception e) {
            return createErrorResponse("Unexpected error: " + e.getMessage(), null, AggregationResultDto.class);
        }
    }

    private Coordinates fetchCoordinates(String locationName) {
        return locationDetailsProvider
                .fetchCoordinates(locationName)
                .orElseThrow(() -> new LocationNotFoundException(locationName));
    }

    private Object parseResponseDetails(RestClientResponseException e) {
        return parseJson(() -> new ObjectMapper().readValue(e.getResponseBodyAsString(), Object.class), e.getResponseBodyAsString());
    }

    private Object parseJson(ThrowingSupplier<Object> parser, String fallback) {
        try {
            return parser.get();
        } catch (Exception e) {
            return fallback;
        }
    }

    private AggregationResultDto buildAggregationResult(final CreatedLocationDto location, final List<CreatedNewsDto> newsList) {
        return AggregationResultDto.builder()
                .location(location)
                .newsList(newsList)
                .build();
    }

    private ResponseWrapper<AggregationResultDto> createSuccessResponse(AggregationResultDto aggregationResult) {
        return new ResponseWrapper<>(true, "SUCCESS", aggregationResult);
    }

    private ResponseWrapper<List<CreatedLocationDto>> createSuccessResponse(List<CreatedLocationDto> data) {
        return new ResponseWrapper<>(true, "SUCCESS", data);
    }

    private ResponseWrapper<CreatedLocationDto> createSuccessResponse(String message, CreatedLocationDto createdLocation) {
        return new ResponseWrapper<>(true, message, createdLocation);
    }

    private ResponseWrapper<AggregationResultDto> createSuccessResponse(String message, AggregationResultDto aggregationResult) {
        return new ResponseWrapper<>(true, message, aggregationResult);
    }

    private <T> ResponseWrapper<T> createErrorResponse(String message, Object details, Type type) {
        return new ResponseWrapper<>(false, message, null, details);
    }

    private ParameterizedType createParameterizedType(final Class<?> raw, final Type... arguments) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return arguments;
            }

            @Override
            public Type getRawType() {
                return raw;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
}