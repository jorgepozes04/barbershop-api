package com.jorgepozes04.barbershop_api.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

@DisplayName("Exception Classes Tests")
class ExceptionClassesTest {

    @Test
    @DisplayName("Should create ValidationException with message")
    void testValidationException() {
        // Act
        ValidationException exception = new ValidationException("Test error message");

        // Assert
        assertNotNull(exception);
        assertEquals("Test error message", exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should create ConflictException with message")
    void testConflictException() {
        // Act
        ConflictException exception = new ConflictException("Conflict error message");

        // Assert
        assertNotNull(exception);
        assertEquals("Conflict error message", exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should create ResourceNotFoundException with message")
    void testResourceNotFoundException() {
        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException("Not found error message");

        // Assert
        assertNotNull(exception);
        assertEquals("Not found error message", exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should create UnauthorizedException with message")
    void testUnauthorizedException() {
        // Act
        UnauthorizedException exception = new UnauthorizedException("Unauthorized error message");

        // Assert
        assertNotNull(exception);
        assertEquals("Unauthorized error message", exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should create BadRequestException with message")
    void testBadRequestException() {
        // Act
        BadRequestException exception = new BadRequestException("Bad request error message");

        // Assert
        assertNotNull(exception);
        assertEquals("Bad request error message", exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should create ErrorResponse with status and message")
    void testErrorResponse() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.of(2026, 03, 07, 10, 00, 00);
        String message = "Error occurred";
        int status = 400;
        String error = "Bad Request";
        String path = "/api/test";

        // Act
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(timestamp);
        errorResponse.setMessage(message);
        errorResponse.setStatus(status);
        errorResponse.setError(error);
        errorResponse.setPath(path);

        // Assert
        assertNotNull(errorResponse);
        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(status, errorResponse.getStatus());
        assertEquals(error, errorResponse.getError());
        assertEquals(path, errorResponse.getPath());
    }
}
