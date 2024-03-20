package com.VersatileDataProcessor.DataConsumer.models.processedMessages;

import com.VersatileDataProcessor.DataConsumer.models.MessageType;
import com.VersatileDataProcessor.DataConsumer.models.apiMessages.MockApiMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MockMessage implements ProcessedMessageInterface{
    private String  id;
    @Getter
    private MessageType messageType = MessageType.MOCK;

    private String mockData;

    private MockMessage() {}

    public static MockMessage processApiMessage(MockApiMessage apiMessage){
        if (apiMessage == null || apiMessage.getId() == null) return null;

        MockMessage mockMessage = new MockMessage();
        mockMessage.setId(apiMessage.getId());
        mockMessage.setMockData(apiMessage.getMockData());

        return mockMessage;
    }
}
