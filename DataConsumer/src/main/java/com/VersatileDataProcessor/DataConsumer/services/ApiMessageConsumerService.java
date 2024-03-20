package com.VersatileDataProcessor.DataConsumer.services;


import com.VersatileDataProcessor.DataConsumer.models.MyResponseBody;
import com.VersatileDataProcessor.DataConsumer.models.apiMessages.ApiMessageInterface;
import com.VersatileDataProcessor.DataConsumer.models.apiMessages.JokeApiMessage;
import com.VersatileDataProcessor.DataConsumer.models.apiMessages.MockApiMessage;
import com.VersatileDataProcessor.DataConsumer.models.processedMessages.JokeMessage;
import com.VersatileDataProcessor.DataConsumer.models.processedMessages.MockMessage;
import com.VersatileDataProcessor.DataConsumer.models.processedMessages.ProcessedMessageInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void genericApiMessageHandler(
            @Payload ApiMessageInterface apiMessage,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partitionId,
            @Header(KafkaHeaders.OFFSET) int offset
            ) {

        log.info(
                "Received Message at Partition=[" + partitionId + "], Offset=[" + offset + "] : [" + apiMessage + "]"
        );

        handleApiMessage(apiMessage);
    } // method rawDataObjectHandler() ends

    private void handleApiMessage(ApiMessageInterface apiMessage) {
        if (apiMessage == null) return;
        switch (apiMessage.getMessageType()){
            case MOCK -> sendDBWriteRequest( MockMessage.processApiMessage((MockApiMessage) apiMessage) );
            case JOKE -> JokeMessage.processApiMessage((JokeApiMessage) apiMessage).forEach(this::sendDBWriteRequest);
        }
    }


    private void sendDBWriteRequest(ProcessedMessageInterface processedMessage){
        webClientBuilder.build()
                .post()
                .body(Mono.just(processedMessage), processedMessage.getClass())
                .retrieve()
                .bodyToMono(MyResponseBody.class)
                .toFuture()
                .whenComplete((myResponseBody, throwable) -> {
                    if (throwable == null) {
                        log.info("Request sent with success=[" + myResponseBody.getSuccess() + "], and return message=[" + myResponseBody.getMessage() + "]");
                        log.debug("Data Sent is : " + myResponseBody.getData());
                    }
                    else {
                        log.error("exception occurred : " + throwable);
                        throw new RuntimeException(throwable);
                    }
                });
    }
}
