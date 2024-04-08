package com.versatileDataProcessor.dataConsumer.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.versatileDataProcessor.dataConsumer.models.MessageType;
import com.versatileDataProcessor.dataConsumer.models.apiMessages.ApiMessageInterface;
import com.versatileDataProcessor.dataConsumer.models.apiMessages.JokeApiMessage;
import com.versatileDataProcessor.dataConsumer.models.messageSupport.Joke;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ApiMessageInterfaceDeserializerTest {

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule().addDeserializer(ApiMessageInterface.class, new ApiMessageInterfaceDeserializer()));
    }

    @Test
    void deserializer_testJokeApiMessage_success() throws IOException {
        // Arrange
        String json = "{\"messageType\": \"JOKE\", \"id\": \"123\", \"error\": false, \"amount\": 1, \"jokes\": [{\"category\": \"Misc\", \"type\": \"single\", \"joke\": \"This is a joke\", \"flags\": {}}]}";

        // Act
        ApiMessageInterface message = objectMapper.readValue(json, ApiMessageInterface.class);

        // Assert
        assertEquals(JokeApiMessage.class, message.getClass());
        assertEquals(MessageType.JOKE, message.getMessageType());
        JokeApiMessage jokeMessage = (JokeApiMessage) message;
        assertEquals("123", jokeMessage.getId());
        assertFalse(jokeMessage.isError());
        assertEquals(1, jokeMessage.getAmount());
        assertEquals(1, jokeMessage.getJokes().size());
        Joke joke = jokeMessage.getJokes().getFirst();
        assertEquals("Misc", joke.getCategory());
        assertEquals("single", joke.getType());
        assertEquals("This is a joke", joke.getJoke());
        assertEquals(Collections.emptyMap(), joke.getFlags());
    }

    @Test
    void deserializer_failure() {
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue("", ApiMessageInterface.class));
    }

    @Test
    void deserializer_null() {
        assertThrows(IllegalArgumentException.class, () -> objectMapper.readValue((JsonParser) null, ApiMessageInterface.class));
    }

    @Test
    void deserializer_missingMessageType() {
        String json = "{\"id\": \"123\", \"error\": false, \"amount\": 1, \"jokes\": [{\"category\": \"Misc\", \"type\": \"single\", \"joke\": \"This is a joke\", \"flags\": {}}]}";
        assertThrows(NullPointerException.class, () -> objectMapper.readValue(json, ApiMessageInterface.class));
    }

    @Test
    void deserializer_invalidMessageType() {
        String json = "{\"messageType\": \"INVALID\", \"id\": \"123\", \"error\": false, \"amount\": 1, \"jokes\": [{\"category\": \"Misc\", \"type\": \"single\", \"joke\": \"This is a joke\", \"flags\": {}}]}";
        assertThrows(IllegalArgumentException.class, () -> objectMapper.readValue(json, ApiMessageInterface.class));
    }

    @Test
    void deserializer_invalidJsonStructure() {
        String json = "{\"messageType\": \"JOKE\", \"id\": \"123\", \"error\": false, \"amount\": 1}";
        assertThrows(IllegalStateException.class, () -> objectMapper.readValue(json, ApiMessageInterface.class));
    }
}
