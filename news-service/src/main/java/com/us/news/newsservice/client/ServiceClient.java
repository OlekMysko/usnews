package com.us.news.newsservice.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
public interface ServiceClient {
    <T> T fetchData(String url, ParameterizedTypeReference<T> responseType);
}
