package org.example.urlshortener.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.integration.support.locks.DefaultLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;

import java.util.concurrent.locks.Lock;

@Configuration
@EnableJpaAuditing
public class AppConfiguration {

    @Bean
    public LockRegistry<Lock> lockRegistry() {
        //For multiple instances of the application and/or different zones,
        // a distributed lock registry should be used instead of a local lock.
        return new DefaultLockRegistry();
    }
}
