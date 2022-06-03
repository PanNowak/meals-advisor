package com.github.pannowak.mealsadvisor.web.exception;

import com.github.pannowak.mealsadvisor.api.exception.MealsAdvisorException;
import com.github.pannowak.mealsadvisor.api.internationalization.MessageDictionary;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

final class LocalizedResponseStatusException extends ResponseStatusException {

    private static final String EXCEPTION_BUNDLE_NAME =
            ErrorTranslator.class.getPackageName() + ".ExceptionMessages";
    private static final String UNEXPECTED_EXCEPTION_KEY = "unexpectedException";

    public static LocalizedResponseStatusException fromBusinessException(HttpStatus status,
                                                                         MealsAdvisorException cause) {
        return new LocalizedResponseStatusException(status, cause.getMessage(),
                cause.getLocalizedMessage(), cause);
    }

    public static LocalizedResponseStatusException fromUnexpectedException(Locale locale,
                                                                           Throwable cause) {
        var messageDictionary = new MessageDictionary(EXCEPTION_BUNDLE_NAME, locale);
        String message = messageDictionary.getMessage(UNEXPECTED_EXCEPTION_KEY);
        String localizedMessage = messageDictionary.getLocalizedMessage(UNEXPECTED_EXCEPTION_KEY);
        return new LocalizedResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message,
                localizedMessage, cause);
    }

    private final String localizedReason;

    private LocalizedResponseStatusException(HttpStatus status, String reason,
                                             String localizedReason, Throwable cause) {
        super(status, reason, cause);
        this.localizedReason = localizedReason;
    }

    public String getLocalizedReason() {
        return localizedReason;
    }
}
