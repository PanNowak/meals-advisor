package com.github.pannowak.mealsadvisor.api.exception;

public class InsufficientDataException extends ClientException {

    public InsufficientDataException(String message) {
        super(message);
    }
}
