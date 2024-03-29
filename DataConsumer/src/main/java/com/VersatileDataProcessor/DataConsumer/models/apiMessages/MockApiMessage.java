package com.versatileDataProcessor.dataConsumer.models.apiMessages;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.versatileDataProcessor.dataConsumer.models.MessageType;
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
