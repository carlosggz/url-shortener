package org.example.urlshortener.utils;

import org.example.urlshortener.infrastructure.persistence.repositories.SequencesRepository;
import org.example.urlshortener.infrastructure.persistence.repositories.UrlEntitiesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {

    @Autowired
    protected SequencesRepository sequencesRepository;

    @Autowired
    protected UrlEntitiesRepository urlEntitiesRepository;

    @BeforeEach
    void setUpBaseTest() {
        sequencesRepository.deleteAll();
        urlEntitiesRepository.deleteAll();
    }
}
