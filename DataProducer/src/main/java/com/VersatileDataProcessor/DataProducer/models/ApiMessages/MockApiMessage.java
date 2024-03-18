package com.VersatileDataProcessor.DataProducer.models.ApiMessages;
import com.VersatileDataProcessor.DataProducer.models.MessageType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MockApiMessage implements ApiMessageInterface {
    private String id;
    private String mockData;
    @Getter
    private  final MessageType messageType = MessageType.MOCK;
}
