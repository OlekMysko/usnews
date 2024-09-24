package com.us.news.aggregationservice.api_provider.model;

import lombok.Data;

@Data
public class OpenWeatherResponse {
    private String name;
    private double lat;
    private double lon;
    private String country;
    private String state;
}