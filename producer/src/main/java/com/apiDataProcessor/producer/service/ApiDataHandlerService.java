package com.apiDataProcessor.producer.service;

import com.apiDataProcessor.models.apiResponse.ApiResponseInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

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
        fetchData(apiUri, responseClass, headers -> {});
    }

    public void fetchData(String apiUri, Class<? extends ApiResponseInterface> responseClass, Consumer<HttpHeaders> headersConsumer) {
        if (apiUri == null || apiUri.isBlank()) {
            log.error("API URI is null or empty");
            return;
        }
        if (responseClass == null) {
            log.error("Response class is null");
            return;
        }

        webClientBuilder.build()
                .get()
                .uri(apiUri)
                .headers(headersConsumer)
                .retrieve()
                .bodyToMono(responseClass)
                .subscribe(this::successHandler, this::errorHandler)
                ;
    }

    private void successHandler(ApiResponseInterface apiResponse) {
        log.info("Handling response of type {}", apiResponse.getClass().getSimpleName());
        producerService.sendMessage(apiResponse);
    }

    private void errorHandler(Throwable error) {
        log.error("Error occurred while fetching data: {}", error.getMessage());
    }
}
