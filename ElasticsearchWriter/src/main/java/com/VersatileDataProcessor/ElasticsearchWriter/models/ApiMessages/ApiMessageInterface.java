package com.VersatileDataProcessor.ElasticsearchWriter.models.ApiMessages;

import com.VersatileDataProcessor.ElasticsearchWriter.deserializers.ApiMessageInterfaceDeserializer;
import com.VersatileDataProcessor.ElasticsearchWriter.models.MessageType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;


@JsonDeserialize(using = ApiMessageInterfaceDeserializer.class)
@Document(indexName = "api_data")
public interface ApiMessageInterface extends Serializable {
    public String getId();
    public void setId(String Id);

    public MessageType getMessageType();
}
