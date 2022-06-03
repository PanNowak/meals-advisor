package com.github.pannowak.mealsadvisor.api.exception;

public abstract class MealsAdvisorException extends RuntimeException {

    @Override
    public abstract String getMessage();

    @Override
    public String getLocalizedMessage() {
        return getMessage(); //TODO abstract
    }
}
