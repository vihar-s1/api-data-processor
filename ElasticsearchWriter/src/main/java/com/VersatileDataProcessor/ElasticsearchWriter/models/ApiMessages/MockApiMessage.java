package com.VersatileDataProcessor.ElasticsearchWriter.models.ApiMessages;

import com.VersatileDataProcessor.ElasticsearchWriter.models.MessageType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonDeserialize(as = MockApiMessage.class)
@Data
@Document(indexName = "mock_api_messages")
public class MockApiMessage implements ApiMessageInterface {
    @Id
    private String id;

    @Field(type = FieldType.Text, name = "mockData")
    private String mockData;

    @Getter
    @Field(type = FieldType.Keyword, name = "messageType")
    private  final MessageType messageType = MessageType.MOCK;
}
