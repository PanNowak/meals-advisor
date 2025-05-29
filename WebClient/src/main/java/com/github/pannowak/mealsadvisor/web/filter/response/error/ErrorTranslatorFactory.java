package com.github.pannowak.mealsadvisor.web.filter.response.error;

import com.github.pannowak.mealsadvisor.api.exception.MealsAdvisorException;
import com.github.pannowak.mealsadvisor.web.exception.ExceptionFactory;
import com.github.pannowak.mealsadvisor.web.exception.ExceptionFactoryProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.util.context.ContextView; // Changed from Context

@Component
class ErrorTranslatorFactory {

    private final ExceptionFactoryProvider exceptionFactoryProvider;

    ErrorTranslatorFactory(ExceptionFactoryProvider exceptionFactoryProvider) {
        this.exceptionFactoryProvider = exceptionFactoryProvider;
    }

    public ErrorTranslator getInstance(HttpStatus status, ContextView contextView) { // Changed Context to ContextView
        var exceptionFactory = exceptionFactoryProvider.get(contextView); // Changed here
        return getInstance(status, exceptionFactory);
    }

    private ErrorTranslator getInstance(HttpStatus status, ExceptionFactory exceptionFactory) {
        if (status.is5xxServerError()) {
            return exceptionFactory::internalServerException;
        } else {
            return exceptionFactory::genericClientException;
        }
    }

    @FunctionalInterface
    public interface ErrorTranslator {

        MealsAdvisorException translate(String logPrefix, ErrorResponse errorResponse);
    }
}
