package com.us.news.newsservice.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "services")
public class ServiceConfig {
    private LocationService locationService;

    @Setter
    @Getter
    public static class LocationService {
        private String getLocationsNamesAndIdsList;
    }
}
