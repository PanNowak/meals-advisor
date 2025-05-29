package com.github.pannowak.mealsadvisor.api.exception;

public abstract class ServiceException extends MealsAdvisorException {

    protected ServiceException(String message) {
        super(message);
    }
}
