package com.structura.steel.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceNotBelongToException extends RuntimeException {

    public ResourceNotBelongToException(String childResourceName,
                                        String childResourceField,
                                        Object childResourceId,
                                        String parentResourceName,
                                        String parentResourceField,
                                        Object parentResourceId) {
        super(String.format("%s with %s %s does not belong to %s %s %s!",
                childResourceName,
                childResourceField,
                childResourceId,
                parentResourceName,
                parentResourceField,
                parentResourceId));
    }

    public ResourceNotBelongToException(String message) {
        super(message);
    }
}
