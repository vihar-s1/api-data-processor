package com.apiDataProcessor.producer.service;

import com.apiDataProcessor.models.apiResponse.ApiResponseInterface;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.apiDataProcessor.utils.utils.isEmpty;

@Slf4j
@Service
public class ApiRequestService {

    private final WebClient.Builder webClientBuilder;
    private final KafkaProducerService producerService;

    public ApiRequestService(WebClient.Builder webClientBuilder, KafkaProducerService producerService) {
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

    public Map<String, Object> get(String uri, Map<String, String> headers, Map<String, String> queryParams) throws IOException, InterruptedException {
        StringBuilder finalUri = new StringBuilder(uri);

        if (!isEmpty(queryParams)) {
            finalUri.append("?");
            queryParams.forEach((k, v) -> finalUri.append(k).append("=").append(v).append("&"));
            finalUri.deleteCharAt(finalUri.length() - 1); // deleting last unnecessary "&"
        }

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(java.net.URI.create(finalUri.toString()))
                .headers(headers.entrySet().stream()
                        .map(e -> new String[]{e.getKey(), e.getValue()})
                        .flatMap(Stream::of)
                        .toArray(String[]::new))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return (new ObjectMapper()).readValue(response.body(), (JavaType) new TypeToken<Map<String, Object>>() {}.getType());
    }

    public Map<String, Object> post(String uri, Map<String, String> headers, Map<String, String> queryParams, String requestBody) throws IOException, InterruptedException {
        StringBuilder finalUri = new StringBuilder(uri);

        if (!isEmpty(queryParams)) {
            finalUri.append("?");
            queryParams.forEach((k, v) -> finalUri.append(k).append("=").append(v).append("&"));
            finalUri.deleteCharAt(finalUri.length() - 1); // deleting last unnecessary "&"
        }

        requestBody = requestBody == null ? "" : requestBody;

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(java.net.URI.create(finalUri.toString()))
                .headers(headers.entrySet().stream()
                        .map(e -> new String[]{e.getKey(), e.getValue()})
                        .flatMap(Stream::of)
                        .toArray(String[]::new))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return (new ObjectMapper()).readValue(response.body(), new TypeReference<>() {
        });
    }

    private void successHandler(ApiResponseInterface apiResponse) {
        log.info("Handling response of type {}", apiResponse.getClass().getSimpleName());
        producerService.sendMessage(apiResponse);
    }

    private void errorHandler(Throwable error) {
        log.error("Error occurred while fetching data: {}", error.getMessage());
    }
}
