package com.versatileDataProcessor.dataConsumer.models.processedMessages;


import com.versatileDataProcessor.dataConsumer.models.MessageType;
import com.versatileDataProcessor.dataConsumer.models.apiMessages.JokeApiMessage;
import com.versatileDataProcessor.dataConsumer.models.messageSupport.Joke;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

@Getter
@Setter
@ToString
public class JokeMessage implements ProcessedMessageInterface{
    private String id;
    private String category;
    private Set<String> tags;
    private List<String> statements;
    @Getter
    private MessageType messageType = MessageType.JOKE;

    private JokeMessage() {
        this.tags = new HashSet<>();
    }

    public void addTag(String tag){
        tags.add(tag);
    }

    public static List<JokeMessage> processApiMessage(JokeApiMessage apiMessage){
        if (apiMessage == null || apiMessage.isError() || apiMessage.getAmount() == 0) return Collections.emptyList();

        List<Joke> jokes = apiMessage.getJokes();

        List<JokeMessage> jokeMessages = new ArrayList<>();

        for ( Joke joke : jokes ) {
            JokeMessage jokeMsg = new JokeMessage();

            jokeMsg.setId( String.valueOf(Objects.hash(joke.getJoke())) );

            jokeMsg.setStatements(Arrays.asList(
                    joke.getJoke().split("(?<=[.!?])\\s+")
            ));

            joke.getFlags().forEach((key, val) -> {
                if (val) jokeMsg.addTag(key);
            });

            jokeMsg.setCategory(joke.getCategory());

            jokeMessages.add(jokeMsg);
        }

        return jokeMessages;
    }
}
