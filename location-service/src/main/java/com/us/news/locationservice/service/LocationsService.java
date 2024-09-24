package com.us.news.locationservice.service;

import com.us.news.common.model.CreateLocationDto;
import com.us.news.common.model.LocationNameIdDto;
import com.us.news.common.model.CreatedLocationDto;
import com.us.news.locationservice.model.LocationEntity;
import com.us.news.locationservice.model.LocationMapper;
import com.us.news.common.model.ResponseWrapper;
import com.us.news.locationservice.repository.LocationRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LocationsService {
    private static final Logger logger = LoggerFactory.getLogger(LocationsService.class);

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public ResponseWrapper<CreatedLocationDto> addNewLocation(final CreateLocationDto createLocationDto) {
        Optional<String> error = createLocationDto.validateFields();
        if (error.isPresent()) {
            logger.error("Failed to create location: {}", error);
            return new ResponseWrapper<>(false, error.get(), null);
        }

        if (locationAlreadyExists(createLocationDto)) {
            logger.error("Failed to create location: Duplicate location name - {}", createLocationDto.getLocationName());
            return new ResponseWrapper<>(false, String.format("Location with name %s already exists.", createLocationDto.getLocationName()), null);
        }

        return saveNewLocation(createLocationDto);
    }

    private ResponseWrapper<CreatedLocationDto> saveNewLocation(final CreateLocationDto createLocationDto) {
        LocationEntity entity = locationMapper.mapTo(createLocationDto);
        try {
            LocationEntity savedLocation = locationRepository.save(entity);
            return new ResponseWrapper<>(true, "Location created successfully.", locationMapper.mapTo(savedLocation));
        } catch (Exception e) {
            logger.error("Failed to save location: {}", e.getMessage());
            return new ResponseWrapper<>(false, "Failed to save location.", null);
        }
    }

    private boolean locationAlreadyExists(final CreateLocationDto createLocationDto) {
        return findByLocationName(createLocationDto.getLocationName()).isPresent();
    }

    public Optional<CreatedLocationDto> findByLocationName(final String name) {
        return name == null ? Optional.empty() : locationRepository.findByLocationName(name).map(locationMapper::mapTo);
    }

    public ResponseWrapper<List<CreatedLocationDto>> findAllLocations() {
        List<LocationEntity> locationEntities = locationRepository.findAll().orElse(Collections.emptyList());
        List<CreatedLocationDto> locations = locationEntities.stream().map(locationMapper::mapTo).collect(Collectors.toList());

        if (locations.isEmpty()) {
            return new ResponseWrapper<>(false, "No locations found.", null);
        } else {
            return new ResponseWrapper<>(true, "Locations retrieved successfully.", locations);
        }
    }

    public Optional<CreatedLocationDto> findByLocationId(final UUID id) {
        return id == null ? Optional.empty() : locationRepository.findByLocationId(id).map(locationMapper::mapTo);
    }

    public ResponseWrapper<CreatedLocationDto> deleteLocationById(final UUID id) {
        Optional<LocationEntity> location = locationRepository.findByLocationId(id);
        if (location.isPresent()) {
            CreatedLocationDto deletedLocation = locationMapper.mapTo(location.get());
            locationRepository.deleteByLocationId(id);
            return new ResponseWrapper<>(true, "Location deleted successfully.", deletedLocation);
        } else {
            logger.error("Location with ID {} not found.", id);
            return new ResponseWrapper<>(false, "Location not found.", new CreatedLocationDto(id, "", 0, 0));
        }
    }

    public ResponseWrapper<List<LocationNameIdDto>> findAllLocationNamesAndIds() {
        List<LocationNameIdDto> locationNames = locationRepository.findAllLocationNamesAndIds().orElse(Collections.emptyList());
        if (locationNames.isEmpty()) {
            return new ResponseWrapper<>(false, "No location names found.", null);
        } else {
            return new ResponseWrapper<>(true, "Location names retrieved successfully.", locationNames);
        }
    }
}
