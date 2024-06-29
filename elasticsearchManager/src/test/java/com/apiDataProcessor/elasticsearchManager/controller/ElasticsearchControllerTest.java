package com.apiDataProcessor.elasticsearchManager.controller;

import com.apiDataProcessor.elasticsearchManager.repository.ChannelPostRepository;
import com.apiDataProcessor.models.InternalResponse;
import com.apiDataProcessor.models.genericChannelPost.GenericChannelPost;
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
    private ChannelPostRepository centralRepository;

    @InjectMocks
    private ElasticsearchController elasticsearchController;

    @Test
    void testAddMessage_Success() {
        // Given
        GenericChannelPost channelPost =  mock(GenericChannelPost.class);

        // When
        when(channelPost.getId()).thenReturn("someId");
        when(centralRepository.findById(anyString())).thenReturn(java.util.Optional.empty());
        when(centralRepository.save(any(GenericChannelPost.class))).thenReturn(channelPost);

        ResponseEntity<InternalResponse<?>> responseEntity = elasticsearchController.addMessage(channelPost);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(channelPost, responseEntity.getBody().getData());
        assertTrue(responseEntity.getBody().getSuccess());

        // Verify that the messageRepository.save() method was called with the correct argument
        verify(centralRepository, times(1)).save(channelPost);
    }

    @Test
    void testAddMessage_NullMessage() {
        // Given

        // When
        ResponseEntity<InternalResponse<?>> responseEntity = elasticsearchController.addMessage(null);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Validation Failed. Id cannot be empty or null", responseEntity.getBody().getData());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_NullId() {
        // Given
        GenericChannelPost channelPost = mock(GenericChannelPost.class);

        // When
        when(channelPost.getId()).thenReturn(null);
        ResponseEntity<InternalResponse<?>> responseEntity = elasticsearchController.addMessage(channelPost);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Validation Failed. Id cannot be empty or null", responseEntity.getBody().getData());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_IdAlreadyExists() {
        // Given
        GenericChannelPost channelPost = mock(GenericChannelPost.class);

        // When
        when(channelPost.getId()).thenReturn("existingId");
        when(centralRepository.findById(anyString())).thenReturn(Optional.of(channelPost));

        ResponseEntity<InternalResponse<?>> responseEntity = elasticsearchController.addMessage(channelPost);

        // Then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Resource Already Exists", responseEntity.getBody().getData());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_InternalServerError() {
        // Given
        GenericChannelPost channelPost = mock(GenericChannelPost.class);


        // When
        when(channelPost.getId()).thenReturn("someId");
        when(channelPost.getId()).thenReturn("someId");
        when(centralRepository.findById(anyString())).thenThrow(RuntimeException.class); // Simulating an internal error
        ResponseEntity<InternalResponse<?>> responseEntity = elasticsearchController.addMessage(channelPost);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Internal Server Error !", responseEntity.getBody().getData());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_MessageWithEmptyId() {
        // Given
        GenericChannelPost channelPost = mock(GenericChannelPost.class);

        // When
        when(channelPost.getId()).thenReturn("");
        ResponseEntity<InternalResponse<?>> responseEntity = elasticsearchController.addMessage(channelPost);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Validation Failed. Id cannot be empty or null", responseEntity.getBody().getData());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_MessageWithWhitespaceId() {
        // Given
        GenericChannelPost channelPost = mock(GenericChannelPost.class);
        when(channelPost.getId()).thenReturn("   ");

        // When
        ResponseEntity<InternalResponse<?>> responseEntity = elasticsearchController.addMessage(channelPost);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Validation Failed. Id cannot be empty or null", responseEntity.getBody().getData());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_MessageWithExistingId() {
        // Given
        GenericChannelPost channelPost = mock(GenericChannelPost.class);


        // When
        when(channelPost.getId()).thenReturn("existingId");
        when(centralRepository.findById(anyString())).thenReturn(Optional.of(channelPost));
        ResponseEntity<InternalResponse<?>> responseEntity = elasticsearchController.addMessage(channelPost);

        // Then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Resource Already Exists", responseEntity.getBody().getData());
        assertFalse(responseEntity.getBody().getSuccess());
    }

    @Test
    void testAddMessage_ExceptionDuringSaving() {
        // Given
        GenericChannelPost channelPost = mock(GenericChannelPost.class);

        when(channelPost.getId()).thenReturn("someId");
        when(channelPost.getId()).thenReturn("someId");
        when(centralRepository.save(channelPost)).thenThrow(new RuntimeException("Failed to save"));

        // When
        ResponseEntity<InternalResponse<?>> responseEntity = elasticsearchController.addMessage(channelPost);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Internal Server Error !", responseEntity.getBody().getData());
        assertFalse(responseEntity.getBody().getSuccess());
    }
}