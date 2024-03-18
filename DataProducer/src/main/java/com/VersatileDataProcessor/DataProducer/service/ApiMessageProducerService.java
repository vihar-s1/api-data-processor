package com.VersatileDataProcessor.DataProducer.service;

import com.VersatileDataProcessor.DataProducer.models.ApiMessages.ApiMessageInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ApiMessageProducerService {
    @Autowired
    private KafkaTemplate<String, ApiMessageInterface> kafkaTemplate;

    @Value(value = "${spring.kafka.topic.name}")
    private String kafkaTopicName;

    public void sendMessage(ApiMessageInterface message) {
        CompletableFuture<SendResult<String, ApiMessageInterface>> future = kafkaTemplate.send(kafkaTopicName, message.getId(), message);

        future.whenComplete((result, exception) -> {
            if (exception == null) {
                System.out.println(
                        "Partition=[" + result.getRecordMetadata().partition() +
                                "] : Offset=[" + result.getRecordMetadata().offset() +
                                "] : Sent Message=[" + message +
                                "] : data-class=[" + message.getMessageType() + "]"
                );

            }
            else {
                System.out.println(
                        "Unable to send message=[" + message + "] due to : " + exception.getCause().toString()
                );
            }
        });
    }
}
