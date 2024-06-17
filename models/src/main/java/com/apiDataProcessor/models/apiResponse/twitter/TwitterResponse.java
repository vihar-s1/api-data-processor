package com.apiDataProcessor.models.apiResponse.twitter;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.apiResponse.ApiResponseInterface;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Data
public class TwitterResponse implements ApiResponseInterface {
    private String id = UUID.randomUUID().toString();

    @Setter(AccessLevel.NONE)
    private ApiType apiType = ApiType.TWITTER;

    private List<Tweet> data;

    // field to ignore after deserialized
    private TwitterResponseAdditional includes;

}
