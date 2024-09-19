package com.us.news.locationservice.controller;

import com.us.news.common.model.CreatedLocationDto;
import com.us.news.common.model.LocationNameIdDto;
import com.us.news.locationservice.model.CreateLocationDto;
import com.us.news.common.model.ResponseWrapper;
import com.us.news.locationservice.service.LocationsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.us.news.common.config.BaseLocationsUrls.*;


@RestController
@RequestMapping(LOCATIONS_CONTROLLER_PREFIX)
@AllArgsConstructor
public class LocationController {
    private final LocationsService locationsService;
    private static final String LOCATION_FOUND = "Location found.";
    private static final String LOCATION_NOT_FOUND = "Location found.";
    private static final String LOCATION_DELETED = "Location deleted successfully.";

    @PostMapping
    public ResponseEntity<ResponseWrapper<CreatedLocationDto>> addLocation(@RequestBody CreateLocationDto createLocationDto) {
        ResponseWrapper<CreatedLocationDto> response = locationsService.addNewLocation(createLocationDto);
        HttpStatus status = response.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<CreatedLocationDto>>> getAllLocations() {
        ResponseWrapper<List<CreatedLocationDto>> response = locationsService.findAllLocations();
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping(LOCATION_NAME)
    public ResponseEntity<ResponseWrapper<CreatedLocationDto>> getLocationByName(@PathVariable String name) {
        Optional<CreatedLocationDto> location = locationsService.findByLocationName(name);
        return location.map(createdLocationDto -> new ResponseEntity<>(
                new ResponseWrapper<>(true, LOCATION_FOUND, createdLocationDto), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new ResponseWrapper<>(false, LOCATION_NOT_FOUND, null), HttpStatus.NOT_FOUND));
    }

    @GetMapping(LOCATION_ID)
    public ResponseEntity<ResponseWrapper<CreatedLocationDto>> getLocationById(@PathVariable UUID id) {
        Optional<CreatedLocationDto> location = locationsService.findByLocationId(id);
        return location.map(createdLocationDto -> new ResponseEntity<>(
                        new ResponseWrapper<>(true, LOCATION_FOUND, createdLocationDto), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new ResponseWrapper<>(false, LOCATION_NOT_FOUND, null), HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(ID_PATH_VARIABLE)
    public ResponseEntity<ResponseWrapper<CreatedLocationDto>> deleteLocation(@PathVariable UUID id) {
        ResponseWrapper<CreatedLocationDto> response = locationsService.deleteLocationById(id);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping(LOCATIONS_NAMES_AND_IDS)
    public ResponseEntity<ResponseWrapper<List<LocationNameIdDto>>> getAllLocationNames() {
        ResponseWrapper<List<LocationNameIdDto>> response = locationsService.findAllLocationNamesAndIds();
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(response, status);
    }
}
