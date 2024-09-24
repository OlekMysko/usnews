package com.us.news.aggregationservice.api_provider.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Coordinates {
    private String locationName;
    private double locationLatitude;
    private double locationLongitude;
}
