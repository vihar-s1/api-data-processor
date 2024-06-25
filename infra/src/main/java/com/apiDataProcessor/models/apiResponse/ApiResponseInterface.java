package com.apiDataProcessor.models.apiResponse;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.deserializer.ApiResponseInterfaceDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

@JsonDeserialize(using = ApiResponseInterfaceDeserializer.class)
public interface ApiResponseInterface extends Serializable {
    ApiType getApiType();

    Long size();
}
