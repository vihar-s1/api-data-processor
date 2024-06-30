package com.apiDataProcessor.producer.service.api;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.apiResponse.randomUser.RandomUserApiResponse;
import com.apiDataProcessor.producer.service.ApiRequestService;
import org.springframework.stereotype.Service;

@Service
public class RandomUserService extends ApiService {

    private boolean disabled = false;

    public RandomUserService(ApiRequestService apiRequestService) {
        super(apiRequestService);
    }


    @Override
    public void fetchData() {
        String uri = "https://randomuser.me/api/1.4?results=5&noinfo";

        apiRequestService.fetchData(uri, RandomUserApiResponse.class);
    }

    @Override
    public boolean isExecutable() {
        return true;
    }

    @Override
    public boolean isUnauthorized() {
        return false;
    }

    @Override
    public ApiType getApiType() {
        return ApiType.RANDOM_USER;
    }

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public void disable() {
        this.disabled = true;
    }

    @Override
    public void enable() {
        this.disabled = false;
    }
}
