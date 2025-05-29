package com.github.pannowak.mealsadvisor.api.exception;

public abstract class InvalidInputException extends ClientException {

    protected InvalidInputException(String message) {
        super(message);
    }
}
