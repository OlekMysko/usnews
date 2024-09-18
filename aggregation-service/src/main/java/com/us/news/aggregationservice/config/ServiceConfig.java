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
    private String locationServiceUrl;
    private String newsServiceUrl;
}
