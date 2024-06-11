package com.VersatileDataProcessor.ElasticsearchWriter.controller;

import com.VersatileDataProcessor.ElasticsearchWriter.repositories.MediaDataRepository;
import com.VersatileDataProcessor.Models.InternalHttpResponse;
import com.VersatileDataProcessor.Models.standardMediaData.StandardMediaData;
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
    private MediaDataRepository centralRepository;

    @InjectMocks
    private ElasticsearchController elasticsearchController;

    @Test
    void testAddMessage_Success() {
        // Given
        StandardMediaData mediaData =  mock(StandardMediaData.class);

        // When
        when(mediaData.getId()).thenReturn("someId");
        when(centralRepository.findById(anyString())).thenReturn(java.util.Optional.empty());
        when(centralRepository.save(any(StandardMediaData.class))).thenReturn(mediaData);

        ResponseEntity<InternalHttpResponse<?>> responseEntity = elasticsearchController.addMessage(mediaData);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(mediaData, responseEntity.getBody().getData());
        assertTrue(responseEntity.getBody().getSuccess());

        // Verify that the messageRepository.save() method was called with the correct argument
        verify(centralRepository, times(1)).save(mediaData);
    }

    @Test
    void testAddMessage_NullMessage() {
        // Given

        // When
        ResponseEntity<InternalHttpResponse<?>> responseEntity = elasticsearchController.addMessage(null);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Validation Failed. Id cannot be empty or null", responseEntity.getBody().getData());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_NullId() {
        // Given
        StandardMediaData mediaData = mock(StandardMediaData.class);

        // When
        when(mediaData.getId()).thenReturn(null);
        ResponseEntity<InternalHttpResponse<?>> responseEntity = elasticsearchController.addMessage(mediaData);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Validation Failed. Id cannot be empty or null", responseEntity.getBody().getData());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_IdAlreadyExists() {
        // Given
        StandardMediaData mediaData = mock(StandardMediaData.class);

        // When
        when(mediaData.getId()).thenReturn("existingId");
        when(centralRepository.findById(anyString())).thenReturn(Optional.of(mediaData));

        ResponseEntity<InternalHttpResponse<?>> responseEntity = elasticsearchController.addMessage(mediaData);

        // Then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("The resource you are trying to create already exists", responseEntity.getBody().getData());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_InternalServerError() {
        // Given
        StandardMediaData mediaData = mock(StandardMediaData.class);


        // When
        when(mediaData.getId()).thenReturn("someId");
        when(mediaData.getId()).thenReturn("someId");
        when(centralRepository.findById(anyString())).thenThrow(RuntimeException.class); // Simulating an internal error
        ResponseEntity<InternalHttpResponse<?>> responseEntity = elasticsearchController.addMessage(mediaData);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Internal Server Error !", responseEntity.getBody().getData());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_MessageWithEmptyId() {
        // Given
        StandardMediaData mediaData = mock(StandardMediaData.class);

        // When
        when(mediaData.getId()).thenReturn("");
        ResponseEntity<InternalHttpResponse<?>> responseEntity = elasticsearchController.addMessage(mediaData);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Validation Failed. Id cannot be empty or null", responseEntity.getBody().getData());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_MessageWithWhitespaceId() {
        // Given
        StandardMediaData mediaData = mock(StandardMediaData.class);
        when(mediaData.getId()).thenReturn("   ");

        // When
        ResponseEntity<InternalHttpResponse<?>> responseEntity = elasticsearchController.addMessage(mediaData);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Validation Failed. Id cannot be empty or null", responseEntity.getBody().getData());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_MessageWithExistingId() {
        // Given
        StandardMediaData mediaData = mock(StandardMediaData.class);


        // When
        when(mediaData.getId()).thenReturn("existingId");
        when(centralRepository.findById(anyString())).thenReturn(Optional.of(mediaData));
        ResponseEntity<InternalHttpResponse<?>> responseEntity = elasticsearchController.addMessage(mediaData);

        // Then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("The resource you are trying to create already exists", responseEntity.getBody().getData());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_ExceptionDuringSaving() {
        // Given
        StandardMediaData mediaData = mock(StandardMediaData.class);

        when(mediaData.getId()).thenReturn("someId");
        when(mediaData.getId()).thenReturn("someId");
        when(centralRepository.save(mediaData)).thenThrow(new RuntimeException("Failed to save"));

        // When
        ResponseEntity<InternalHttpResponse<?>> responseEntity = elasticsearchController.addMessage(mediaData);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Internal Server Error !", responseEntity.getBody().getData());
        assertFalse(responseEntity.getBody().getSuccess());
    }
}