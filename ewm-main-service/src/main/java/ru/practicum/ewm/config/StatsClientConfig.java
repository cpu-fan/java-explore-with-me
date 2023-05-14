package ru.practicum.ewm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.ewm.stats.client.StatsClient;

@Configuration
public class StatsClientConfig {

    @Value("${stats-server.url}")
    private String serverUrl;

    @Bean
    StatsClient client() {
        return new StatsClient(serverUrl);
    }
}
