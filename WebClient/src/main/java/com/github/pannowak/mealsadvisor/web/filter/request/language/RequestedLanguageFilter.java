package com.github.pannowak.mealsadvisor.web.filter.request.language;

import com.github.pannowak.mealsadvisor.web.filter.request.RequestFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView; // Changed from Context

import java.util.Collections;
import java.util.Locale;

@Component
@Order(1)
class RequestedLanguageFilter extends RequestFilter {

    @Override
    protected Mono<ClientRequest> processRequest(ClientRequest request) {
        return Mono.deferContextual(Mono::just)
                .map(contextView -> addAcceptLanguageHeader(request, contextView)); // Changed context to contextView
    }

    private ClientRequest addAcceptLanguageHeader(ClientRequest request, ContextView contextView) { // Changed Context to ContextView
        return ClientRequest.from(request)
                .headers(headers -> setAcceptLanguage(headers, contextView)) // Changed context to contextView
                .build();
    }

    private void setAcceptLanguage(HttpHeaders headers, ContextView contextView) { // Changed Context to ContextView
        contextView.<Locale>getOrEmpty(Locale.class) // Changed context to contextView
                .map(Collections::singletonList)
                .ifPresent(headers::setAcceptLanguageAsLocales);
    }
}
