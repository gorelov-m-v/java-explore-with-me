package ru.practicum.main.exceptions;

import org.springframework.dao.DataAccessException;

public class ConflictException extends DataAccessException {
    public ConflictException(String msg) {
        super(msg);
    }
}
