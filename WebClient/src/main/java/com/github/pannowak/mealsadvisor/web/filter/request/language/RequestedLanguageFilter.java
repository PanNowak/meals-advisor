package com.github.pannowak.mealsadvisor.web.filter.request.language;

import com.github.pannowak.mealsadvisor.web.filter.request.RequestFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Collections;
import java.util.Locale;

@Component
@Order(1)
class RequestedLanguageFilter extends RequestFilter {

    @Override
    protected Mono<ClientRequest> processRequest(ClientRequest request) {
        return Mono.subscriberContext()
                .map(context -> addAcceptLanguageHeader(request, context));
    }

    private ClientRequest addAcceptLanguageHeader(ClientRequest request, Context context) {
        return ClientRequest.from(request)
                .headers(headers -> setAcceptLanguage(headers, context))
                .build();
    }

    private void setAcceptLanguage(HttpHeaders headers, Context context) {
        context.<Locale>getOrEmpty(Locale.class)
                .map(Collections::singletonList)
                .ifPresent(headers::setAcceptLanguageAsLocales);
    }
}
