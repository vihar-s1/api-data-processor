package com.versatileDataProcessor.dataProducer.models.apiMessages;

import com.versatileDataProcessor.dataProducer.models.MessageType;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
class Joke implements Serializable {
    private String category;
    private String type;
    private String joke;
    private Map<String, Boolean> flags;
}
