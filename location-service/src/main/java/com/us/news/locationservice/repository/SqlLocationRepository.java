package com.us.news.locationservice.repository;


import com.us.news.locationservice.model.LocationEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class SqlLocationRepository implements LocationRepository {

    private final JpaLocationRepository repository;

    @Override
    public Optional<LocationEntity> findByLocationName(String locationName) {
        return Optional.ofNullable(repository.findByLocationName(locationName));
    }

    @Override
    public LocationEntity save(LocationEntity locationEntity) {
        return repository.save(locationEntity);
    }

    @Override
    public void deleteByLocationId(UUID locationId) {
        repository.deleteById(locationId);
    }

    @Override
    public Optional<LocationEntity> findByLocationId(UUID locationId) {
        return repository.findById(locationId);
    }

    @Override
    public Optional<List<LocationEntity>> findAll() {
        return Optional.of(repository.findAll());
    }
}