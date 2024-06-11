package com.VersatileDataProcessor.ElasticsearchWriter.models.processedMessages;

import com.VersatileDataProcessor.ElasticsearchWriter.deserializers.MessageInterfaceDeserializer;
import com.VersatileDataProcessor.ElasticsearchWriter.models.MessageType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@JsonDeserialize(using = MessageInterfaceDeserializer.class)
@Document(indexName = "messages")
public interface MessageInterface extends Serializable {
    String getId();
    void setId(String Id);

    MessageType getMessageType();
}
