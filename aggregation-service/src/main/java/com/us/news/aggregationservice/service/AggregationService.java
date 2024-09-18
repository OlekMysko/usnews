package com.us.news.aggregationservice.service;

import com.us.news.aggregationservice.config.ServiceConfig;
import com.us.news.aggregationservice.model.AggregationResultDto;
import com.us.news.common.model.CreatedLocationDto;
import com.us.news.common.model.CreatedNewsDto;
import com.us.news.common.model.ResponseWrapper;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AggregationService {
    private final RestTemplate restTemplate;
    private final ServiceConfig serviceConfig;

    public ResponseWrapper<AggregationResultDto> getLocationWithNews(UUID locationId) {
        try {
            CreatedLocationDto location = fetchData(serviceConfig.getLocationServiceUrl() + locationId,
                    new ParameterizedTypeReference<>() {});

            List<CreatedNewsDto> newsList = fetchData( serviceConfig.getNewsServiceUrl() + locationId,
                    new ParameterizedTypeReference<>() {});


            AggregationResultDto aggregationResult = AggregationResultDto.builder()
                    .location(location)
                    .newsList(newsList)
                    .build();

            return new ResponseWrapper<>(true, "SUCCESS", aggregationResult);
        } catch (Exception e) {
            return new ResponseWrapper<>(false, "Error during data aggregation: " + e.getMessage(), null);
        }
    }

    private <T> T fetchData(String url, ParameterizedTypeReference<ResponseWrapper<T>> responseType) {
        ResponseEntity<ResponseWrapper<T>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                responseType
        );
        return Objects.requireNonNull(response.getBody()).getData();
    }
}
