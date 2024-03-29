package com.versatileDataProcessor.dataConsumer.deserializers;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.versatileDataProcessor.dataConsumer.models.MessageType;
import com.versatileDataProcessor.dataConsumer.models.apiMessages.ApiMessageInterface;
import com.versatileDataProcessor.dataConsumer.models.apiMessages.JokeApiMessage;
import com.versatileDataProcessor.dataConsumer.models.apiMessages.MockApiMessage;
import com.versatileDataProcessor.dataConsumer.models.apiMessages.RandomUserApiMessage;

import java.io.IOException;

public class ApiMessageInterfaceDeserializer extends JsonDeserializer<ApiMessageInterface> {
    @Override
    public ApiMessageInterface deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper. readTree(jsonParser);

        // currently only one type is there
        String messageType = root.get("messageType").asText();

        return switch (MessageType.valueOf(messageType)) {
            case JOKE -> mapper.readValue(root.toString(), JokeApiMessage.class);
            case RANDOM_USER -> mapper.readValue(root.toString(), RandomUserApiMessage.class);
            case MOCK -> mapper.readValue(root.toString(), MockApiMessage.class);
        };
    }
}
