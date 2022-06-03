package com.github.pannowak.mealsadvisor.web.filter.log.request;

import com.github.jknack.handlebars.Template;
import com.github.pannowak.mealsadvisor.web.filter.log.configuration.HBS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static org.springframework.http.HttpMethod.*;

@Slf4j
@Component
public class RequestLoggingFilter {

    private static final Set<HttpMethod> BODYLESS_METHODS = Set.of(GET, DELETE, TRACE, OPTIONS, HEAD);

    private final Template requestTemplate;

    RequestLoggingFilter(@HBS("request") Template requestTemplate) {
        this.requestTemplate = requestTemplate;
    }

    public ServerHttpRequest processRequest(ServerHttpRequest request) {
        if (hasNoBody(request)) {
            logRequest(request, null);
            return request;
        } else {
            return new BufferingHttpRequest(request, body -> logRequest(request, body));
        }
    }

    private boolean hasNoBody(ServerHttpRequest request) {
        return BODYLESS_METHODS.contains(request.getMethod());
    }

    private void logRequest(ServerHttpRequest request, String body) {
        var requestInfo = createRequestInfo(request, body);
        var requestMessage = createRequestMessage(requestInfo);
        log.info("Request received:\n{}", requestMessage);
    }

    private RequestInfo createRequestInfo(ServerHttpRequest request, String body) {
        var headers = processHeaders(request.getHeaders());
        return new RequestInfo(request.getId(), request.getMethod(),
                request.getURI(), headers, body);
    }

    private Map<String, String> processHeaders(HttpHeaders inputHeaders) {
        Map<String, String> headers = inputHeaders.toSingleValueMap();
        return CollectionUtils.isEmpty(headers) ? null : headers;
    }

    private String createRequestMessage(RequestInfo requestInfo) {
        try {
            return requestTemplate.apply(requestInfo);
        } catch (IOException e) {
            log.warn("Failed to create request info message", e);
            return "NO DETAILS";
        }
    }

    private static final class BufferingHttpRequest extends ServerHttpRequestDecorator {

        private static final DataBufferFactory BUFFER_FACTORY = new DefaultDataBufferFactory();

        private final Consumer<String> bodyConsumer;
        private final DataBuffer buffer;

        BufferingHttpRequest(ServerHttpRequest inputMessage, Consumer<String> bodyConsumer) {
            super(inputMessage);
            this.bodyConsumer = bodyConsumer;
            this.buffer = BUFFER_FACTORY.allocateBuffer();
        }

        @Override
        public Flux<DataBuffer> getBody() {
            return super.getBody()
                    .transform(this::ensureNoMissingBody)
                    .doOnNext(buffer::write)
                    .doOnComplete(this::consumeBufferedData);
        }

        private Flux<DataBuffer> ensureNoMissingBody(Flux<DataBuffer> fluxBody) {
            Mono<DataBuffer> emptyBufferMono =
                    Mono.fromCallable(() -> BUFFER_FACTORY.allocateBuffer(0));
            return fluxBody.switchIfEmpty(emptyBufferMono);
        }

        private void consumeBufferedData() {
            try {
                var body = buffer.toString(Charset.defaultCharset());
                bodyConsumer.accept(body);
            } finally {
                DataBufferUtils.release(buffer);
            }
        }
    }
}
