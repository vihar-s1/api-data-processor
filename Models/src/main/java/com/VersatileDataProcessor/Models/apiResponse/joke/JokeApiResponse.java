package com.VersatileDataProcessor.Models.apiResponse.joke;

import com.VersatileDataProcessor.Models.ApiType;
import com.VersatileDataProcessor.Models.apiResponse.ApiResponseInterface;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

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

