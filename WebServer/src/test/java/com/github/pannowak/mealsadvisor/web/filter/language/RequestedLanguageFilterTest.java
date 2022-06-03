package com.github.pannowak.mealsadvisor.web.filter.language;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class RequestedLanguageFilterTest {

    private final RequestedLanguageFilter languageFilter = new RequestedLanguageFilter();

    @DisplayName("Should copy requested language from Accept-Language header into Reactor Context")
    @ParameterizedTest
    @MethodSource("requestLanguageArgs")
    void shouldCopyRequestedLanguageFromHeaderToReactorContext(String requestedLanguage,
                                                               Locale expectedLocale) {
        var exchange = createMockExchange(requestedLanguage);

        Mono<Void> result = filterRequest(exchange);

        assertLocaleInContext(result, expectedLocale);
    }

    @DisplayName("Should pass english into Reactor Context if there's no Accept-Language header")
    @Test
    void shouldPassEnglishLocaleIfNoAcceptLanguageHeaderIsPresent() {
        var exchange = createMockExchange();

        Mono<Void> result = filterRequest(exchange);

        assertLocaleInContext(result, Locale.ENGLISH);
    }

    private static Stream<Arguments> requestLanguageArgs() {
        return Stream.of(
                arguments("fr", Locale.FRENCH),
                arguments("de", Locale.GERMAN),
                arguments("pl", Locale.forLanguageTag("pl"))
        );
    }

    private ServerWebExchange createMockExchange(String... acceptedLanguages) {
        var request = MockServerHttpRequest.get("/")
                .header(HttpHeaders.ACCEPT_LANGUAGE, acceptedLanguages)
                .build();
        return MockServerWebExchange.from(request);
    }

    private Mono<Void> filterRequest(ServerWebExchange exchange) {
        WebFilterChain byPassChain = ex -> Mono.just(ex).then();
        return languageFilter.filter(exchange, byPassChain);
    }

    private void assertLocaleInContext(Mono<Void> result, Locale expectedLocale) {
        StepVerifier.create(result)
                .expectAccessibleContext()
                .contains(Locale.class, expectedLocale)
                .then()
                .verifyComplete();
    }
}
