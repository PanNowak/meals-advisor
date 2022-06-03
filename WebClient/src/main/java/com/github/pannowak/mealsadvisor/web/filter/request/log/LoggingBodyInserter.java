package com.github.pannowak.mealsadvisor.web.filter.request.log;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.function.Consumer;

final class LoggingBodyInserter implements BodyInserter<Object, ClientHttpRequest> {

    public static BodyInserter<?, ClientHttpRequest> fromClientRequest(ClientRequest delegateRequest,
                                                                       Consumer<RequestInfo> loggingConsumer) {
        return new LoggingBodyInserter(delegateRequest, loggingConsumer);
    }

    private static final DataBufferFactory BUFFER_FACTORY = new DefaultDataBufferFactory();

    private final ClientRequest delegateRequest;
    private final Consumer<RequestInfo> loggingConsumer;

    private LoggingBodyInserter(ClientRequest delegateRequest, Consumer<RequestInfo> loggingConsumer) {
        this.delegateRequest = delegateRequest;
        this.loggingConsumer = loggingConsumer;
    }

    @Override
    public Mono<Void> insert(ClientHttpRequest outputMessage, Context context) {
        return delegateRequest.body()
                .insert(new LoggingClientHttpRequest(outputMessage), context);
    }

    private final class LoggingClientHttpRequest extends ClientHttpRequestDecorator {

        private final DataBuffer buffer;

        LoggingClientHttpRequest(ClientHttpRequest delegate) {
            super(delegate);
            buffer = BUFFER_FACTORY.allocateBuffer();
        }

        @Override
        public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
            return super.writeWith(toBufferingBody(body))
                    .doOnSuccess(d -> logRequest());
        }

        @Override
        public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
            return super.writeAndFlushWith(toNestedBufferingBody(body))
                    .doOnSuccess(d -> logRequest());
        }

        @Override
        public Mono<Void> setComplete() {
            return super.setComplete().doOnSuccess(d -> logRequest());
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

        private void logRequest() {
            var body = buffer.toString(Charset.defaultCharset());
            var requestInfo = createRequestInfo(delegateRequest, body);
            loggingConsumer.accept(requestInfo);
        }

        private RequestInfo createRequestInfo(ClientRequest request, String body) {
            HttpHeaders headers = request.headers();
            return new RequestInfo(request.logPrefix(), request.method(),
                    request.url(), headers.toSingleValueMap(), body);
        }
    }
}
