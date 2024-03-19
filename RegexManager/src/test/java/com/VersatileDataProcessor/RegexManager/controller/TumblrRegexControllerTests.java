package com.VersatileDataProcessor.RegexManager.controller;


import com.VersatileDataProcessor.RegexManager.models.MyResponseBody;
import com.VersatileDataProcessor.RegexManager.models.TumblrPattern;
import com.VersatileDataProcessor.RegexManager.repository.TumblrPatternRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TumblrRegexControllerTests {

    @Mock
    private TumblrPatternRepository tumblrPatternRepository;

    @InjectMocks
    private TumblrRegexController tumblrRegexController;

    @Test
    void testGetAllRegexPatterns() {
        // Arrange
        List<TumblrPattern> expectedPatterns = Arrays.asList(
                new TumblrPattern("Pattern1"),
                new TumblrPattern("Pattern2")
        );

        when(tumblrPatternRepository.findAll()).thenReturn(expectedPatterns);

        // Act
        List<TumblrPattern> actualPatterns = tumblrRegexController.getAllRegexPatterns();

        // Assert
        assertEquals(expectedPatterns, actualPatterns);
    }

    @Test
    void testGetAllRegexPatterns_EmptyList() {
        // Arrange
        when(tumblrPatternRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<TumblrPattern> actualPatterns = tumblrRegexController.getAllRegexPatterns();

        // Assert
        assertTrue(actualPatterns.isEmpty());
    }

    @Test
    void testGetAllRegexPatterns_ExceptionHandling() {
        // Arrange
        when(tumblrPatternRepository.findAll()).thenThrow(new RuntimeException("Simulated exception"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tumblrRegexController.getAllRegexPatterns());
    }

    @Test
    void testAddRegexExpression_Success() {
        // Arrange
        TumblrPattern pattern = new TumblrPattern();
        pattern.setExpression("Pattern1");

        when(tumblrPatternRepository.findPatternByExpression(pattern.getExpression())).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = tumblrRegexController.addRegexExpression(pattern);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertInstanceOf(MyResponseBody.class, responseEntity.getBody());
        assertEquals("Resource Created Successfully", ((MyResponseBody<?>) responseEntity.getBody()).getMessage());
    }

    @Test
    void testAddRegexExpression_MinExpressionLength() {
        // Arrange
        TumblrPattern pattern = new TumblrPattern(); // Empty expression

        // Act
        ResponseEntity<Object> responseEntity = tumblrRegexController.addRegexExpression(pattern);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertInstanceOf(MyResponseBody.class, responseEntity.getBody());
        assertEquals("Validation failed. Expression cannot be null or empty.", ((MyResponseBody<?>) responseEntity.getBody()).getMessage());
    }

    @Test
    void testAddRegexExpression_MaxExpressionLength() {
        // Arrange
        TumblrPattern pattern = new TumblrPattern("a".repeat(TumblrPattern.MAX_EXPRESSION_LENGTH)); // Maximum length expression

        // Act
        ResponseEntity<Object> responseEntity = tumblrRegexController.addRegexExpression(pattern);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertInstanceOf(MyResponseBody.class, responseEntity.getBody());
        assertEquals("Resource Created Successfully", ((MyResponseBody<?>) responseEntity.getBody()).getMessage());
    }

    @Test
    void testAddRegexExpression_Conflict() {
        // Arrange
        TumblrPattern pattern = new TumblrPattern("Pattern1");

        when(tumblrPatternRepository.findPatternByExpression(pattern.getExpression())).thenReturn(pattern);

        // Act
        ResponseEntity<Object> responseEntity = tumblrRegexController.addRegexExpression(pattern);

        // Assert
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertInstanceOf(MyResponseBody.class, responseEntity.getBody());
        assertEquals("The resource you are trying to create already exists", ((MyResponseBody<?>) responseEntity.getBody()).getMessage());
    }

    @Test
    void testAddRegexExpression_ValidationFailure() {
        // Arrange
        TumblrPattern pattern = new TumblrPattern(); // Invalid pattern without expression

        // Act
        ResponseEntity<Object> responseEntity = tumblrRegexController.addRegexExpression(pattern);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertInstanceOf(MyResponseBody.class, responseEntity.getBody());
        assertEquals("Validation failed. Expression cannot be null or empty.", ((MyResponseBody<?>) responseEntity.getBody()).getMessage());
    }

    @Test
    void testAddRegexExpression_ValidationFailure_NullExpression() {
        // Arrange
        TumblrPattern pattern = new TumblrPattern(); // Invalid pattern without expression

        // Act
        ResponseEntity<Object> responseEntity = tumblrRegexController.addRegexExpression(pattern);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertInstanceOf(MyResponseBody.class, responseEntity.getBody());
        assertEquals("Validation failed. Expression cannot be null or empty.", ((MyResponseBody<?>) responseEntity.getBody()).getMessage());
    }

    @Test
    void testDeleteRegexExpression_Success() {
        // Arrange
        TumblrPattern pattern = new TumblrPattern();
        pattern.set_id("1");
        pattern.setExpression("Pattern1");

        when(tumblrPatternRepository.findById(pattern.get_id())).thenReturn(Optional.of(pattern));

        // Act
        ResponseEntity<Object> responseEntity = tumblrRegexController.deleteRegexExpression(pattern.get_id());

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertFalse(responseEntity.hasBody());
        verify(tumblrPatternRepository, times(1)).delete(pattern);
    }

    @Test
    void testDeleteRegexExpression_Success_Variant() {
        // Arrange
        String patternId = "123";
        TumblrPattern patternToDelete = new TumblrPattern();
        patternToDelete.set_id(patternId);
        patternToDelete.setExpression("PatternToDelete");

        when(tumblrPatternRepository.findById(patternId)).thenReturn(Optional.of(patternToDelete));
        doNothing().when(tumblrPatternRepository).delete(patternToDelete);

        // Act
        ResponseEntity<Object> responseEntity = tumblrRegexController.deleteRegexExpression(patternId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertFalse(responseEntity.hasBody());
        verify(tumblrPatternRepository, times(1)).delete(patternToDelete);
    }

    @Test
    void testDeleteRegexExpression_NotFound() {
        // Arrange
        String patternId = "1";

        when(tumblrPatternRepository.findById(patternId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> responseEntity = tumblrRegexController.deleteRegexExpression(patternId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertInstanceOf(MyResponseBody.class, responseEntity.getBody());
        assertEquals("The Resource you are trying to delete does not exist", ((MyResponseBody<?>) responseEntity.getBody()).getMessage());
    }

    @Test
    void testDeleteRegexExpression_NonexistentPattern() {
        // Arrange
        String nonExistentPatternId = "123";
        when(tumblrPatternRepository.findById(nonExistentPatternId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> responseEntity = tumblrRegexController.deleteRegexExpression(nonExistentPatternId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertInstanceOf(MyResponseBody.class, responseEntity.getBody());
        assertEquals("The Resource you are trying to delete does not exist", ((MyResponseBody<?>) responseEntity.getBody()).getMessage());
    }

    @Test
    void testDeleteRegexExpression_NullPatternId() {
        // Act
        ResponseEntity<Object> responseEntity = tumblrRegexController.deleteRegexExpression(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertInstanceOf(MyResponseBody.class, responseEntity.getBody());
        assertEquals("Validation failed. Pattern ID cannot be null or empty.", ((MyResponseBody<?>) responseEntity.getBody()).getMessage());
    }

    @Test
    void testDeleteRegexExpression_EmptyPatternId() {
        // Act
        ResponseEntity<Object> responseEntity = tumblrRegexController.deleteRegexExpression("");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertInstanceOf(MyResponseBody.class, responseEntity.getBody());
        assertEquals("Validation failed. Pattern ID cannot be null or empty.", ((MyResponseBody<?>) responseEntity.getBody()).getMessage());
    }
}
