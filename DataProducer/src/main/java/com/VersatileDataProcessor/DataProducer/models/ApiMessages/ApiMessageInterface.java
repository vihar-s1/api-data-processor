package com.VersatileDataProcessor.DataProducer.models.ApiMessages;

import com.VersatileDataProcessor.DataProducer.models.MessageType;

import java.io.Serializable;

public interface ApiMessageInterface extends Serializable {
    String getId();
    void setId(String Id);

    MessageType getMessageType();
}
