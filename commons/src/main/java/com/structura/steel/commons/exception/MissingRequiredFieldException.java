package com.structura.steel.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingRequiredFieldException extends RuntimeException {

    public MissingRequiredFieldException(String fieldName) {
        super(String.format("Field '%s' is required and cannot be null.", fieldName));
    }

    // Nếu cần truyền thêm thông tin, bạn có thể thêm constructor thứ hai:
    public MissingRequiredFieldException(String fieldName, String message) {
        super(String.format("Field '%s' error: %s", fieldName, message));
    }
}

