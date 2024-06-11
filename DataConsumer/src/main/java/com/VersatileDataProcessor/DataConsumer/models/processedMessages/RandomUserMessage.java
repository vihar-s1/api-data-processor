package com.VersatileDataProcessor.DataConsumer.models.processedMessages;

import com.VersatileDataProcessor.DataConsumer.models.MessageType;
import com.VersatileDataProcessor.DataConsumer.models.apiMessages.RandomUserApiMessage;
import com.VersatileDataProcessor.DataConsumer.models.messageSupport.randomUser.ID;
import com.VersatileDataProcessor.DataConsumer.models.messageSupport.randomUser.Name;
import com.VersatileDataProcessor.DataConsumer.models.messageSupport.randomUser.RandomUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter @Setter
@ToString
public class RandomUserMessage implements ProcessedMessageInterface{
    private String id;
    @Getter
    private MessageType messageType = MessageType.RANDOM_USER;

    private String gender,email, dob, registrationDate, phone, cell, picture;
    private Name name;
    private ID userId;

    private RandomUserMessage() {}

    public static List<RandomUserMessage> processApiMessage(RandomUserApiMessage apiMessage){
        if (apiMessage == null || apiMessage.getId() == null) return Collections.emptyList();

        List<RandomUserMessage> messages = new ArrayList<>();

        for (RandomUser randomUser : apiMessage.getResults()){
            RandomUserMessage message = new RandomUserMessage();

            message.setId( String.valueOf(Objects.hash(randomUser)) );
            message.setGender(randomUser.getGender());
            message.setEmail(randomUser.getEmail());
            message.setDob(randomUser.getDob().getDate());
            message.setRegistrationDate(randomUser.getRegistered().getDate());
            message.setPhone(randomUser.getPhone());
            message.setCell(randomUser.getCell());
            message.setName(randomUser.getName());
            message.setPicture(randomUser.getPicture().getLarge()); // no need to store all
            message.setUserId(randomUser.getId());

            messages.add(message);
        }

        return messages;
    }
}
