package org.example.urlshortener.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Configuration
@EnableJpaAuditing
public class AppConfiguration {

    @Bean
    public Lock sequenceLock() {
        //For multiple instances of the application, a distributed lock should be used instead of a local lock.
        return new ReentrantLock();
    }
}
