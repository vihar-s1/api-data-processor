package com.apiDataProcessor.producer.service.api;

public abstract class ApiService {
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
     * @return true if the service is authorized
     */
    public abstract boolean isAuthorized();
}
