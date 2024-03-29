package com.versatileDataProcessor.dataConsumer.models.processedMessages;

import com.versatileDataProcessor.dataConsumer.models.MessageType;

import java.io.Serializable;

public interface ProcessedMessageInterface extends Serializable {
    String getId();
    void setId(String Id);

    MessageType getMessageType();
}
