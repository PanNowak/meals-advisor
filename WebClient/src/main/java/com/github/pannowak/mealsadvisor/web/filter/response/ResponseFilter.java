package com.github.pannowak.mealsadvisor.web.filter.response;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public abstract class ResponseFilter implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        var clientResponse = next.exchange(request);
        return processResponse(request, clientResponse);
    }

    protected abstract Mono<ClientResponse> processResponse(ClientRequest request, Mono<ClientResponse> response);
}
