package org.example.urlshortener.infrastructure.components;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.example.urlshortener.domain.SeedGenerator;
import org.example.urlshortener.infrastructure.persistence.entities.SequenceEntity;
import org.example.urlshortener.infrastructure.persistence.repositories.SequencesRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Lock;

@Component
@RequiredArgsConstructor
public class SimpleSeedGenerator implements SeedGenerator {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    public static final String DEFAULT_SEQUENCE_NAME = "default";

    private final SequencesRepository sequencesRepository;
    private final Lock sequenceLock;

    @Override
    public String generateSeed() {
        sequenceLock.lock();

        try {
            val sequence = sequencesRepository
                    .findById(DEFAULT_SEQUENCE_NAME)
                    .orElseGet(() -> SequenceEntity.builder()
                            .name(DEFAULT_SEQUENCE_NAME)
                            .sequenceValue(0L)
                            .build());

            sequence.setSequenceValue(sequence.getSequenceValue() + 1);
            sequencesRepository.save(sequence);

            return DATE_TIME_FORMATTER.format(LocalDateTime.now()) + sequence.getSequenceValue();
        } finally {
            sequenceLock.unlock();
        }
    }
}
