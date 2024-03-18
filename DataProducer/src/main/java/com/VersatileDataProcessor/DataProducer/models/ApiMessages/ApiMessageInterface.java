package com.VersatileDataProcessor.DataProducer.models.ApiMessages;

import com.VersatileDataProcessor.DataProducer.models.MessageType;

import java.io.Serializable;

public interface ApiMessageInterface extends Serializable {
    String Id = null;
    public String getId();
    public void setId(String Id);

    public MessageType getMessageType();
}
