package com.versatileDataProcessor.elasticsearchWriter.models.processedMessages;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.versatileDataProcessor.elasticsearchWriter.deserializers.MessageInterfaceDeserializer;
import com.versatileDataProcessor.elasticsearchWriter.models.MessageType;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@JsonDeserialize(using = MessageInterfaceDeserializer.class)
@Document(indexName = "messages")
public interface MessageInterface extends Serializable {
    String getId();
    void setId(String Id);

    MessageType getMessageType();
}
