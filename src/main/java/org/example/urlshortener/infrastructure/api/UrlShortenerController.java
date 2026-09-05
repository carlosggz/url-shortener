package org.example.urlshortener.infrastructure.api;

import lombok.val;
import org.example.urlshortener.application.UrlShortenerService;
import org.example.urlshortener.infrastructure.models.ShortenUrlRequest;
import org.example.urlshortener.infrastructure.models.ShortenUrlResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/generate-url")
public class UrlShortenerController {

    private final String baseUrl;
    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(
            @Value("${app.base-url}") String baseUrl,
            UrlShortenerService urlShortenerService) {
        this.baseUrl = baseUrl;
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping
    public ShortenUrlResponse shortenUrl(@RequestBody ShortenUrlRequest request) {
        val shortenedUrl = urlShortenerService.generateShortUrl(request.originalUrl());
        val fullShortenedUrl = UriComponentsBuilder
                .fromUriString(baseUrl + "/{shortenedUrl}")
                .build(shortenedUrl);
        return new ShortenUrlResponse(request.originalUrl(), fullShortenedUrl);
    }
}
