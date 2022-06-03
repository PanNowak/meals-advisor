package com.github.pannowak.mealsadvisor.web.filter.request;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public abstract class RequestFilter implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return ExchangeFilterFunction.ofRequestProcessor(this::processRequest)
                .filter(request, next);
    }

    protected abstract Mono<ClientRequest> processRequest(ClientRequest request);
}
