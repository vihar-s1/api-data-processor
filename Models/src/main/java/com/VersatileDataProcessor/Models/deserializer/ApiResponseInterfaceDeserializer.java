package com.VersatileDataProcessor.Models.deserializer;

import com.VersatileDataProcessor.Models.ApiType;
import com.VersatileDataProcessor.Models.apiResponse.ApiResponseInterface;
import com.VersatileDataProcessor.Models.apiResponse.joke.JokeApiResponse;
import com.VersatileDataProcessor.Models.apiResponse.randomUser.RandomUserApiResponse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class ApiResponseInterfaceDeserializer extends JsonDeserializer<ApiResponseInterface> {
    @Override
    public ApiResponseInterface deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper.readTree(jsonParser);

        String messageType = root.get("messageType").asText();

        return switch (ApiType.valueOf(messageType)) {
            case JOKE -> {
                if (root.get("jokes") == null) {
                    throw new IllegalStateException("field jokes cannot be null when messageType = " + messageType);
                }
                yield mapper.readValue(root.toString(), JokeApiResponse.class);
            }
            case RANDOM_USER -> mapper.readValue(root.toString(), RandomUserApiResponse.class);
        };
    }
}
