package com.versatileDataProcessor.dataProducer.fetcher;

import com.versatileDataProcessor.dataProducer.models.apiMessages.RandomUserApiMessage;
import com.versatileDataProcessor.dataProducer.service.ApiMessageProducerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RandomUserApiFetcherTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private Mono<RandomUserApiMessage> randomUserApiMessageMono;

    @Mock
    private RandomUserApiMessage randomUserApiMessage;

    @Mock
    private ApiMessageProducerService apiMessageProducerService;

    @InjectMocks
    private RandomUserApiFetcher randomUserApiFetcher;

    @Test
    void getUser_success() {
        // Given
        String uri = "https://randomuser.me/api/1.4?results=5&noinfo";

        // Mock WebClient builder
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(uri)).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(RandomUserApiMessage.class)).thenReturn(randomUserApiMessageMono);
        when(randomUserApiMessageMono.block()).thenReturn(randomUserApiMessage);

        // When
        randomUserApiFetcher.fetchData();

        // Then
        verify(apiMessageProducerService, times(1)).sendMessage(randomUserApiMessage);
    }
}