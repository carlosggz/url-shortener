package org.example.urlshortener.infrastructure.components;

import lombok.val;
import org.example.urlshortener.infrastructure.persistence.entities.SequenceEntity;
import org.example.urlshortener.infrastructure.persistence.repositories.SequencesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.locks.Lock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleSeedGeneratorTest {

    @Mock
    private SequencesRepository sequencesRepository;

    @Mock
    private Lock sequenceLock;

    @InjectMocks
    private SimpleSeedGenerator simpleSeedGenerator;

    @Captor
    private ArgumentCaptor<SequenceEntity> sequenceEntityCaptor;

    @Test
    void whenNoSequenceExists_thenGenerateSeedCreatesNewSequence() {
        //given
        when(sequencesRepository.findById(SimpleSeedGenerator.DEFAULT_SEQUENCE_NAME))
                .thenReturn(Optional.empty());

        //when
        val seed = simpleSeedGenerator.generateSeed();

        //then
        verifySequence(1L, seed);
    }

    @Test
    void whenSequenceExists_thenSeedIsUpdated() {
        //given
        when(sequencesRepository.findById(SimpleSeedGenerator.DEFAULT_SEQUENCE_NAME))
                .thenReturn(Optional.of(SequenceEntity.builder()
                        .name(SimpleSeedGenerator.DEFAULT_SEQUENCE_NAME)
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
        assertEquals(SimpleSeedGenerator.DEFAULT_SEQUENCE_NAME, savedSequence.getName());
        assertEquals(sequence, savedSequence.getSequenceValue());

        assertNotNull(seed);
        assertTrue(seed.matches("\\d{17}" + sequence));
    }
}