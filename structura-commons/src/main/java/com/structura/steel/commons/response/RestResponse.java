package com.structura.steel.commons.response;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public record RestResponse<T>(
        int status,
        T data
) implements Serializable {

    public static <T> RestResponse<T> ok(T data) {
        return new RestResponse<>(HttpStatus.OK.value(), data);
    }

    public static <T> RestResponse<T> created(T data) {
        return new RestResponse<>(HttpStatus.CREATED.value(), data);
    }

    public static <T> RestResponse<T> accepted(T data) {
        return new RestResponse<>(HttpStatus.ACCEPTED.value(), data);
    }

    public static <T> RestResponse<T> multiStatus(T data) {
        return new RestResponse<>(HttpStatus.MULTI_STATUS.value(), data);
    }

}
