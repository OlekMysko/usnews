package com.us.news.aggregationservice.config;

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
    private NewsService newsService;

    @Setter
    @Getter
    public static class LocationService {
        private String locationsUrl;
        private String locationDetailsById;
    }

    @Setter
    @Getter
    public static class NewsService {
        private String newsByLocationId;
    }
}
