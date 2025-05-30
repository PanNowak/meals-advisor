package com.github.pannowak.mealsadvisor.web.utils.exception;

import com.github.pannowak.mealsadvisor.api.exception.ClientException;

public final class MockClientException extends ClientException {

    private final String message;
    private final String localizedMessage;

    public MockClientException(String message, String localizedMessage) {
        super(message); // Call super constructor
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
