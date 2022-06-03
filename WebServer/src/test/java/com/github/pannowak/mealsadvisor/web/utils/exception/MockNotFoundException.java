package com.github.pannowak.mealsadvisor.web.utils.exception;

import com.github.pannowak.mealsadvisor.api.exception.EntityNotFoundException;

public final class MockNotFoundException extends EntityNotFoundException {

    private final String message;
    private final String localizedMessage;

    public MockNotFoundException(String message, String localizedMessage) {
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
