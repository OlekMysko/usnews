package com.us.news.aggregationservice.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "open-weather-api")
public class WeatherApiConfig {
    private String apiKey;
    private String apiUrlLocationDetailsByLocationName;
    private String apiUrlLocationDetailsByCoordinates;
}
