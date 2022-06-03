package com.github.pannowak.mealsadvisor.web.exception;

import com.github.pannowak.mealsadvisor.web.utils.exception.ExceptionArgumentsProvider;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ErrorTranslatorTest {

    private final ErrorTranslator errorTranslator = new ErrorTranslator();

    @DisplayName("Given ResponseStatusException without a cause should return it")
    @Test
    void givenResponseStatusExceptionWithoutCauseShouldReturnIt() {
        var exchange = createMockExchange();
        var originalException = new ResponseStatusException(HttpStatus.NOT_FOUND, "Main and only reason");

        Optional<Throwable> translatedException = translateException(exchange, originalException);

        assertThat(translatedException)
                .get(InstanceOfAssertFactories.THROWABLE)
                .isExactlyInstanceOf(ResponseStatusException.class)
                .hasNoCause()
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND)
                .hasFieldOrPropertyWithValue("reason", "Main and only reason");
    }

    @DisplayName("Given ResponseStatusException with a cause should return it with more detailed message")
    @Test
    void givenResponseStatusExceptionWithCauseShouldReturnItWithMoreDetailedMessage() {
        var exchange = createMockExchange();
        var originalException = new ResponseStatusException(HttpStatus.BAD_REQUEST, "First-level reason",
                new RuntimeException("Second-level reason"));

        Optional<Throwable> translatedException = translateException(exchange, originalException);

        assertThat(translatedException)
                .get(InstanceOfAssertFactories.THROWABLE)
                .isExactlyInstanceOf(ResponseStatusException.class)
                .hasNoCause()
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
                .hasFieldOrPropertyWithValue("reason", "First-level reason -> Second-level reason");
    }

    @DisplayName("Given unexpected error should return Internal Server Error")
    @Test
    void givenUnexpectedExceptionShouldReturnResponseStatusExceptionWith500Code() {
        var exchange = createMockExchange();
        var originalException = new Exception();

        Optional<Throwable> translatedException = translateException(exchange, originalException);

        assertThat(translatedException)
                .get(InstanceOfAssertFactories.THROWABLE)
                .isExactlyInstanceOf(LocalizedResponseStatusException.class)
                .hasCause(originalException)
                .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR)
                .hasFieldOrPropertyWithValue("reason", "Unexpected exception occurred")
                .hasFieldOrPropertyWithValue("localizedReason", "Unexpected exception occurred");
    }

    @DisplayName("Given unexpected error should return Internal Server Error with message in requested locale")
    @Test
    void givenUnexpectedExceptionShouldReturnResponseStatusExceptionWith500CodeAndInRequestedLocale() {
        var exchange = createMockExchange("pl");
        var originalException = new Exception();

        Optional<Throwable> translatedException = translateException(exchange, originalException);

        assertThat(translatedException)
                .get(InstanceOfAssertFactories.THROWABLE)
                .isExactlyInstanceOf(LocalizedResponseStatusException.class)
                .hasCause(originalException)
                .hasFieldOrPropertyWithValue("status", HttpStatus.INTERNAL_SERVER_ERROR)
                .hasFieldOrPropertyWithValue("reason", "Unexpected exception occurred")
                .hasFieldOrPropertyWithValue("localizedReason", "Wystąpił nieoczekiwany błąd");
    }

    @DisplayName("Given business exception should return matching LocalizedResponseStatusException")
    @ParameterizedTest
    @ArgumentsSource(BusinessExceptionArgsProvider.class)
    void givenBusinessExceptionShouldReturnResponseStatusExceptionWithMatchingCodesAndMessages(Exception originalException,
                                                                                               HttpStatus expectedStatus) {
        var exchange = createMockExchange();

        Optional<Throwable> translatedException = translateException(exchange, originalException);

        assertThat(translatedException)
                .get(InstanceOfAssertFactories.THROWABLE)
                .isExactlyInstanceOf(LocalizedResponseStatusException.class)
                .hasCause(originalException)
                .hasFieldOrPropertyWithValue("status", expectedStatus)
                .hasFieldOrPropertyWithValue("reason", originalException.getMessage())
                .hasFieldOrPropertyWithValue("localizedReason", originalException.getLocalizedMessage());
    }

    private ServerWebExchange createMockExchange(String... acceptedLanguages) {
        var request = MockServerHttpRequest.get("/")
                .header(HttpHeaders.ACCEPT_LANGUAGE, acceptedLanguages)
                .build();
        return MockServerWebExchange.from(request);
    }

    private Optional<Throwable> translateException(ServerWebExchange exchange, Throwable originalException) {
        try {
            errorTranslator.handle(exchange, originalException).block();
            return Optional.empty();
        } catch (Throwable e) {
            return Optional.of(e);
        }
    }

    private static final class BusinessExceptionArgsProvider implements ArgumentsProvider {

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return new ExceptionArgumentsProvider.BusinessExceptions()
                    .provideArguments(context)
                    .map(args -> arguments(args.exceptionFromService(), args.expectedStatus()));
        }
    }
}
