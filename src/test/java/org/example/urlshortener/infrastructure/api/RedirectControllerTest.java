package org.example.urlshortener.infrastructure.api;

import lombok.SneakyThrows;
import lombok.val;
import org.example.urlshortener.domain.UrlStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RedirectController.class)
class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlStore urlStore;

    @Test
    @SneakyThrows
    void whenRequestARedirectWithAValidShortenedUrl_thenRedirectToOriginalUrl() {
        //given
        val originalUrl = "https://www.example.com";
        val shortenedUrl = "abc123";
        doReturn(Optional.of(originalUrl)).when(urlStore).getOriginalUrl(shortenedUrl);

        //when/then
        mockMvc
                .perform(get("/" + shortenedUrl))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(originalUrl));
    }

    @Test
    @SneakyThrows
    void whenRequestARedirectWithAnInvalidShortenedUrl_thenReturnNotFound() {
        //given
        val shortenedUrl = "abc123";
        doReturn(Optional.empty()).when(urlStore).getOriginalUrl(shortenedUrl);

        //when/then
        mockMvc
                .perform(get("/" + shortenedUrl))
                .andExpect(status().isNotFound());
    }
}