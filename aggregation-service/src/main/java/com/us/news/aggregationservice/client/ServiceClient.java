package com.us.news.aggregationservice.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
public interface ServiceClient {
    <T> T fetchData(String url, ParameterizedTypeReference<T> responseType);
    <T, R> T fetchData(String url, ParameterizedTypeReference<T> responseType, R requestData);
}
