package com.github.pannowak.mealsadvisor.web.exception;

import com.github.pannowak.mealsadvisor.api.exception.ServiceException;

final class WebServiceException extends ServiceException {

    private final String message;
    private final String localizedMessage;

    WebServiceException(String message, String localizedMessage) {
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
