package org.example.urlshortener.infrastructure.models;

import java.net.URI;

public record ShortenUrlRequest(URI originalUrl) {}

