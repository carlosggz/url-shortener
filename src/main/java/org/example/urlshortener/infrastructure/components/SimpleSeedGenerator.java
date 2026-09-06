package org.example.urlshortener.infrastructure.components;

import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.example.urlshortener.domain.SeedGenerator;
import org.example.urlshortener.infrastructure.persistence.entities.SequenceEntity;
import org.example.urlshortener.infrastructure.persistence.repositories.SequencesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Lock;

@Component
public class SimpleSeedGenerator implements SeedGenerator {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final SequencesRepository sequencesRepository;
    private final Lock sequenceLock;
    private final String zone;

    public SimpleSeedGenerator(
            SequencesRepository sequencesRepository,
            Lock sequenceLock,
            @Value("${app.zone}") String zone) {
        Assert.isTrue(StringUtils.isNumeric(zone), "Zone must a numerical string with 3 digits");
        this.sequencesRepository = sequencesRepository;
        this.sequenceLock = sequenceLock;
        this.zone = zone;
    }

    @Override
    public String generateSeed() {
        sequenceLock.lock();

        try {
            val sequence = sequencesRepository
                    .findById(zone)
                    .orElseGet(() -> SequenceEntity.builder()
                            .zone(zone)
                            .sequenceValue(0L)
                            .build());

            sequence.setSequenceValue(sequence.getSequenceValue() + 1);
            sequencesRepository.save(sequence);

            return DATE_TIME_FORMATTER.format(LocalDateTime.now()) +
                    this.zone +
                    sequence.getSequenceValue();
        } finally {
            sequenceLock.unlock();
        }
    }
}
