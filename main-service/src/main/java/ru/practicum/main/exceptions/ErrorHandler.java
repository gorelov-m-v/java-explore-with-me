package ru.practicum.main.exceptions;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.main.constants.Pattern;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Pattern.DATE);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleWrongTimeOfEventException(final WrongTimeException exception) {
        return new ApiError(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseBody
    public ApiError handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception) {
        return new ApiError(exception.getMessage(), "Incorrectly made request.",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        return new ApiError(exception.getMessage(), "Incorrectly made request.",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleEmptyResultDataAccessException(final EmptyResultDataAccessException exception) {
        return new ApiError("It is impossible to do the operation", "data not found",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(), LocalDateTime.now().format(dateFormatter));
    }
}