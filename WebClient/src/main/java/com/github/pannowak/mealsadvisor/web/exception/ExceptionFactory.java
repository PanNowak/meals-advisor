package com.github.pannowak.mealsadvisor.web.exception;

import com.github.pannowak.mealsadvisor.api.exception.ClientException;
import com.github.pannowak.mealsadvisor.api.exception.MealsAdvisorException;
import com.github.pannowak.mealsadvisor.api.exception.ServiceException;
import com.github.pannowak.mealsadvisor.api.internationalization.MessageDictionary;
import com.github.pannowak.mealsadvisor.web.filter.response.error.ErrorResponse;
import org.springframework.web.reactive.function.client.ClientRequest;

import java.net.ConnectException;

public final class ExceptionFactory {

    private static final String CLIENT_EXCEPTION_KEY = "clientException";
    private static final String SERVICE_EXCEPTION_KEY = "serviceException";
    private static final String CONNECTION_EXCEPTION_KEY = "connectionException";

    private final MessageDictionary messageDictionary;

    ExceptionFactory(MessageDictionary messageDictionary) {
        this.messageDictionary = messageDictionary;
    }

    public ClientException genericClientException(String logPrefix, ErrorResponse errorResponse) {
        String message = messageDictionary.getMessage(CLIENT_EXCEPTION_KEY, logPrefix,
                errorResponse.getUrlPath(), errorResponse.getStatusCode(), errorResponse.getMessage());
        String localizedMessage = messageDictionary.getLocalizedMessage(CLIENT_EXCEPTION_KEY, logPrefix,
                errorResponse.getUrlPath(), errorResponse.getStatusCode(), errorResponse.getLocalizedMessage());
        return getAdjustedException(new WebClientException(message, localizedMessage));
    }

    public ServiceException internalServerException(String logPrefix, ErrorResponse errorResponse) {
        String message = messageDictionary.getMessage(SERVICE_EXCEPTION_KEY, logPrefix,
                errorResponse.getUrlPath(), errorResponse.getStatusCode(), errorResponse.getMessage());
        String localizedMessage = messageDictionary.getLocalizedMessage(SERVICE_EXCEPTION_KEY, logPrefix,
                errorResponse.getUrlPath(), errorResponse.getStatusCode(), errorResponse.getLocalizedMessage());
        return getAdjustedException(new WebServiceException(message, localizedMessage));
    }

    public MealsAdvisorException connectionException(ClientRequest request, ConnectException cause) {
        String logPrefix = request.logPrefix().strip();
        String message = messageDictionary.getMessage(CONNECTION_EXCEPTION_KEY, logPrefix, request.url());
        String localizedMessage = messageDictionary
                .getLocalizedMessage(CONNECTION_EXCEPTION_KEY, logPrefix, request.url());
        return getAdjustedException(new ConnectionException(message, localizedMessage, cause));
    }

    private <T extends MealsAdvisorException> T getAdjustedException(T exception) {
        StackTraceElement[] newStackTrace =
                removeFactoryMethodCallFromStackTrace(exception.getStackTrace());
        exception.setStackTrace(newStackTrace);
        return exception;
    }

    private StackTraceElement[] removeFactoryMethodCallFromStackTrace(StackTraceElement[] oldStackTrace) {
        int oldStackLength = oldStackTrace.length;
        int numberOfElementsToRemove = Math.min(1, oldStackLength);

        int newStackLength = oldStackLength - numberOfElementsToRemove;
        StackTraceElement[] newStackTrace = new StackTraceElement[newStackLength];
        System.arraycopy(oldStackTrace, numberOfElementsToRemove, newStackTrace, 0, newStackLength);

        return newStackTrace;
    }
}
