package com.VersatileDataProcessor.Models.models.apiResponse;

import com.VersatileDataProcessor.Models.MessageType;

import java.io.Serializable;

public interface ApiResponseInterface extends Serializable {
    String getId();
    void setId(String id);

    MessageType getMessageType();
}
