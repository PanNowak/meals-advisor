package com.github.pannowak.mealsadvisor.core.exception;

import com.github.pannowak.mealsadvisor.api.exception.InvalidInputException;

final class IllegalWeekDayException extends InvalidInputException {

    private final String message;
    private final String localizedMessage;

    IllegalWeekDayException(String message, String localizedMessage) {
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
