package com.VersatileDataProcessor.DataConsumer.models.processedMessages;

import com.VersatileDataProcessor.DataConsumer.models.MessageType;

import java.io.Serializable;

public interface ProcessedMessageInterface extends Serializable {
    String getId();
    void setId(String Id);

    MessageType getMessageType();
}
