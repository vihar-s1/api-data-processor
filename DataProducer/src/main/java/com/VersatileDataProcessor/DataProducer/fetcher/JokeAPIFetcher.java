package com.versatileDataProcessor.dataProducer.fetcher;

import com.versatileDataProcessor.dataProducer.models.apiMessages.JokeApiMessage;
import com.versatileDataProcessor.dataProducer.service.ApiMessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class JokeAPIFetcher implements DataFetcherInterface{

    private final WebClient.Builder webClientBuilder;
    private final ApiMessageProducerService producerService;

    public JokeAPIFetcher(WebClient.Builder webClientBuilder, ApiMessageProducerService producerService) {
        this.webClientBuilder = webClientBuilder;
        this.producerService = producerService;
    }

    @Override
    public void fetchData() {
        String uri = "https://v2.jokeapi.dev/joke/Any?type=single&amount=5";

        JokeApiMessage jokes = webClientBuilder.build()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(JokeApiMessage.class)
                .block();

        log.info("Jokes fetched by JokeApiFetcher");
        log.info("Sending Jokes to Kafka");

        producerService.sendMessage(jokes);
    }
}
