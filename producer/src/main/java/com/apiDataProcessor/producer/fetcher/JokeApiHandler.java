package com.apiDataProcessor.producer.fetcher;

import com.apiDataProcessor.models.apiResponse.joke.JokeApiResponse;
import com.apiDataProcessor.producer.service.ApiMessageProducerService;
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

        webClientBuilder.build()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(JokeApiResponse.class)
                .subscribe(jokeApiResponse -> {
                    log.info("Joke(s) fetched by {}", JokeApiHandler.class.getSimpleName());
                    log.info("Sending the joke(s) to Kafka");

                    producerService.sendMessage(jokeApiResponse);
                });
    }
}
