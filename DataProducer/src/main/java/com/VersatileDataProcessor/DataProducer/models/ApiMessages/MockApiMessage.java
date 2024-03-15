package com.VersatileDataProcessor.DataProducer.models.ApiMessages;
import com.VersatileDataProcessor.DataProducer.models.MessageType;
import com.VersatileDataProcessor.DataProducer.models.StandardApiMessage;
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

    @Override
    public StandardApiMessage toStandardApiMessage() {
        StandardApiMessage message = new StandardApiMessage();

        message.setData(this);
        message.setId(this.messageType.name() + "-" + this.id);

        return  message;
    }
}
