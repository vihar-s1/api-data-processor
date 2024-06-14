package com.apiDataProcessor.producer.fetcher;

import com.apiDataProcessor.models.apiResponse.joke.JokeApiResponse;
import com.apiDataProcessor.producer.service.ApiMessageProducerService;
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
class JokeApiHandlerTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private Mono<JokeApiResponse> jokeApiResponseMono;

    @Mock
    private JokeApiResponse jokeApiResponse;

    @Mock
    private ApiMessageProducerService apiMessageProducerService;

    @InjectMocks
    private JokeApiHandler jokeApiHandler;

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
        when(responseSpec.bodyToMono(JokeApiResponse.class)).thenReturn(jokeApiResponseMono);
        when(jokeApiResponseMono.block()).thenReturn(jokeApiResponse);

        // When
        jokeApiHandler.fetchData();

        // Then
        verify(apiMessageProducerService, times(1)).sendMessage(jokeApiResponse);
    }
}