package com.VersatileDataProcessor.DataConsumer.models.ApiMessages;

import com.VersatileDataProcessor.DataConsumer.models.MessageType;
import com.VersatileDataProcessor.DataConsumer.models.StandardApiMessage;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

@JsonDeserialize(using = ApiMessageInterfaceDeserializer.class)
public interface ApiMessageInterface extends Serializable {
    public String getId();
    public void setId(String Id);

    public StandardApiMessage toStandardApiMessage();

    public MessageType getMessageType();
}
