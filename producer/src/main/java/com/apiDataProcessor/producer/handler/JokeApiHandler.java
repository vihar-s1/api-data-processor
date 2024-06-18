package com.apiDataProcessor.producer.handler;

import com.apiDataProcessor.models.apiResponse.joke.JokeApiResponse;
import com.apiDataProcessor.producer.service.ApiDataHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
public class JokeApiHandler implements ApiDataHandlerInterface {

    private final ApiDataHandlerService apiDataHandlerService;

    public JokeApiHandler(ApiDataHandlerService apiDataHandlerService) {
        this.apiDataHandlerService = apiDataHandlerService;
    }

    @Override
    public void fetchData() {
        String uri = "https://v2.jokeapi.dev/joke/Any?type=single&amount=5";

        apiDataHandlerService.fetchData(uri, JokeApiResponse.class);
    }
}
