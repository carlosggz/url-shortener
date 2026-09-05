package org.example.urlshortener.infrastructure.components;

import lombok.val;
import org.example.urlshortener.infrastructure.config.AppConfiguration;
import org.example.urlshortener.infrastructure.persistence.entities.UrlEntity;
import org.example.urlshortener.infrastructure.persistence.repositories.UrlEntitiesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(AppConfiguration.class)
class UrlStoreImplTest {

    @Autowired
    private UrlEntitiesRepository urlEntitiesRepository;

    private UrlStoreImpl urlStore;

    @BeforeEach
    void setUp() {
        urlEntitiesRepository.deleteAll();
        urlStore = new UrlStoreImpl(urlEntitiesRepository);
    }

    @Test
    void whenNoOriginalUrlExists_thenGetShortUrlReturnsEmpty() {
        //given
        val originalUrl = "http://example.com";

        //when
        val result = urlStore.getShortUrl(originalUrl);

        //then
        assertTrue(result.isEmpty());
    }

    @Test
    void whenOriginalUrlExists_thenGetShortUrlReturnsShortenedUrl() {
        //given
        val originalUrl = "http://example.com";
        val shortenedUrl = "abc123";
        urlEntitiesRepository.save(UrlEntity.builder()
                        .shortenedUrl(shortenedUrl)
                        .originalUrl(originalUrl)
                .build());

        //when
        val result = urlStore.getShortUrl(originalUrl);

        //then
        assertTrue(result.isPresent());
        assertEquals(shortenedUrl, result.get());
    }

    @Test
    void whenNoShortenerUrlExists_thenGetOriginalUrlReturnsEmpty() {
        //given
        val shortenedUrl = "abc123";

        //when
        val result = urlStore.getOriginalUrl(shortenedUrl);

        //then
        assertTrue(result.isEmpty());
    }

    @Test
    void whenShortenerUrlExists_thenGetOriginalUrlReturnsOriginalUrl() {
        //given
        val originalUrl = "http://example.com";
        val shortenedUrl = "abc123";
        urlEntitiesRepository.save(UrlEntity.builder()
                .shortenedUrl(shortenedUrl)
                .originalUrl(originalUrl)
                .build());

        //when
        val result = urlStore.getOriginalUrl(shortenedUrl);

        //then
        assertTrue(result.isPresent());
        assertEquals(originalUrl, result.get());
    }

    @Test
    void addUrlMappingStoresTheMapping() {
        //given
        val originalUrl = "http://example.com";
        val shortenedUrl = "abc123";

        //when
        urlStore.addUrlMapping(shortenedUrl, originalUrl);

        //then
        assertEquals(1, urlEntitiesRepository.count());
        val result = urlEntitiesRepository.findById(shortenedUrl);
        assertTrue(result.isPresent());
        assertEquals(originalUrl, result.get().getOriginalUrl());
        assertEquals(shortenedUrl, result.get().getShortenedUrl());
        assertNotNull(result.get().getCreatedAt());
    }
}