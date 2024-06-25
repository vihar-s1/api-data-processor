package com.apiDataProcessor.models.apiResponse.reddit;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.apiResponse.ApiResponseInterface;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@JsonDeserialize(as = RedditApiResponse.class)
public class RedditApiResponse implements ApiResponseInterface {
    @Setter(AccessLevel.NONE)
    private ApiType apiType = ApiType.REDDIT;

    private String kind;
    private RedditApiResponseData data;

    @Override
    public Long size() {
        return data.getCount();
    }
}

