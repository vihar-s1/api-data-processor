package com.versatileDataProcessor.dataProducer.models.apiMessages;
import com.versatileDataProcessor.dataProducer.models.MessageType;
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
