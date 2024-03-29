package com.versatileDataProcessor.dataProducer.models.apiMessages;

import com.versatileDataProcessor.dataProducer.models.MessageType;

import java.io.Serializable;

public interface ApiMessageInterface extends Serializable {
    String getId();
    void setId(String Id);

    MessageType getMessageType();
}
