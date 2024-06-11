package com.VersatileDataProcessor.Models.apiResponse.randomUser;

import com.VersatileDataProcessor.Models.ApiType;
import com.VersatileDataProcessor.Models.apiResponse.ApiResponseInterface;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.List;
import java.util.UUID;

@JsonDeserialize(as = RandomUserApiResponse.class)
@Data @NoArgsConstructor @AllArgsConstructor
public class RandomUserApiResponse implements ApiResponseInterface {
    private String id = UUID.randomUUID().toString(); // Default Value
    @Getter
    private ApiType apiType = ApiType.RANDOM_USER;

    private List<User> results;
}
