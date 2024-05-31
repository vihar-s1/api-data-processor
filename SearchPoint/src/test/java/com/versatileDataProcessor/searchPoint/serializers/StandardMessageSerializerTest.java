package com.versatileDataProcessor.searchPoint.serializers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.versatileDataProcessor.searchPoint.models.MessageType;
import com.versatileDataProcessor.searchPoint.models.StandardMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StandardMessageSerializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();

        objectMapper.registerModule( new SimpleModule().addSerializer(StandardMessage.class, new StandardMessageSerializer()) );
    }

    @Test
    public void serialize_success() throws JsonProcessingException {
        StandardMessage standardMessage = new StandardMessage();
        standardMessage.setId("someId");
        standardMessage.setMessageType(MessageType.RANDOM_USER);

        String expected = "{\"id\":\"someId\",\"messageType\":\"RANDOM_USER\"}";
        String actual = objectMapper.writeValueAsString(standardMessage);

        assertEquals(expected, actual);
    }

    @Test
    public void serialize_emptyMessage() throws JsonProcessingException {
        String actual = objectMapper.writeValueAsString(null);
        assertEquals("null", actual);
    }
}