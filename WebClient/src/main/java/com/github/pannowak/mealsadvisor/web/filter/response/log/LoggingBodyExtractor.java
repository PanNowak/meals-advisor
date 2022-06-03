package com.github.pannowak.mealsadvisor.web.filter.response.log;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.client.reactive.ClientHttpResponseDecorator;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Flux;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.Consumer;

final class LoggingBodyExtractor implements BodyExtractor<Flux<DataBuffer>, ClientHttpResponse> {

    public static BodyExtractor<Flux<DataBuffer>, ClientHttpResponse> fromClientResponse(
            ClientResponse delegateResponse, Consumer<ResponseInfo> loggingConsumer) {
        return new LoggingBodyExtractor(delegateResponse, loggingConsumer);
    }

    private static final DataBufferFactory BUFFER_FACTORY = new DefaultDataBufferFactory();

    private final ClientResponse delegateResponse;
    private final Consumer<ResponseInfo> loggingConsumer;

    private LoggingBodyExtractor(ClientResponse delegateResponse,
                                 Consumer<ResponseInfo> loggingConsumer) {
        this.delegateResponse = delegateResponse;
        this.loggingConsumer = loggingConsumer;
    }

    @Override
    public Flux<DataBuffer> extract(ClientHttpResponse inputMessage, Context context) {
        return new LoggingClientHttpResponse(inputMessage).getBody();
    }
    
    private final class LoggingClientHttpResponse extends ClientHttpResponseDecorator {

        private final DataBuffer buffer;

        LoggingClientHttpResponse(ClientHttpResponse delegate) {
            super(delegate);
            buffer = BUFFER_FACTORY.allocateBuffer();
        }

        @Override
        public Flux<DataBuffer> getBody() {
            return super.getBody()
                    .doOnNext(buffer::write)
                    .doOnComplete(this::logRequest);
        }

        private void logRequest() {
            var body = buffer.toString(Charset.defaultCharset());
            var requestInfo = createResponseInfo(delegateResponse, body);
            loggingConsumer.accept(requestInfo);
        }

        private ResponseInfo createResponseInfo(ClientResponse response, String body) {
            HttpStatus status = response.statusCode();
            Map<String, String> headers = transformToMap(response.headers());
            return new ResponseInfo(response.logPrefix(), status.value(), headers, body);
        }

        private Map<String, String> transformToMap(ClientResponse.Headers headers) {
            return headers.asHttpHeaders().toSingleValueMap();
        }
    }
}
