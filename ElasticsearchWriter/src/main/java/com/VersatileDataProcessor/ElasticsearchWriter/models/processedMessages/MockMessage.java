package com.versatileDataProcessor.elasticsearchWriter.models.processedMessages;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.versatileDataProcessor.elasticsearchWriter.models.MessageType;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.stereotype.Component;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Component
@JsonDeserialize(as = MockMessage.class)
@Document(indexName = "mock_messages")
@TypeAlias("MockMessage")
public class MockMessage implements MessageInterface {
    private String  id;
    @Getter
    private MessageType messageType = MessageType.MOCK;

    private String mockData;
}
