package com.github.pannowak.mealsadvisor.api.exception;

public abstract class MealsAdvisorException extends RuntimeException {

    protected final String message; // Store the message

    protected MealsAdvisorException(String message) {
        super(message); // Pass to RuntimeException constructor
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message; // Return the stored message
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage(); // For now, localized is same as message
    }
}
