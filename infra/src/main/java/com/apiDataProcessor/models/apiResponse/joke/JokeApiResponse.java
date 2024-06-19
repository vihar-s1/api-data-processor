package com.apiDataProcessor.models.apiResponse.joke;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.apiResponse.ApiResponseInterface;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@JsonDeserialize(as = JokeApiResponse.class)
@Data @NoArgsConstructor @AllArgsConstructor
public class JokeApiResponse implements ApiResponseInterface {
    private String id = UUID.randomUUID().toString(); // Default Value

    @Getter
    private final ApiType apiType = ApiType.JOKE;

    private boolean error;
    private int amount;
    private List<Joke> jokes;
}

