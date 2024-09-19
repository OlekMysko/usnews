package com.us.news.newsservice.repository;

import com.us.news.newsservice.model.NewsEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class SqlNewsRepository implements NewsRepository {
    private final JpaNewsRepository repository;

    @Override
    public Optional<NewsEntity> findByNewsId(UUID newsId) {
        return repository.findById(newsId);
    }

    @Override
    public NewsEntity save(NewsEntity newsEntity) {
        return repository.save(newsEntity);
    }

    @Override
    public Optional<List<NewsEntity>> findByLocationId(UUID locationId) {
        return Optional.of(repository.findByLocationId(locationId));
    }

    @Override
    public Optional<List<NewsEntity>> findAll() {
        return Optional.of(repository.findAll());
    }

    @Override
    public Optional<NewsEntity> findByTitleAndLocationId(String title, UUID locationId) {
        return Optional.ofNullable(repository.findByTitleAndLocationId(title, locationId));
    }
}
