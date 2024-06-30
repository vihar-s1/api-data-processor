package com.apiDataProcessor.consumer.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import static com.apiDataProcessor.utils.utils.basicAuthHeader;

@Configuration
public class DatabaseWebClientConfig {

    @Value(value = "${database.endpoint.add}")
    private String ESAddEndPoint;

    @Value(value = "${database.manager.username}")
    private String dbManagerUsername;

    @Value(value = "${database.manager.password}")
    private String dbManagerPassword;

    @Bean
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder()
                .baseUrl(ESAddEndPoint)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, basicAuthHeader(dbManagerUsername, dbManagerPassword))
                ;
    }
}
