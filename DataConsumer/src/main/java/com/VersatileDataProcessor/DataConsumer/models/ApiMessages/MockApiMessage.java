package com.VersatileDataProcessor.DataConsumer.models.ApiMessages;
import com.VersatileDataProcessor.DataConsumer.models.MessageType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonDeserialize(as = MockApiMessage.class)
public class MockApiMessage implements ApiMessageInterface {
    private String id;
    private String mockData;
    @Getter
    private  final MessageType messageType = MessageType.MOCK;
}
