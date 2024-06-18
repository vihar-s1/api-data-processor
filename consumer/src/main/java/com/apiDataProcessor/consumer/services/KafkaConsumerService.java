package com.apiDataProcessor.consumer.services;

import com.apiDataProcessor.models.InternalHttpResponse;
import com.apiDataProcessor.models.apiResponse.ApiResponseInterface;
import com.apiDataProcessor.models.apiResponse.joke.JokeApiResponse;
import com.apiDataProcessor.models.apiResponse.randomUser.RandomUserApiResponse;
import com.apiDataProcessor.models.apiResponse.twitter.TwitterApiResponse;
import com.apiDataProcessor.models.genericChannelPost.Adapter;
import com.apiDataProcessor.models.genericChannelPost.GenericChannelPost;
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
public class KafkaConsumerService {

    private final WebClient.Builder webClientBuilder;

    public KafkaConsumerService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "getStandardContainerFactory"
    )
    public void genericApiResponseListener(
            @Payload ApiResponseInterface apiResponse,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partitionId,
            @Header(KafkaHeaders.OFFSET) int offset
            ) {

        log.info(
                "Received Message at Partition=[{}], Offset=[{}] : [(apiType={}, id={})]",
                partitionId, offset, apiResponse.getApiType(), apiResponse.getId()
        );

        switch (apiResponse.getApiType()) {
            case JOKE -> Adapter.toGenericChannelPost((JokeApiResponse) apiResponse).forEach(this::sendDBWriteRequest);
            case RANDOM_USER -> Adapter.toGenericChannelPost((RandomUserApiResponse) apiResponse).forEach(this::sendDBWriteRequest);
            case TWITTER -> Adapter.toGenericChannelPost((TwitterApiResponse) apiResponse).forEach(this::sendDBWriteRequest);
        }
    } // method genericApiResponseListener() ends


    private void sendDBWriteRequest(GenericChannelPost channelPost){
        try {
            webClientBuilder.build()
                    .post()
                    .body(Mono.just(channelPost), GenericChannelPost.class)
                    .retrieve()
                    .bodyToMono(InternalHttpResponse.class)
                    .toFuture()
                    .whenComplete((httpResponse, throwable) -> {
                        if (throwable == null) {
                            log.info("DATABASE WRITE REQUEST : apiType=[{}] : success=[{}]", channelPost.getApiType(), httpResponse.getSuccess());
                            log.debug("Data Sent is : {}", httpResponse.getData());
                        }
                        else {
                            log.error("Returned Throwable=[{}] : apiType=[{}]", throwable, channelPost.getApiType());
//                        throw new RuntimeException(throwable);
                        }
                    });
        }
        catch (Exception exception){
            log.error(
                    "ERROR SENDING DATABASE WRITE REQUEST: Exception=[{}] : Message=[{}] : messageType=[{}]",
                    exception.getClass().getSimpleName(),
                    exception.getMessage(),
                    channelPost.getApiType()
            );
        }
    }
}
