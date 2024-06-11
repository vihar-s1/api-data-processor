package com.VersatileDataProcessor.DataProducer.service;

import com.VersatileDataProcessor.Models.apiResponse.ApiResponseInterface;
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
public class ApiMessageProducerService {
    private final KafkaTemplate<String, ApiResponseInterface> kafkaTemplate;

    @Value(value = "${spring.kafka.topic.name}")
    private String kafkaTopicName;

    public ApiMessageProducerService(KafkaTemplate<String, ApiResponseInterface> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Bean
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }


    public void sendMessage(ApiResponseInterface message) {
        CompletableFuture<SendResult<String, ApiResponseInterface>> future = kafkaTemplate.send(kafkaTopicName, message.getId(), message);

        future.whenComplete((result, exception) -> {
            if (exception == null) {
                log.info(
                        "Sent Message to Partition=[{}] with Offset=[{}] : [{}]",
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        message
                );
            }
            else {
                log.error(
                        "Unable to send message=[{}] due to :{}", message,
                        exception.getCause().toString()
                );
            }
        });
    }
}
