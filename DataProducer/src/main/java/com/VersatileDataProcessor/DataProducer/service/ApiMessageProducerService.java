package com.versatileDataProcessor.dataProducer.service;

import com.versatileDataProcessor.dataProducer.models.apiMessages.ApiMessageInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private KafkaTemplate<String, ApiMessageInterface> kafkaTemplate;

    @Value(value = "${spring.kafka.topic.name}")
    private String kafkaTopicName;

    @Bean
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }


    public void sendMessage(ApiMessageInterface message) {
        CompletableFuture<SendResult<String, ApiMessageInterface>> future = kafkaTemplate.send(kafkaTopicName, message.getId(), message);

        future.whenComplete((result, exception) -> {
            if (exception == null) {
                log.info(
                        "Sent Message to Partition=[" + result.getRecordMetadata().partition()
                        + "] with Offset=[" + result.getRecordMetadata().offset()
                        + "] : [" + message + "]"
                );
            }
            else {
                log.warn(
                    "Unable to send message=[" + message + "] due to :" + exception.getCause().toString()
                );
            }
        });
    }
}
