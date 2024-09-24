package com.us.news.aggregationservice.client;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@AllArgsConstructor
public class ServiceClientImpl implements ServiceClient {
    private final RestTemplate restTemplate;

    @Override
    public <T> T fetchData(final String url, final ParameterizedTypeReference<T> responseType) {
        ResponseEntity<T> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                responseType
        );
        return response.getBody();
    }

    @Override
    public <T, R> T fetchData(final String url, final ParameterizedTypeReference<T> responseType, final R requestData) {
        HttpEntity<R> requestEntity = new HttpEntity<>(requestData);
        ResponseEntity<T> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                responseType
        );
        return response.getBody();
    }
}
