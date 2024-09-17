package com.us.news.locationservice.repository;

import com.us.news.locationservice.model.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface JpaLocationRepository extends JpaRepository<LocationEntity, UUID> {
    LocationEntity findByLocationName(String locationName);
}