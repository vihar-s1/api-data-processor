package com.VersatileDataProcessor.DataConsumer.models.apiMessages;

import com.VersatileDataProcessor.DataConsumer.deserializers.ApiMessageInterfaceDeserializer;
import com.VersatileDataProcessor.DataConsumer.models.MessageType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

@JsonDeserialize(using = ApiMessageInterfaceDeserializer.class)
public interface ApiMessageInterface extends Serializable {
    String getId();
    void setId(String Id);

    MessageType getMessageType();
}
