package com.github.pannowak.mealsadvisor.core.exception;

import com.github.pannowak.mealsadvisor.api.exception.EntityNotFoundException;

final class EntityNotFoundInDatabaseException extends EntityNotFoundException {

    private final String message;
    private final String localizedMessage;

    EntityNotFoundInDatabaseException(String message, String localizedMessage) {
        this.message = message;
        this.localizedMessage = localizedMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }
}
