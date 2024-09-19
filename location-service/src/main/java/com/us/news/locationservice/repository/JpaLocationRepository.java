package com.us.news.locationservice.repository;

import com.us.news.common.model.LocationNameIdDto;
import com.us.news.locationservice.model.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface JpaLocationRepository extends JpaRepository<LocationEntity, UUID> {
    LocationEntity findByLocationName(String locationName);
    @Query("SELECT new com.us.news.common.model.LocationNameIdDto(l.locationId, l.locationName) FROM LocationEntity l")
    List<LocationNameIdDto> findAllLocationNamesAndIds();
}