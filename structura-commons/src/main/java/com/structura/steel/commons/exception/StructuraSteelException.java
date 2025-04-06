package com.structura.steel.commons.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StructuraSteelException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public StructuraSteelException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public StructuraSteelException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
