package com.VersatileDataProcessor.DataConsumer.models.apiMessages;

import com.VersatileDataProcessor.DataConsumer.models.MessageType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonDeserialize(as = JokeApiMessage.class)
public class JokeApiMessage implements ApiMessageInterface {
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
class Joke {
    private String category;
    private String type;
    private String joke;
    private Map<String, Boolean> flags;
}
