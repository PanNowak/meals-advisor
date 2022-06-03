package com.github.pannowak.mealsadvisor.core.exception;

import com.github.pannowak.mealsadvisor.api.exception.ClientException;

final class ValidationException extends ClientException {

    private final String message;
    private final String localizedMessage;

    ValidationException(String message, String localizedMessage, Throwable cause) {
        initCause(cause);
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
