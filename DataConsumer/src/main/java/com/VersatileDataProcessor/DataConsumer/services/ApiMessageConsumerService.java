package com.VersatileDataProcessor.DataConsumer.services;

import com.VersatileDataProcessor.DataConsumer.models.StandardApiMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ApiMessageConsumerService {
    @KafkaListener(
            topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "getStandardContainerFactory"
    )
    public void rawDataObjectHandler(
            @Payload StandardApiMessage dataObject,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partitionId,
            @Header(KafkaHeaders.OFFSET) int offset
            ) {
        System.out.println(
                "Partition=[" + partitionId + "] : Offset=[" + offset + "] : Received Message=[" + dataObject + "]"
        );
    }
}
