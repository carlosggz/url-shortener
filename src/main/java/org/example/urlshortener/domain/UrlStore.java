package org.example.urlshortener.domain;

import java.util.Optional;

public interface UrlStore {

    Optional<String> getShortUrl(String originalUrl);
    Optional<String> getOriginalUrl(String shortenedUrl);
    void addUrlMapping(String shortenedUrl, String originalUrl);
}
