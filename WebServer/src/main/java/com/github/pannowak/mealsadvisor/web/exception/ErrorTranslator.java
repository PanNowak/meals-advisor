package com.github.pannowak.mealsadvisor.web.exception;

import com.github.pannowak.mealsadvisor.api.exception.ClientException;
import com.github.pannowak.mealsadvisor.api.exception.EntityNotFoundException;
import com.github.pannowak.mealsadvisor.api.exception.MealsAdvisorException;
import com.github.pannowak.mealsadvisor.api.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Slf4j
@Component
@Order(-2)
class ErrorTranslator implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        var responseStatusException = unifyException(exchange, ex);
        logIfNecessary(responseStatusException);
        return Mono.error(responseStatusException);
    }

    private ResponseStatusException unifyException(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof ResponseStatusException) {
            ResponseStatusException e = (ResponseStatusException) ex;
            if (e.getCause() != null) {
                return new ResponseStatusException(e.getStatus(), e.getReason() + " -> " + e.getCause().getMessage(), e.getCause().getCause());
            }
            return e;
        } else {
            var locale = resolveLocale(exchange.getLocaleContext());
            return translateException(ex, locale);
        }
    }

    private Locale resolveLocale(LocaleContext localeContext) {
        var requestedLocale = localeContext.getLocale();
        return requestedLocale != null ? requestedLocale : Locale.ENGLISH;
    }

    private ResponseStatusException translateException(Throwable ex, Locale locale) {
        if (ex instanceof MealsAdvisorException) {
            return processBusinessException((MealsAdvisorException) ex);
        } else {
            return LocalizedResponseStatusException.fromUnexpectedException(locale, ex);
        }
    }

    private ResponseStatusException processBusinessException(MealsAdvisorException exception) {
        HttpStatus status = determineStatus(exception);
        return LocalizedResponseStatusException.fromBusinessException(status, exception);
    }

    private HttpStatus determineStatus(MealsAdvisorException exception) {
        if (exception instanceof ServiceException) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        } else {
            return determineClientErrorStatus((ClientException) exception);
        }
    }

    private HttpStatus determineClientErrorStatus(ClientException exception) {
        if (exception instanceof EntityNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    private void logIfNecessary(ResponseStatusException exception) {
        var status = exception.getStatus();
        if (notLoggedByOtherHandlers(status)) {
            var message = getErrorLogMessage(status);
            log.error(message, exception);
        }
    }

    private boolean notLoggedByOtherHandlers(HttpStatus status) {
        return status != HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String getErrorLogMessage(HttpStatus status) {
        var errorType = status.is4xxClientError() ? "Client" : "Service";
        return String.format("%s exception occurred", errorType);
    }
}
