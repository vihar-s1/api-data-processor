package com.versatileDataProcessor.dataConsumer.models.apiMessages;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.versatileDataProcessor.dataConsumer.models.MessageType;
import com.versatileDataProcessor.dataConsumer.models.messageSupport.Joke;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonDeserialize(as = JokeApiMessage.class)
public class JokeApiMessage implements ApiMessageInterface {
    private String id;

    @Getter
    private final MessageType messageType = MessageType.JOKE;

    private boolean error;
    private int amount;
    private List<Joke> jokes;
}
