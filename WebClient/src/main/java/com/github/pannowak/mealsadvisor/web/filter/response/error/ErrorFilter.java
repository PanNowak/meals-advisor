package com.github.pannowak.mealsadvisor.web.filter.response.error;

import com.github.pannowak.mealsadvisor.web.exception.ExceptionFactoryProvider;
import com.github.pannowak.mealsadvisor.web.filter.response.ResponseFilter;
import com.github.pannowak.mealsadvisor.web.filter.response.error.ErrorTranslatorFactory.ErrorTranslator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

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
        return Mono.subscriberContext()
                .flatMap(context -> translate(request, response, context));
    }

    private Mono<ClientResponse> translate(ClientRequest request, Mono<ClientResponse> response,
                                           Context context) {
        return translateConnectivityErrors(request, response, context)
                .flatMap(r -> translateIfErrorResponse(r, context));
    }

    private Mono<ClientResponse> translateConnectivityErrors(ClientRequest request, Mono<ClientResponse> response,
                                                             Context context) {
        return response
                .onErrorMap(ConnectException.class, e -> {
                    var exceptionFactory = exceptionFactoryProvider.get(context);
                    return exceptionFactory.connectionException(request, e);
                });
        //TODO obsługa innych błędów
    }

    private Mono<ClientResponse> translateIfErrorResponse(ClientResponse response, Context context) {
        if (response.statusCode().isError()) {
            return translateErrorResponse(response, context);
        } else {
            return Mono.just(response);
        }
    }

    private Mono<ClientResponse> translateErrorResponse(ClientResponse response, Context context) {
        ErrorTranslator errorTranslator = errorTranslatorFactory
                .getInstance(response.statusCode(), context);
        String logPrefix = response.logPrefix().strip();
        return response.bodyToMono(ErrorResponse.class)
                .map(errorResponse -> errorTranslator.translate(logPrefix, errorResponse))
                .flatMap(Mono::error);
    }
}
