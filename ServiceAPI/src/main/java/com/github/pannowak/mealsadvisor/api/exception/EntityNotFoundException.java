package com.github.pannowak.mealsadvisor.api.exception;

public abstract class EntityNotFoundException extends ClientException {

    protected EntityNotFoundException(String message) {
        super(message);
    }
}
