package com.VersatileDataProcessor.ElasticsearchWriter.controller;

import com.VersatileDataProcessor.ElasticsearchWriter.models.MyResponseBody;
import com.VersatileDataProcessor.ElasticsearchWriter.models.processedMessages.MessageInterface;
import com.VersatileDataProcessor.ElasticsearchWriter.models.standardMessage.Adapter;
import com.VersatileDataProcessor.ElasticsearchWriter.models.standardMessage.StandardMessage;
import com.VersatileDataProcessor.ElasticsearchWriter.repositories.CentralRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ElasticsearchControllerTest {

    @Mock
    private CentralRepository centralRepository;

    @InjectMocks
    private ElasticsearchController elasticsearchController;

    @BeforeAll
    static void setUp() {
        mockStatic(Adapter.class);
    }

    @Test
    void testAddMessage_Success() {
        // Given
        MessageInterface message =  mock(MessageInterface.class);
        StandardMessage standardMessage = mock(StandardMessage.class);

        // When
        when(Adapter.genericAdapter(any(MessageInterface.class))).thenReturn(standardMessage);

        when(message.getId()).thenReturn("someId");

        when(centralRepository.findById(anyString())).thenReturn(java.util.Optional.empty());
        when(centralRepository.save(any(StandardMessage.class))).thenReturn(standardMessage);

        ResponseEntity<MyResponseBody<Object>> responseEntity = elasticsearchController.addMessage(message);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("The resource was created successfully", responseEntity.getBody().getMessage());
        assertTrue(responseEntity.getBody().getSuccess());

        // Verify that the messageRepository.save() method was called with the correct argument
        verify(centralRepository, times(1)).save(standardMessage);
    }

    @Test
    void testAddMessage_NullMessage() {
        // Given

        // When
        ResponseEntity<MyResponseBody<Object>> responseEntity = elasticsearchController.addMessage(null);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Validation Failed. Id cannot be empty or null", responseEntity.getBody().getMessage());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_NullId() {
        // Given
        MessageInterface message = mock(MessageInterface.class);

        // When
        when(message.getId()).thenReturn(null);
        ResponseEntity<MyResponseBody<Object>> responseEntity = elasticsearchController.addMessage(message);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Validation Failed. Id cannot be empty or null", responseEntity.getBody().getMessage());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_IdAlreadyExists() {
        // Given
        MessageInterface message = mock(MessageInterface.class);
        StandardMessage standardMessage = mock(StandardMessage.class);

        // When
        when(Adapter.genericAdapter(any(MessageInterface.class))).thenReturn(standardMessage);

        when(message.getId()).thenReturn("existingId");

        when(centralRepository.findById(anyString())).thenReturn(Optional.of(standardMessage));

        ResponseEntity<MyResponseBody<Object>> responseEntity = elasticsearchController.addMessage(message);

        // Then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("The resource you are trying to create already exists", responseEntity.getBody().getMessage());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_InternalServerError() {
        // Given
        MessageInterface message = mock(MessageInterface.class);
        StandardMessage standardMessage = mock(StandardMessage.class);


        // When
        when(Adapter.genericAdapter(any(MessageInterface.class))).thenReturn(standardMessage);

        when(message.getId()).thenReturn("someId");
        when(message.getId()).thenReturn("someId");
        when(centralRepository.findById(anyString())).thenThrow(RuntimeException.class); // Simulating an internal error
        ResponseEntity<MyResponseBody<Object>> responseEntity = elasticsearchController.addMessage(message);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Internal Server Error !", responseEntity.getBody().getMessage());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_MessageWithEmptyId() {
        // Given
        MessageInterface message = mock(MessageInterface.class);

        // When
        when(message.getId()).thenReturn("");
        ResponseEntity<MyResponseBody<Object>> responseEntity = elasticsearchController.addMessage(message);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Validation Failed. Id cannot be empty or null", responseEntity.getBody().getMessage());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_MessageWithWhitespaceId() {
        // Given
        MessageInterface message = mock(MessageInterface.class);
        when(message.getId()).thenReturn("   ");

        // When
        ResponseEntity<MyResponseBody<Object>> responseEntity = elasticsearchController.addMessage(message);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Validation Failed. Id cannot be empty or null", responseEntity.getBody().getMessage());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_MessageWithExistingId() {
        // Given
        MessageInterface message = mock(MessageInterface.class);
        StandardMessage standardMessage = mock(StandardMessage.class);

        when(message.getId()).thenReturn("existingId");
        when(centralRepository.findById(anyString())).thenReturn(Optional.of(standardMessage));

        // When
        ResponseEntity<MyResponseBody<Object>> responseEntity = elasticsearchController.addMessage(message);

        // Then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("The resource you are trying to create already exists", responseEntity.getBody().getMessage());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_ExceptionDuringSaving() {
        // Given
        MessageInterface message = mock(MessageInterface.class);
        StandardMessage standardMessage = mock(StandardMessage.class);

        when(message.getId()).thenReturn("someId");
        when(standardMessage.getId()).thenReturn("someId");
        when(centralRepository.save(standardMessage)).thenThrow(new RuntimeException("Failed to save"));

        // When
        ResponseEntity<MyResponseBody<Object>> responseEntity = elasticsearchController.addMessage(message);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Internal Server Error !", responseEntity.getBody().getMessage());
        assertFalse(responseEntity.getBody().getSuccess());
    }
}