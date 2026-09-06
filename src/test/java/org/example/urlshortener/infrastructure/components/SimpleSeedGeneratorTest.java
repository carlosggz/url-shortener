package org.example.urlshortener.infrastructure.components;

import lombok.val;
import org.example.urlshortener.infrastructure.persistence.entities.SequenceEntity;
import org.example.urlshortener.infrastructure.persistence.repositories.SequencesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.locks.Lock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleSeedGeneratorTest {

    private static final String ZONE = "001";
    
    @Mock
    private SequencesRepository sequencesRepository;

    @Mock
    private Lock sequenceLock;

    @Captor
    private ArgumentCaptor<SequenceEntity> sequenceEntityCaptor;
    
    private SimpleSeedGenerator simpleSeedGenerator;

    @BeforeEach
    void setGenerator() {
        simpleSeedGenerator = new SimpleSeedGenerator(sequencesRepository, sequenceLock, ZONE);
    }

    @Test
    void whenNoSequenceExists_thenGenerateSeedCreatesNewSequence() {
        //given
        when(sequencesRepository.findById(ZONE))
                .thenReturn(Optional.empty());

        //when
        val seed = simpleSeedGenerator.generateSeed();

        //then
        verifySequence(1L, seed);
    }

    @Test
    void whenSequenceExists_thenSeedIsUpdated() {
        //given
        when(sequencesRepository.findById(ZONE))
                .thenReturn(Optional.of(SequenceEntity.builder()
                        .zone(ZONE)
                        .sequenceValue(123L)
                        .build()));

        //when
        val seed = simpleSeedGenerator.generateSeed();

        //then
        verifySequence(124L, seed);
    }

    private void verifySequence(long sequence, String seed) {
        verify(sequenceLock).lock();
        verify(sequenceLock).unlock();

        verify(sequencesRepository).save(sequenceEntityCaptor.capture());
        val savedSequence = sequenceEntityCaptor.getValue();
        assertNotNull(savedSequence);
        assertEquals(ZONE, savedSequence.getZone());
        assertEquals(sequence, savedSequence.getSequenceValue());

        assertNotNull(seed);
        assertTrue(seed.matches("\\d{17}" + sequence));
    }
}