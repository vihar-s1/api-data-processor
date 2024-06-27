package com.apiDataProcessor.models.apiResponse;

import com.apiDataProcessor.models.ApiType;

import java.io.Serializable;

public interface ApiResponseInterface extends Serializable {
    ApiType getApiType();

    Long size();
}
