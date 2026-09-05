package org.example.urlshortener.infrastructure.api;

import lombok.RequiredArgsConstructor;
import org.example.urlshortener.domain.UrlStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class RedirectController {

    private final UrlStore urlStore;

    @GetMapping("/{shortenedUrl}")
    public ResponseEntity<?> redirect(@PathVariable String shortenedUrl) {
        return urlStore
                .getOriginalUrl(shortenedUrl)
                .map(originalUrl -> ResponseEntity
                        .status(HttpStatus.FOUND)
                        .location(URI.create(originalUrl))
                        .build())
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
