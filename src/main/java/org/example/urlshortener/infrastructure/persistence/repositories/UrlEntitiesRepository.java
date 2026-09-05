package org.example.urlshortener.infrastructure.persistence.repositories;

import org.example.urlshortener.infrastructure.persistence.entities.UrlEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlEntitiesRepository extends CrudRepository<UrlEntity, String> {

    Optional<UrlEntity> findByOriginalUrl(String originalUrl);
}
