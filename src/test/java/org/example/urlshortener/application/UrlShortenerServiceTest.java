package org.example.urlshortener.application;

import lombok.val;
import org.example.urlshortener.domain.SeedGenerator;
import org.example.urlshortener.domain.StringEncoder;
import org.example.urlshortener.infrastructure.components.UrlStoreImpl;
import org.example.urlshortener.infrastructure.persistence.entities.UrlEntity;
import org.example.urlshortener.utils.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest extends BaseIntegrationTest {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @MockitoSpyBean
    private SeedGenerator seedGenerator;

    @MockitoSpyBean
    private UrlStoreImpl urlStore;

    @MockitoSpyBean
    private StringEncoder stringEncoder;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Test
    void invalidUrlShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> urlShortenerService.generateShortUrl(null));
    }

    @Test
    void nonExistentUrlShouldGenerateShortUrl() {
        //given
        val longUrl = URI.create("https://www.example.com/some/long/url");

        //when
        val shortUrl = urlShortenerService.generateShortUrl(longUrl);

        //then
        assertNotNull(shortUrl);
        assertFalse(shortUrl.isEmpty());
        verify(seedGenerator).generateSeed();
        verify(stringEncoder).encode(any());
        verify(urlStore).addUrlMapping(stringCaptor.capture(), eq(longUrl.toString()));
        val capturedShortUrl = stringCaptor.getValue();
        assertEquals(shortUrl, capturedShortUrl);
    }

    @Test
    void existentUrlShouldReturnTheSame() {
        //given
        val longUrl = URI.create("https://www.example.com/some/long/url");
        val expectedShortUrl = "abc123";
        urlEntitiesRepository.save(UrlEntity.builder()
                .originalUrl(longUrl.toString())
                .shortenedUrl(expectedShortUrl)
                .build());

        //when
        val shortUrl = urlShortenerService.generateShortUrl(longUrl);

        //then
        assertNotNull(shortUrl);
        assertEquals(expectedShortUrl, shortUrl);
        verify(urlStore).getShortUrl(longUrl.toString());
        verifyNoInteractions(seedGenerator, stringEncoder);
        verifyNoMoreInteractions(urlStore);
    }
}