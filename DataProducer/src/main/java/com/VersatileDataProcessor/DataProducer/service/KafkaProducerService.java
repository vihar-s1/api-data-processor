package com.VersatileDataProcessor.DataProducer.service;

import com.VersatileDataProcessor.DataProducer.models.KafkaDataObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, KafkaDataObject> kafkaTemplate;

    @Value(value = "${spring.kafka.topic.name}")
    private String kafkaTopicName;

    public void sendMessage(KafkaDataObject message) {
        CompletableFuture<SendResult<String, KafkaDataObject>> future = kafkaTemplate.send(kafkaTopicName, message.getId(), message);

        future.whenComplete((result, exception) -> {
            if (exception == null) {
                System.out.println(
                        "Sent Message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]"
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
