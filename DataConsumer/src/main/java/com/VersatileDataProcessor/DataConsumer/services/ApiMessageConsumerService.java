package com.versatileDataProcessor.dataConsumer.services;


import com.versatileDataProcessor.dataConsumer.models.MyResponseBody;
import com.versatileDataProcessor.dataConsumer.models.apiMessages.ApiMessageInterface;
import com.versatileDataProcessor.dataConsumer.models.apiMessages.JokeApiMessage;
import com.versatileDataProcessor.dataConsumer.models.apiMessages.RandomUserApiMessage;
import com.versatileDataProcessor.dataConsumer.models.processedMessages.JokeMessage;
import com.versatileDataProcessor.dataConsumer.models.processedMessages.ProcessedMessageInterface;
import com.versatileDataProcessor.dataConsumer.models.processedMessages.RandomUserMessage;
import lombok.extern.slf4j.Slf4j;
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

    private final WebClient.Builder webClientBuilder;

    public ApiMessageConsumerService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

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
            case JOKE -> JokeMessage.processApiMessage((JokeApiMessage) apiMessage).forEach(this::sendDBWriteRequest);
            case RANDOM_USER -> RandomUserMessage.processApiMessage((RandomUserApiMessage) apiMessage).forEach(this::sendDBWriteRequest);
        }
    }


    private void sendDBWriteRequest(ProcessedMessageInterface processedMessage){
        try {
            webClientBuilder.build()
                    .post()
                    .body(Mono.just(processedMessage), processedMessage.getClass())
                    .retrieve()
                    .bodyToMono(MyResponseBody.class)
                    .toFuture()
                    .whenComplete((myResponseBody, throwable) -> {
                        if (throwable == null) {
                            log.info("DATABASE WRITE REQUEST : success=[" + myResponseBody.getSuccess() + "] : message=[" + myResponseBody.getMessage()
                                    + "] : sent dataType=[" + processedMessage.getMessageType() + "]");
                            log.debug("Data Sent is : " + myResponseBody.getData());
                        }
                        else {
                            log.error("exception occurred : dataType=[" + processedMessage.getMessageType() + "] : message=[" + myResponseBody.getMessage() + "] : " + throwable);
//                        throw new RuntimeException(throwable);
                        }
                    });
        }
        catch (Exception exception){
            log.error(
                    "ERROR SENDING DATABASE WRITE REQUEST: Exception=[{}] : Message=[{}] : messageType=[{}]",
                    exception.getClass().getSimpleName(),
                    exception.getMessage(),
                    processedMessage.getMessageType()
            );
        }
    }
}
