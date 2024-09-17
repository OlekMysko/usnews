package com.us.news.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreatedLocationDto {
    private UUID locationId;
    private String locationName;
    private double locationLatitude, locationLongitude;
}
