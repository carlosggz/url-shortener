package org.example.urlshortener.infrastructure.persistence.repositories;

import org.example.urlshortener.infrastructure.persistence.entities.SequenceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SequencesRepository extends CrudRepository<SequenceEntity, String> {
}
