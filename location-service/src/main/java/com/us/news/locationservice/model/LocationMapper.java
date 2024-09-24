package com.us.news.locationservice.model;

import com.us.news.common.model.CreatedLocationDto;
import com.us.news.common.model.CreateLocationDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LocationMapper {

    public LocationEntity mapTo(CreateLocationDto createLocationDto) {
        return LocationEntity.builder()
                .locationName(createLocationDto.getLocationName())
                .locationLatitude(createLocationDto.getLocationLatitude())
                .locationLongitude(createLocationDto.getLocationLongitude())
                .build();
    }

    public CreatedLocationDto mapTo(LocationEntity locationEntity) {
        return CreatedLocationDto.builder()
                .locationId(locationEntity.getLocationId())
                .locationName(locationEntity.getLocationName())
                .locationLatitude(locationEntity.getLocationLatitude())
                .locationLongitude(locationEntity.getLocationLongitude())
                .build();
    }

}
