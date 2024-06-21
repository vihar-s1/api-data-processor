package com.apiDataProcessor.models.apiResponse.randomUser;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.apiResponse.ApiResponseInterface;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonDeserialize(as = RandomUserApiResponse.class)
@Data @NoArgsConstructor @AllArgsConstructor
public class RandomUserApiResponse implements ApiResponseInterface {
    @Getter
    private ApiType apiType = ApiType.RANDOM_USER;

    private List<User> results;
}
