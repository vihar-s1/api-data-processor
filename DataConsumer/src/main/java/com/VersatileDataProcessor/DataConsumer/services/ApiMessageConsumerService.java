package com.VersatileDataProcessor.DataConsumer.services;

import com.VersatileDataProcessor.DataConsumer.models.MyResponseBody;
import com.VersatileDataProcessor.DataConsumer.models.apiMessages.ApiMessageInterface;
import com.VersatileDataProcessor.DataConsumer.models.apiMessages.MockApiMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service

@Slf4j
public class ApiMessageConsumerService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @KafkaListener(
            topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "getStandardContainerFactory"
    )
    public void rawDataObjectHandler(
            @Payload ApiMessageInterface dataObject,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partitionId,
            @Header(KafkaHeaders.OFFSET) int offset
            ) {

        log.info(
                "Received Message at Partition=[" + partitionId + "], Offset=[" + offset + "] : [" + dataObject + "]"
        );

         webClientBuilder.build()
                 .post()
                 .uri("http://localhost:8084/api/add")
                 .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                 .body(Mono.just(dataObject), MockApiMessage.class)
                 .retrieve()
                 .bodyToMono(MyResponseBody.class)
                 .toFuture()
                 .whenComplete((myResponseBody, throwable) -> {
                     if (throwable == null) {
                         log.info("Request sent with success=[" + myResponseBody.getSuccess() + "], and return message=[" + myResponseBody.getMessage() + "]");
                     }
                     else throw new RuntimeException(throwable);
                 });
    }
}
