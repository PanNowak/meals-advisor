package com.github.pannowak.mealsadvisor.web.exception;

import com.github.pannowak.mealsadvisor.api.exception.MealsAdvisorException;

import java.net.ConnectException;

final class ConnectionException extends MealsAdvisorException {

    private final String message;
    private final String localizedMessage;

    ConnectionException(String message, String localizedMessage, ConnectException cause) {
        this.message = message;
        this.localizedMessage = localizedMessage;
        initCause(cause);
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
