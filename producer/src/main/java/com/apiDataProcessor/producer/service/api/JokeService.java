package com.apiDataProcessor.producer.service.api;

import com.apiDataProcessor.models.apiResponse.joke.JokeApiResponse;
import com.apiDataProcessor.producer.service.ApiRequestService;
import org.springframework.stereotype.Service;

@Service
public class JokeService extends ApiService {

    public JokeService(ApiRequestService apiRequestService) {
        super(apiRequestService);
    }

    @Override
    public void fetchData() {
        String uri = "https://v2.jokeapi.dev/joke/Any?type=single&amount=5";

        apiRequestService.fetchData(uri, JokeApiResponse.class);
    }

    @Override
    public boolean isExecutable() {
        return true;
    }

    @Override
    public boolean isAuthorized() {
        return true;
    }
}
