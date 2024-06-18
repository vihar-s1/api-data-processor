package com.apiDataProcessor.models.deserializer;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.apiResponse.ApiResponseInterface;
import com.apiDataProcessor.models.apiResponse.joke.JokeApiResponse;
import com.apiDataProcessor.models.apiResponse.randomUser.RandomUserApiResponse;
import com.apiDataProcessor.models.apiResponse.twitter.TwitterApiResponse;
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
            case JOKE -> mapper.readValue(root.toString(), JokeApiResponse.class);
            case RANDOM_USER -> mapper.readValue(root.toString(), RandomUserApiResponse.class);
            case TWITTER -> mapper.readValue(root.toString(), TwitterApiResponse.class);
        };
    }
}
