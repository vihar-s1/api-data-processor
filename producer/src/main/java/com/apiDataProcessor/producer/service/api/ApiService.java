package com.apiDataProcessor.producer.service.api;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.producer.service.ApiRequestService;

public abstract class ApiService {

    protected final ApiRequestService apiRequestService;

    protected ApiService(ApiRequestService apiRequestService) {
        this.apiRequestService = apiRequestService;
    }


    /**
     * Fetch data from the API and handle it (Send to kafka directly)
     */
    public abstract void fetchData();

    /**
     * Check if the service is executable (has the necessary environment variables)
     * @return true if the service is executable
     */
    public abstract boolean isExecutable();

    /**
     * Check if the service is authorized (has the necessary permissions/tokens)
     * @return true if the service is un-authorized
     */
    public abstract boolean isUnauthorized();

    /**
     * Get the name of the API
     * @return the name of the API
     */
    public abstract ApiType getApiType();

    /**
     * Check if the service is disabled
     * @return true if the service is disabled
     */
    public abstract boolean isDisabled();

    /**
     * Disable the service
     */
    public abstract void disable();

    /**
     * Enable the service
     */
    public abstract void enable();
}
