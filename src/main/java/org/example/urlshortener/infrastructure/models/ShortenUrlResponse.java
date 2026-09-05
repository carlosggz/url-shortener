package org.example.urlshortener.infrastructure.models;

import java.net.URI;

public record ShortenUrlResponse(URI originalUrl, URI shortenedUrl) {}
