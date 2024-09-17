package com.us.news.locationservice.repository;


import com.us.news.locationservice.model.LocationEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationRepository {
    Optional<LocationEntity> findByLocationName(String name);

    LocationEntity save(LocationEntity locationEntity);

    void deleteByLocationId(UUID locationId);

    Optional<LocationEntity> findByLocationId(UUID locationId);

    Optional<List<LocationEntity>> findAll();
}