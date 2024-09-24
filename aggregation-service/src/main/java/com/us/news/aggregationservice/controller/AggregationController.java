package com.us.news.aggregationservice.controller;

import com.us.news.aggregationservice.model.AggregationResultDto;
import com.us.news.aggregationservice.service.AggregationService;
import com.us.news.common.model.CreatedLocationDto;
import com.us.news.common.model.ResponseWrapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.us.news.common.config.BaseAggregationUrls.*;

@RestController
@RequestMapping(AGGREGATION_CONTROLLER_PREFIX)
@AllArgsConstructor
public class AggregationController {
    private final AggregationService aggregationService;

    @GetMapping(LOCATION_BY_ID)
    public ResponseEntity<ResponseWrapper<AggregationResultDto>> getLocationWithNews(@PathVariable UUID locationId) {
        ResponseWrapper<AggregationResultDto> response = aggregationService.getLocationWithNews(locationId);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping(LOCATIONS)
    public ResponseEntity<ResponseWrapper<List<CreatedLocationDto>>> getAllLocations() {
        ResponseWrapper<List<CreatedLocationDto>> response = aggregationService.getAllLocations();
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(response, status);
    }

    @PostMapping(LOCATION_NAME)
    public ResponseEntity<ResponseWrapper<CreatedLocationDto>> addLocation(@PathVariable String locationName) {
        ResponseWrapper<CreatedLocationDto> response = aggregationService.addLocation(locationName);
        HttpStatus status = response.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping(LOCATION_BY_COORDINATES)
    public ResponseEntity<ResponseWrapper<AggregationResultDto>> getLocationByCoordinates(@RequestParam double lat, @RequestParam double lon) {
        ResponseWrapper<AggregationResultDto> response = aggregationService.getLocationWithNewsByCoordinates(lat, lon);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }
}
