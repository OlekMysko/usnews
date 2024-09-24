package com.us.news.aggregationservice.api_provider;

import com.us.news.aggregationservice.api_provider.model.Coordinates;
import com.us.news.aggregationservice.api_provider.model.OpenWeatherResponse;
import com.us.news.aggregationservice.config.WeatherApiConfig;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LocationDetailsProviderImpl implements LocationDetailsProvider {

    private final RestTemplate restTemplate;
    private final WeatherApiConfig apiConfig;

    @Override
    public Optional<Coordinates> fetchCoordinates(final String locationName) {
        String url = String.format(apiConfig.getApiUrlLocationDetailsByLocationName(), locationName, apiConfig.getApiKey());
        ResponseEntity<List<OpenWeatherResponse>> response = restTemplate.exchange(url,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        List<OpenWeatherResponse> locations = response.getBody();

        return locations != null && !locations.isEmpty()
                ? locations.stream().findFirst().map(this::convertToCoordinates)
                : Optional.empty();
    }

    private Coordinates convertToCoordinates(final OpenWeatherResponse openWeatherResponse) {
        return new Coordinates(
                openWeatherResponse.getName(),
                openWeatherResponse.getLat(),
                openWeatherResponse.getLon()
        );
    }
    @Override
    public Optional<String> fetchLocationByCoordinates(final double lat, final double lon) {
        String url = String.format(apiConfig.getApiUrlLocationDetailsByCoordinates(), lat, lon, apiConfig.getApiKey());
        ResponseEntity<List<OpenWeatherResponse>> response = restTemplate.exchange(url,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
        });
        List<OpenWeatherResponse> locationData = response.getBody();
        return locationData != null && !locationData.isEmpty()
                ? locationData.stream().findFirst().map(OpenWeatherResponse::getName)
                : Optional.empty();
    }
}
