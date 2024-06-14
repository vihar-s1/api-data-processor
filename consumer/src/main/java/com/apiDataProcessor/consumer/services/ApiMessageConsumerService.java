package com.apiDataProcessor.consumer.services;

import com.apiDataProcessor.models.InternalHttpResponse;
import com.apiDataProcessor.models.apiResponse.ApiResponseInterface;
import com.apiDataProcessor.models.apiResponse.joke.JokeApiResponse;
import com.apiDataProcessor.models.apiResponse.randomUser.RandomUserApiResponse;
import com.apiDataProcessor.models.standardMediaData.Adapter;
import com.apiDataProcessor.models.standardMediaData.StandardMediaData;
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
            @Payload ApiResponseInterface apiResponse,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partitionId,
            @Header(KafkaHeaders.OFFSET) int offset
            ) {

        log.info(
                "Received Message at Partition=[" + partitionId + "], Offset=[" + offset + "] : [" + apiResponse + "]"
        );

        handleApiMessage(apiResponse);
    } // method rawDataObjectHandler() ends

    private void handleApiMessage(ApiResponseInterface apiMessage) {
        if (apiMessage == null) return;

        switch (apiMessage.getApiType()){
            case JOKE -> Adapter.toStandardMediaData((JokeApiResponse) apiMessage).forEach(this::sendDBWriteRequest);
            case RANDOM_USER -> Adapter.toStandardMediaData((RandomUserApiResponse) apiMessage).forEach(this::sendDBWriteRequest);
        }
    }


    private void sendDBWriteRequest(StandardMediaData mediaData){
        try {
            webClientBuilder.build()
                    .post()
                    .body(Mono.just(mediaData), StandardMediaData.class)
                    .retrieve()
                    .bodyToMono(InternalHttpResponse.class)
                    .toFuture()
                    .whenComplete((httpResponse, throwable) -> {
                        if (throwable == null) {
                            log.info("DATABASE WRITE REQUEST : apiType=[{}] : success=[{}]", mediaData.getApiType(), httpResponse.getSuccess());
                            log.debug("Data Sent is : {}", httpResponse.getData());
                        }
                        else {
                            log.error("Returned Throwable=[{}] : apiType=[{}]", throwable, mediaData.getApiType());
//                        throw new RuntimeException(throwable);
                        }
                    });
        }
        catch (Exception exception){
            log.error(
                    "ERROR SENDING DATABASE WRITE REQUEST: Exception=[{}] : Message=[{}] : messageType=[{}]",
                    exception.getClass().getSimpleName(),
                    exception.getMessage(),
                    mediaData.getApiType()
            );
        }
    }
}
