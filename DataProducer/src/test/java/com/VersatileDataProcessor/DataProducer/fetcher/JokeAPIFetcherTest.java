package com.VersatileDataProcessor.DataProducer.fetcher;

import com.VersatileDataProcessor.DataProducer.models.apiMessages.JokeApiMessage;
import com.VersatileDataProcessor.DataProducer.service.ApiMessageProducerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JokeAPIFetcherTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private Mono<JokeApiMessage> jokeApiMessageMono;

    @Mock
    private JokeApiMessage jokeApiMessage;

    @Mock
    private ApiMessageProducerService apiMessageProducerService;

    @InjectMocks
    private JokeAPIFetcher jokeAPIFetcher;

    @BeforeAll
    static void setup() {
    }

    @Test
    void getJoke_success() {
        // Given
        String uri = "https://v2.jokeapi.dev/joke/Any?type=single&amount=5";

        // Mock WebClient builder
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(uri)).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JokeApiMessage.class)).thenReturn(jokeApiMessageMono);
        when(jokeApiMessageMono.block()).thenReturn(jokeApiMessage);

        // When
        jokeAPIFetcher.fetchData();

        // Then
        verify(apiMessageProducerService, times(1)).sendMessage(jokeApiMessage);
    }
}