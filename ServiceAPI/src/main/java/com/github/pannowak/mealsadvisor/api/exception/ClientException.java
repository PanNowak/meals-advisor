package com.github.pannowak.mealsadvisor.api.exception;

public abstract class ClientException extends MealsAdvisorException {

    protected ClientException(String message) {
        super(message);
    }
}
