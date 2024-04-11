package com.versatileDataProcessor.elasticsearchWriter.models.standardMessage;

import com.versatileDataProcessor.elasticsearchWriter.models.processedMessages.JokeMessage;
import com.versatileDataProcessor.elasticsearchWriter.models.processedMessages.MessageInterface;
import com.versatileDataProcessor.elasticsearchWriter.models.processedMessages.RandomUserMessage;


public class Adapter {
    public static StandardMessage genericAdapter(MessageInterface messageInterface) {
        if (messageInterface == null) return null;

        return switch (messageInterface.getMessageType()){
            case JOKE -> toStandardMessage((JokeMessage) messageInterface);
            case RANDOM_USER -> toStandardMessage((RandomUserMessage) messageInterface);
            case null -> null;
        };
    }

    public static StandardMessage toStandardMessage(JokeMessage jokeMessage) {
        StandardMessage standardMessage = new StandardMessage();

        standardMessage.setId(jokeMessage.getId());
        standardMessage.setMessageType(jokeMessage.getMessageType());

        standardMessage.setTags(jokeMessage.getTags());
        standardMessage.setJokeStatements(jokeMessage.getStatements());

        standardMessage.addAdditional("category", jokeMessage.getCategory());

        return standardMessage;
    }

    public static StandardMessage toStandardMessage(RandomUserMessage randomUserMessage) {
        StandardMessage standardMessage = new StandardMessage();

        standardMessage.setId(randomUserMessage.getId());
        standardMessage.setMessageType(randomUserMessage.getMessageType());

        standardMessage.setName(randomUserMessage.getName());
        standardMessage.setUserId(randomUserMessage.getUserId());

//        gender,email, dob, registrationDate, phone, cell, picture
        standardMessage.addAdditional("gender", randomUserMessage.getGender());
        standardMessage.addAdditional("email", randomUserMessage.getEmail());
        standardMessage.addAdditional("dob", randomUserMessage.getDob());
        standardMessage.addAdditional("registrationDate", randomUserMessage.getRegistrationDate());
        standardMessage.addAdditional("phone", randomUserMessage.getPhone());
        standardMessage.addAdditional("cell", randomUserMessage.getCell());
        standardMessage.addAdditional("picture", randomUserMessage.getPicture());

        return standardMessage;
    }
}
