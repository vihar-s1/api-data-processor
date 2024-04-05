package com.versatileDataProcessor.elasticsearchWriter.controller;

import com.versatileDataProcessor.elasticsearchWriter.models.MyResponseBody;
import com.versatileDataProcessor.elasticsearchWriter.models.processedMessages.MessageInterface;
import com.versatileDataProcessor.elasticsearchWriter.repositories.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ElasticsearchControllerTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private ElasticsearchController elasticsearchController;

    @Test
    void testAddMessage_Success() {
        // Given
        MessageInterface message = mock(MessageInterface.class);
        when(message.getId()).thenReturn("someId");
        when(messageRepository.findById(anyString())).thenReturn(java.util.Optional.empty());
        when(messageRepository.save(message)).thenReturn(message);

        // When
        ResponseEntity<MyResponseBody<Object>> responseEntity = elasticsearchController.addMessage(message);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("The resource was created successfully", responseEntity.getBody().getMessage());
        assertTrue(responseEntity.getBody().getSuccess());

        // Verify that the messageRepository.save() method was called with the correct argument
        verify(messageRepository, times(1)).save(message);
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
        when(message.getId()).thenReturn(null);

        // When
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
        when(message.getId()).thenReturn("existingId");
        when(messageRepository.findById(anyString())).thenReturn(Optional.of(message));

        // When
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
        when(message.getId()).thenReturn("someId");
        when(messageRepository.findById(anyString())).thenThrow(RuntimeException.class); // Simulating an internal error

        // When
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
        when(message.getId()).thenReturn("");

        // When
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
        String existingId = "existingId";
        MessageInterface message = mock(MessageInterface.class);
        when(message.getId()).thenReturn(existingId);
        when(messageRepository.findById(existingId)).thenReturn(Optional.of(message));

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
        when(message.getId()).thenReturn("someId");
        when(messageRepository.save(message)).thenThrow(new RuntimeException("Failed to save"));

        // When
        ResponseEntity<MyResponseBody<Object>> responseEntity = elasticsearchController.addMessage(message);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Internal Server Error !", responseEntity.getBody().getMessage());
        assertFalse(responseEntity.getBody().getSuccess());
    }
}