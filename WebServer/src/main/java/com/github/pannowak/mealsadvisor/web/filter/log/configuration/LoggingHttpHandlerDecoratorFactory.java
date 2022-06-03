package com.github.pannowak.mealsadvisor.web.filter.log.configuration;

import com.github.pannowak.mealsadvisor.web.config.ApiPathConfigurer;
import com.github.pannowak.mealsadvisor.web.filter.log.request.RequestLoggingFilter;
import com.github.pannowak.mealsadvisor.web.filter.log.response.ResponseLoggingFilter;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.HttpHandlerDecoratorFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
class LoggingHttpHandlerDecoratorFactory implements HttpHandlerDecoratorFactory {

    private final ApiPathConfigurer apiPathConfigurer;
    private final RequestLoggingFilter requestLoggingFilter;
    private final ResponseLoggingFilter responseLoggingFilter;

    LoggingHttpHandlerDecoratorFactory(ApiPathConfigurer apiPathConfigurer,
                                       RequestLoggingFilter requestLoggingFilter,
                                       ResponseLoggingFilter responseLoggingFilter) {
        this.apiPathConfigurer = apiPathConfigurer;
        this.requestLoggingFilter = requestLoggingFilter;
        this.responseLoggingFilter = responseLoggingFilter;
    }

    @Override
    public HttpHandler apply(HttpHandler httpHandler) {
        return new LoggingHttpHandlerDecorator(httpHandler);
    }

    private final class LoggingHttpHandlerDecorator implements HttpHandler {

        private final HttpHandler delegate;

        LoggingHttpHandlerDecorator(HttpHandler delegate) {
            this.delegate = delegate;
        }

        @Override
        public Mono<Void> handle(ServerHttpRequest request, ServerHttpResponse response) {
            if (isToBeLogged(request.getURI())) {
                request = requestLoggingFilter.processRequest(request);
                response = responseLoggingFilter.processResponse(request, response);
            }
            return delegate.handle(request, response);
        }

        private boolean isToBeLogged(URI requestUri) {
            String path = StringUtils.trimLeadingCharacter(requestUri.getPath(), '/');
            return apiPathConfigurer.isApiPath(path);
        }
    }
}
