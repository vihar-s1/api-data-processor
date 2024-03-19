package com.VersatileDataProcessor.DataProducer.service;

import com.VersatileDataProcessor.DataProducer.models.ApiMessages.ApiMessageInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ApiMessageProducerService {
    @Autowired
    private KafkaTemplate<String, ApiMessageInterface> kafkaTemplate;

    @Value(value = "${spring.kafka.topic.name}")
    private String kafkaTopicName;

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
