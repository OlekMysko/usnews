package com.us.news.aggregationservice.ex;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(String message) {
        super("Location not found: "+ message);
    }
}