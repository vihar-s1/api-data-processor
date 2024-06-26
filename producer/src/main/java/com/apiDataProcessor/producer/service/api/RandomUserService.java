package com.apiDataProcessor.producer.service.api;

import com.apiDataProcessor.models.apiResponse.randomUser.RandomUserApiResponse;
import com.apiDataProcessor.producer.service.ApiDataHandlerService;
import org.springframework.stereotype.Service;

@Service
public class RandomUserService extends ApiService {

    private final ApiDataHandlerService apiDataHandlerService;

    public RandomUserService(ApiDataHandlerService apiDataHandlerService) {
        this.apiDataHandlerService = apiDataHandlerService;
    }


    @Override
    public void fetchData() {
        String uri = "https://randomuser.me/api/1.4?results=5&noinfo";

        apiDataHandlerService.fetchData(uri, RandomUserApiResponse.class);
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
