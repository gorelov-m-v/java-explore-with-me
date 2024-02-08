package ru.practicum.server.exception;

public class ValidationRequestException extends RuntimeException {
    public ValidationRequestException(String message) {
        super(message);
    }
}