package com.apiDataProcessor.producer.handler;

import com.apiDataProcessor.models.apiResponse.randomUser.RandomUserApiResponse;
import com.apiDataProcessor.producer.service.ApiDataHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
public class RandomUserApiHandler implements ApiDataHandlerInterface {

    private final ApiDataHandlerService apiDataHandler;

    public RandomUserApiHandler(ApiDataHandlerService apiDataHandlerService) {
        this.apiDataHandler = apiDataHandlerService;
    }

    @Override
    public void fetchData() {
        String uri = "https://randomuser.me/api/1.4?results=5&noinfo";

        apiDataHandler.fetchData(uri, RandomUserApiResponse.class);
    }
}
