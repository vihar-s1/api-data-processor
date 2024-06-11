package com.VersatileDataProcessor.Models.models.apiResponse;

import com.VersatileDataProcessor.Models.MessageType;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class JokeApiResponse implements ApiResponseInterface{
    private String id = UUID.randomUUID().toString();

    @Getter
    private final MessageType messageType = MessageType.JOKE;

    private boolean error;
    private int amount;
    private List<Joke> jokes;
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
class Joke implements Serializable {
    private String category;
    private String type;
    private String joke;
    private Map<String, Boolean> flags;
}
