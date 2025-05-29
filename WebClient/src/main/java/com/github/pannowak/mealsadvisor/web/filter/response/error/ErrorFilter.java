package com.github.pannowak.mealsadvisor.web.filter.response.error;

import com.github.pannowak.mealsadvisor.web.exception.ExceptionFactoryProvider;
import com.github.pannowak.mealsadvisor.web.filter.response.ResponseFilter;
import com.github.pannowak.mealsadvisor.web.filter.response.error.ErrorTranslatorFactory.ErrorTranslator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus; // Added import
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView; // Changed import

import java.net.ConnectException;

@Component
@Order(1)
class ErrorFilter extends ResponseFilter {

    private final ErrorTranslatorFactory errorTranslatorFactory;
    private final ExceptionFactoryProvider exceptionFactoryProvider;

    ErrorFilter(ErrorTranslatorFactory errorTranslatorFactory,
                ExceptionFactoryProvider exceptionFactoryProvider) {
        this.errorTranslatorFactory = errorTranslatorFactory;
        this.exceptionFactoryProvider = exceptionFactoryProvider;
    }

    @Override
    protected Mono<ClientResponse> processResponse(ClientRequest request, Mono<ClientResponse> response) {
        return Mono.deferContextual(Mono::just) // Changed here
                .flatMap(contextView -> translate(request, response, contextView));
    }

    private Mono<ClientResponse> translate(ClientRequest request, Mono<ClientResponse> response,
                                           ContextView contextView) { // Changed here
        return translateConnectivityErrors(request, response, contextView)
                .flatMap(r -> translateIfErrorResponse(r, contextView));
    }

    private Mono<ClientResponse> translateConnectivityErrors(ClientRequest request, Mono<ClientResponse> response,
                                                             ContextView contextView) { // Changed here
        return response
                .onErrorMap(ConnectException.class, e -> {
                    var exceptionFactory = exceptionFactoryProvider.get(contextView); // Changed here
                    return exceptionFactory.connectionException(request, e);
                });
        //TODO obsługa innych błędów
    }

    private Mono<ClientResponse> translateIfErrorResponse(ClientResponse response, ContextView contextView) { // Changed here
        if (response.statusCode().isError()) {
            return translateErrorResponse(response, contextView);
        } else {
            return Mono.just(response);
        }
    }

    private Mono<ClientResponse> translateErrorResponse(ClientResponse response, ContextView contextView) { // Changed here
        ErrorTranslator errorTranslator = errorTranslatorFactory
                .getInstance(HttpStatus.valueOf(response.statusCode().value()), contextView); // Changed response.statusCode() and context
        String logPrefix = response.logPrefix().strip();
        return response.bodyToMono(ErrorResponse.class)
                .map(errorResponse -> errorTranslator.translate(logPrefix, errorResponse))
                .flatMap(Mono::error);
    }
}
