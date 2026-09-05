package org.example.urlshortener.application;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.example.urlshortener.domain.SeedGenerator;
import org.example.urlshortener.domain.StringEncoder;
import org.example.urlshortener.infrastructure.components.UrlStoreImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.net.URI;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final SeedGenerator seedGenerator;
    private final UrlStoreImpl urlStore;
    private final StringEncoder stringEncoder;

    public String generateShortUrl(final URI url) {
        Assert.isTrue(Objects.nonNull(url), "URL must not be null");
        Assert.isTrue(StringUtils.isNotEmpty(url.toString()), "URL must not be empty");

        return urlStore
                .getShortUrl(url.toString())
                .orElseGet(() -> {
                    val seed = seedGenerator.generateSeed();
                    val hash = stringEncoder.encode(seed);
                    urlStore.addUrlMapping(hash, url.toString());
                    return hash;
                });
    }
}
