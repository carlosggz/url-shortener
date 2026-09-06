package org.example.urlshortener.infrastructure.components;

import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.example.urlshortener.domain.SeedGenerator;
import org.example.urlshortener.infrastructure.persistence.entities.SequenceEntity;
import org.example.urlshortener.infrastructure.persistence.repositories.SequencesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Lock;

@Component
public class SimpleSeedGenerator implements SeedGenerator {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final SequencesRepository sequencesRepository;
    private final LockRegistry<Lock> lockRegistry;
    private final String zone;

    public SimpleSeedGenerator(
            SequencesRepository sequencesRepository,
            LockRegistry<Lock> lockRegistry,
            @Value("${app.zone}") String zone) {
        Assert.isTrue(StringUtils.length(zone) == 3 && StringUtils.isNumeric(zone),"Zone must a numerical string with 3 digits");
        this.sequencesRepository = sequencesRepository;
        this.lockRegistry = lockRegistry;
        this.zone = zone;
    }

    @Override
    public String generateSeed() {
        val lock = lockRegistry.obtain(zone);
        lock.lock();

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
            lock.unlock();
        }
    }
}
