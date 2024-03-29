package com.versatileDataProcessor.elasticsearchWriter.deserializers;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.versatileDataProcessor.elasticsearchWriter.models.MessageType;
import com.versatileDataProcessor.elasticsearchWriter.models.processedMessages.JokeMessage;
import com.versatileDataProcessor.elasticsearchWriter.models.processedMessages.MessageInterface;
import com.versatileDataProcessor.elasticsearchWriter.models.processedMessages.MockMessage;
import com.versatileDataProcessor.elasticsearchWriter.models.processedMessages.RandomUserMessage;

import java.io.IOException;

public class MessageInterfaceDeserializer extends JsonDeserializer<MessageInterface> {
    @Override
    public MessageInterface deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper.readTree(jsonParser);

        // currently only one type is there
        String messageType = root.get("messageType").asText();

        return switch (MessageType.valueOf(messageType)) {
            case JOKE -> mapper.readValue(root.toString(), JokeMessage.class);
            case RANDOM_USER -> mapper.readValue(root.toString(), RandomUserMessage.class);
            case MOCK -> mapper.readValue(root.toString(), MockMessage.class);
        };
    }
}
