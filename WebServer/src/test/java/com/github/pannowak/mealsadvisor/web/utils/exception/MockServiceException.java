package com.github.pannowak.mealsadvisor.web.utils.exception;

import com.github.pannowak.mealsadvisor.api.exception.ServiceException;

public final class MockServiceException extends ServiceException {

    private final String message;
    private final String localizedMessage;

    public MockServiceException(String message, String localizedMessage) {
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
