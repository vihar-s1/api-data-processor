package com.apiDataProcessor.models.apiResponse.twitter;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.apiResponse.ApiResponseInterface;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@JsonDeserialize(as = TwitterApiResponse.class)
public class TwitterApiResponse implements ApiResponseInterface {
    @Setter(AccessLevel.NONE)
    private ApiType apiType = ApiType.TWITTER;

    private List<Tweet> data;

    // field to ignore after deserialized
    private TwitterApiResponseAdditional includes;

}
