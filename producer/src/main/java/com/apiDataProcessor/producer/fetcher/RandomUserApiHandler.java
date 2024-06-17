package com.apiDataProcessor.producer.fetcher;

import com.apiDataProcessor.models.apiResponse.randomUser.RandomUserApiResponse;
import com.apiDataProcessor.producer.service.ApiMessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class RandomUserApiHandler implements ApiDataHandlerInterface {
    private final WebClient.Builder webClientBuilder;
    private final ApiMessageProducerService producerService;

    public RandomUserApiHandler(WebClient.Builder webClientBuilder, ApiMessageProducerService producerService) {
        this.webClientBuilder = webClientBuilder;
        this.producerService = producerService;
    }

    @Override
    public void fetchData() {
        String uri = "https://randomuser.me/api/1.4?results=5&noinfo";

        webClientBuilder.build()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(RandomUserApiResponse.class)
                .subscribe(randomUserApiResponse -> {
                    log.info("Random User(s) fetched by {}", RandomUserApiHandler.class.getSimpleName());
                    log.info("Sending the random user(s) to Kafka");

                    producerService.sendMessage(randomUserApiResponse);
                });
    }
}
