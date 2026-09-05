package org.example.urlshortener.infrastructure.api;

import lombok.SneakyThrows;
import lombok.val;
import org.example.urlshortener.application.UrlShortenerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = UrlShortenerController.class,
        properties = {
                "app.base-url=https://www.myservice.com:8080"
        })
class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlShortenerService urlShortenerService;

    @Test
    @SneakyThrows
    void whenRequestAShortenedUrl_thenReturnShortenedUrl() {
        //given
        val url = "https://www.example.com";
        val shortenedUrl = "abc123";
        val expectedShortenedUrl = "https://www.myservice.com:8080/" + shortenedUrl;
        doReturn(shortenedUrl).when(urlShortenerService).generateShortUrl(any());

        //when/then
        mockMvc
                .perform(post("/api/v1/generate-url")
                .contentType("application/json")
                .content("{ \"originalUrl\": \"" + url + "\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl").value(url))
                .andExpect(jsonPath("$.shortenedUrl").value(expectedShortenedUrl));
    }
}