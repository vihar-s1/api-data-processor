package com.apiDataProcessor.producer.service;

import com.apiDataProcessor.models.apiResponse.ApiResponseInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class ApiDataHandlerService {

    private final WebClient.Builder webClientBuilder;
    private final KafkaProducerService producerService;

    public ApiDataHandlerService(WebClient.Builder webClientBuilder, KafkaProducerService producerService) {
        this.webClientBuilder = webClientBuilder;
        this.producerService = producerService;
    }

    public void fetchData(String apiUri, Class<? extends ApiResponseInterface> responseClass) {
        webClientBuilder.build()
                .get()
                .uri(apiUri)
                .retrieve()
                .bodyToMono(responseClass)
                .subscribe(apiResponse -> {
                    log.info("Handling response of type {}", responseClass.getSimpleName());
                    producerService.sendMessage(apiResponse);
                });
    }
}
