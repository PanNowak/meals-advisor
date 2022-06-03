package com.github.pannowak.mealsadvisor.web.filter.response.retry;

import com.github.pannowak.mealsadvisor.api.exception.ServiceException;
import com.github.pannowak.mealsadvisor.web.filter.response.ResponseFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.Retry.RetrySignal;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;

@Slf4j
@Component
@Order(2)
class RetryFilter extends ResponseFilter {

    private final int maxAttempts;

    RetryFilter(@Value("${maxRetryAttempts:3}") int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    @Override
    protected Mono<ClientResponse> processResponse(ClientRequest request, Mono<ClientResponse> response) {
        Retry retryStrategy = createRetryStrategy(request);
        return response.retryWhen(retryStrategy);
    }

    private RetryBackoffSpec createRetryStrategy(ClientRequest request) {
        return Retry.backoff(maxAttempts, Duration.ofSeconds(1))
                .filter(ServiceException.class::isInstance)
                .doBeforeRetry(retrySignal -> logRequestRetry(request, retrySignal))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                        logRetryFailed(request, retrySignal));
    }

    private void logRequestRetry(ClientRequest request, RetrySignal retrySignal) {
        log.warn("Retrying request {} to {} ({}/{})", request.logPrefix().strip(),
                request.url(), retrySignal.totalRetries() + 1, maxAttempts);
    }

    private Throwable logRetryFailed(ClientRequest request, RetrySignal retrySignal) {
        log.error("Retrying request {} to {} failed", request.logPrefix().strip(), request.url());
        return retrySignal.failure();
    }
}
