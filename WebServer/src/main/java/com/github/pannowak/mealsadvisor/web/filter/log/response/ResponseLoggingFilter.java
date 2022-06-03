package com.github.pannowak.mealsadvisor.web.filter.log.response;

import com.github.jknack.handlebars.Template;
import com.github.pannowak.mealsadvisor.web.filter.log.configuration.HBS;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Component
public class ResponseLoggingFilter {

    private final Template responseTemplate;

    ResponseLoggingFilter(@HBS("response") Template responseTemplate) {
        this.responseTemplate = responseTemplate;
    }

    public ServerHttpResponse processResponse(ServerHttpRequest request, ServerHttpResponse response) {
        return new BufferingHttpResponse(response, body -> logResponse(request.getId(), response, body));
    }

    private void logResponse(String id, ServerHttpResponse response, String body) {
        var responseInfo = createResponseInfo(id, response, body);
        var responseMessage = createResponseMessage(responseInfo);
        log.info("Response sent:\n{}", responseMessage);
    }

    private ResponseInfo createResponseInfo(String id, ServerHttpResponse response, String body) {
        var headers = processHeaders(response.getHeaders());
        return new ResponseInfo(id, response.getRawStatusCode(), headers, body);
    }

    private Map<String, String> processHeaders(HttpHeaders inputHeaders) {
        Map<String, String> headers = inputHeaders.toSingleValueMap();
        return CollectionUtils.isEmpty(headers) ? null : headers;
    }

    private String createResponseMessage(ResponseInfo responseInfo) {
        try {
            return responseTemplate.apply(responseInfo);
        } catch (IOException e) {
            log.warn("Failed to create response info message", e);
            return "NO DETAILS";
        }
    }

    private static final class BufferingHttpResponse extends ServerHttpResponseDecorator {

        private static final DataBufferFactory BUFFER_FACTORY = new DefaultDataBufferFactory();

        private final Consumer<String> bodyConsumer;
        private final DataBuffer buffer;

        BufferingHttpResponse(ServerHttpResponse outputMessage, Consumer<String> bodyConsumer) {
            super(outputMessage);
            this.bodyConsumer = bodyConsumer;
            this.buffer = BUFFER_FACTORY.allocateBuffer();
            beforeCommit(() -> Mono.fromRunnable(this::consumeBufferedData));
        }

        @Override
        public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
            return super.writeWith(toBufferingBody(body));
        }

        @Override
        public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
            return super.writeAndFlushWith(toNestedBufferingBody(body));
        }

        private Flux<DataBuffer> toBufferingBody(Publisher<? extends DataBuffer> body) {
            return Flux.from(body)
                    .cast(DataBuffer.class)
                    .doOnNext(buffer::write);
        }

        private Flux<Flux<DataBuffer>> toNestedBufferingBody(
                Publisher<? extends Publisher<? extends DataBuffer>> body) {
            return Flux.from(body).map(this::toBufferingBody);
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
