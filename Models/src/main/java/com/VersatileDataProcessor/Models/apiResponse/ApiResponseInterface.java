package com.VersatileDataProcessor.Models.apiResponse;

import com.VersatileDataProcessor.Models.ApiType;
import com.VersatileDataProcessor.Models.deserializer.ApiResponseInterfaceDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

@JsonDeserialize(using = ApiResponseInterfaceDeserializer.class)
public interface ApiResponseInterface extends Serializable {
    String getId();
    void setId(String id);

    ApiType getApiType();
}
