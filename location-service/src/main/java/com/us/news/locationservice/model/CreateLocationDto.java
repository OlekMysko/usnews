package com.us.news.locationservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateLocationDto {
    private String locationName;
    private double locationLatitude, locationLongitude;

    public Optional<String > validateFields() {
        if (locationName == null || locationName.trim().isEmpty()) {
            return Optional.of("Location name is required and cannot be empty.");
        }

        if (locationLatitude < -90.0 || locationLatitude > 90.0) {
            return Optional.of("Location latitude must be between -90 and 90 degrees.");
        }

        if (locationLongitude < -180.0 || locationLongitude > 180.0) {
            return Optional.of("Location longitude must be between -180 and 180 degrees.");
        }

        return Optional.empty();
    }
}
