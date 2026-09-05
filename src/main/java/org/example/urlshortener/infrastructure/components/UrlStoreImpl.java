package org.example.urlshortener.infrastructure.components;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.urlshortener.domain.UrlStore;
import org.example.urlshortener.infrastructure.persistence.entities.UrlEntity;
import org.example.urlshortener.infrastructure.persistence.repositories.UrlEntitiesRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UrlStoreImpl implements UrlStore {
    private final UrlEntitiesRepository urlEntitiesRepository;

    @Override
    public Optional<String> getShortUrl(String originalUrl) {
        Assert.isTrue(StringUtils.isNotBlank(originalUrl), "Original URL must not be blank");

        return urlEntitiesRepository.findByOriginalUrl(originalUrl).map(UrlEntity::getShortenedUrl);
    }

    @Override
    public Optional<String> getOriginalUrl(String shortenedUrl) {
        Assert.isTrue(StringUtils.isNotBlank(shortenedUrl), "Shortened URL must not be blank");

        return urlEntitiesRepository.findById(shortenedUrl).map(UrlEntity::getOriginalUrl);
    }

    @Override
    public void addUrlMapping(String shortenedUrl, String originalUrl) {
        Assert.isTrue(StringUtils.isNotBlank(shortenedUrl), "Shortened URL must not be blank");
        Assert.isTrue(StringUtils.isNotBlank(originalUrl), "Original URL must not be blank");

        urlEntitiesRepository.save(UrlEntity.builder()
                .shortenedUrl(shortenedUrl)
                .originalUrl(originalUrl)
                .build());
    }
}
