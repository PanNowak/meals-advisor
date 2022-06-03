package com.github.pannowak.mealsadvisor.web.filter.response.log;

import com.github.jknack.handlebars.Template;
import com.github.pannowak.mealsadvisor.web.filter.configuration.HBS;
import com.github.pannowak.mealsadvisor.web.filter.response.ResponseFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Component
@Order(0)
class ResponseLoggingFilter extends ResponseFilter {

    private final Template responseTemplate;

    ResponseLoggingFilter(@HBS("response") Template responseTemplate) {
        this.responseTemplate = responseTemplate;
    }

    @Override
    protected Mono<ClientResponse> processResponse(ClientRequest request, Mono<ClientResponse> response) {
        return response.map(this::decorateResponse);
    }

    private ClientResponse decorateResponse(ClientResponse response) {
        return ClientResponse.from(response)
                .body(response.body(LoggingBodyExtractor.fromClientResponse(response, this::logResponse)))
                .build();
    }

    private void logResponse(ResponseInfo responseInfo) {
        String responseMessage = getResponseMessage(responseInfo);
        log.info("Response received:\n{}", responseMessage);
    }

    private String getResponseMessage(ResponseInfo responseInfo) {
        try {
            return responseTemplate.apply(responseInfo);
        } catch (IOException e) {
            log.warn("Failed to create response info message", e);
            return "NO DETAILS";
        }
    }
}
