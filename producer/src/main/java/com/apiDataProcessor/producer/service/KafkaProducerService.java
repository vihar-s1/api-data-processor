package com.apiDataProcessor.producer.service;

import com.apiDataProcessor.models.apiResponse.ApiResponseInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, ApiResponseInterface> kafkaTemplate;

    @Value(value = "${spring.kafka.topic.name}")
    private String kafkaTopicName;

    public KafkaProducerService(KafkaTemplate<String, ApiResponseInterface> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Bean
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }


    public void sendMessage(ApiResponseInterface response) {
        CompletableFuture<SendResult<String, ApiResponseInterface>> future = kafkaTemplate.send(kafkaTopicName, response.getId(), response);

        future.whenComplete((result, exception) -> {
            if (exception == null) {
                log.info(
                        "Sent Message to Partition=[{}] with Offset=[{}] : apiType=[{}] : response=[id={}]",
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        response.getApiType(),
                        response.getId()
                );
            }
            else {
                log.error(
                        "Unable to send (apiType=[{}], response=[id={}]) due to :{}",
                        response.getApiType(),
                        response.getId(),
                        exception.getCause().toString()
                );
            }
        });
    }
}