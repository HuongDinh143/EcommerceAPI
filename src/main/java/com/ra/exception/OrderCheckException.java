package com.ra.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class OrderCheckException extends RuntimeException {
    private final Map<String, String> fieldErrors;

    public OrderCheckException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }

}

