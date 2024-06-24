package com.apiDataProcessor.producer.service.api;

import com.apiDataProcessor.models.apiResponse.joke.JokeApiResponse;
import com.apiDataProcessor.producer.service.ApiDataHandlerService;
import org.springframework.stereotype.Service;

@Service
public class JokeService implements ApiServiceInterface{

    private final ApiDataHandlerService apiDataHandlerService;

    public JokeService(ApiDataHandlerService apiDataHandlerService) {
        this.apiDataHandlerService = apiDataHandlerService;
    }

    @Override
    public void fetchData() {
        String uri = "https://v2.jokeapi.dev/joke/Any?type=single&amount=5";

        apiDataHandlerService.fetchData(uri, JokeApiResponse.class);
    }

    @Override
    public boolean isExecutable() {
        return true;
    }
}
