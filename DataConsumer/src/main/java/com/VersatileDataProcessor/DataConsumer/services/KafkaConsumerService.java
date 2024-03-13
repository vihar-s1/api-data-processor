package com.VersatileDataProcessor.DataConsumer.services;

import com.VersatileDataProcessor.DataConsumer.models.KafkaDataObject;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService{
    @KafkaListener(
            topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "getKafkaDataObjectConsumerFactory"
    )
    public void kafkaDataObjectHandler(
            @Payload KafkaDataObject dataObject,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partitionId,
            @Header(KafkaHeaders.OFFSET) int offset
    ) {
        System.out.println(
                "Partition=[" + partitionId + "] : Offset=[" + offset + "] : Received Message=[" + dataObject + "]"
        );

    }
}
