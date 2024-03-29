package com.versatileDataProcessor.dataConsumer.models.apiMessages;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.versatileDataProcessor.dataConsumer.deserializers.ApiMessageInterfaceDeserializer;
import com.versatileDataProcessor.dataConsumer.models.MessageType;

import java.io.Serializable;

@JsonDeserialize(using = ApiMessageInterfaceDeserializer.class)
public interface ApiMessageInterface extends Serializable {
    String getId();
    void setId(String Id);

    MessageType getMessageType();
}
