package com.structura.steel.commons.exception;

import org.springframework.http.HttpStatus;

public class StructuraSteelException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public StructuraSteelException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public StructuraSteelException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
