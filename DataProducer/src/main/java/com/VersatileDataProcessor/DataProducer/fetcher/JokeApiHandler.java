package com.VersatileDataProcessor.DataProducer.fetcher;

import com.VersatileDataProcessor.DataProducer.service.ApiMessageProducerService;
import com.VersatileDataProcessor.Models.apiResponse.joke.JokeApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class JokeApiHandler implements ApiDataHandlerInterface {

    private final WebClient.Builder webClientBuilder;
    private final ApiMessageProducerService producerService;

    public JokeApiHandler(WebClient.Builder webClientBuilder, ApiMessageProducerService producerService) {
        this.webClientBuilder = webClientBuilder;
        this.producerService = producerService;
    }

    @Override
    public void fetchData() {
        String uri = "https://v2.jokeapi.dev/joke/Any?type=single&amount=5";

        JokeApiResponse jokeResponse = webClientBuilder.build()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(JokeApiResponse.class)
                .block();

        log.info("Jokes fetched by {}", JokeApiHandler.class.getSimpleName());
        log.info("Sending Jokes to Kafka");

        producerService.sendMessage(jokeResponse);
    }
}
