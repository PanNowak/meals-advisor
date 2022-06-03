package com.github.pannowak.mealsadvisor.web.filter.request.log;

import com.github.jknack.handlebars.Template;
import com.github.pannowak.mealsadvisor.web.filter.configuration.HBS;
import com.github.pannowak.mealsadvisor.web.filter.request.RequestFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Component
@Order(0)
class RequestLoggingFilter extends RequestFilter {

    private final Template requestTemplate;

    RequestLoggingFilter(@HBS("request") Template requestTemplate) {
        this.requestTemplate = requestTemplate;
    }

    @Override
    protected Mono<ClientRequest> processRequest(ClientRequest clientRequest) {
        return Mono.just(clientRequest)
                .map(this::decorateRequest);
    }

    private ClientRequest decorateRequest(ClientRequest request) {
        return ClientRequest.from(request)
                .body(LoggingBodyInserter.fromClientRequest(request, this::logRequest))
                .build();
    }

    private void logRequest(RequestInfo requestInfo) {
        String requestMessage = getRequestMessage(requestInfo);
        log.info("Request sent:\n{}", requestMessage);
    }

    private String getRequestMessage(RequestInfo requestInfo) {
        try {
            return requestTemplate.apply(requestInfo);
        } catch (IOException e) {
            log.warn("Failed to create request info message", e);
            return "NO DETAILS";
        }
    }
}
