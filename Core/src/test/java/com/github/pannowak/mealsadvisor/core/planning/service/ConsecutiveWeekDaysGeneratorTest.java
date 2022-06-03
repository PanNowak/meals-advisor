package com.github.pannowak.mealsadvisor.core.planning.service;

import com.github.pannowak.mealsadvisor.api.exception.ClientException;
import com.github.pannowak.mealsadvisor.core.MockClientException;
import com.github.pannowak.mealsadvisor.core.exception.ExceptionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ConsecutiveWeekDaysGeneratorTest {

    @Mock
    private ExceptionFactory exceptionFactory;

    @InjectMocks
    private ConsecutiveWeekDaysGenerator weekDaysGenerator;

    @DisplayName("Given valid boundaries should generate consecutive days of week")
    @ParameterizedTest
    @MethodSource("validFirstAndLastDaysArgsProvider")
    void givenValidBoundariesShouldGenerateConsecutiveDaysOfWeek(int firstDay, int lastDay,
                                                                 List<Integer> expectedDaysRange) {
        StepVerifier.create(weekDaysGenerator.generate(firstDay, lastDay))
                .expectNextSequence(expectedDaysRange)
                .verifyComplete();
    }

    @DisplayName("Given invalid first or last day of week should throw ClientException")
    @ParameterizedTest
    @MethodSource("invalidFirstOrLastDaysProvider")
    void givenInvalidDaysOfWeekShouldThrow(int firstDay, int lastDay) {
        given(exceptionFactory.illegalWeekDayException(anyInt())).willThrow(MockClientException.class);

        StepVerifier.create(weekDaysGenerator.generate(firstDay, lastDay))
                .verifyError(ClientException.class);
    }

    private static Stream<Arguments> validFirstAndLastDaysArgsProvider() {
        return Stream.of(
                arguments(2, 4, List.of(2, 3, 4)),
                arguments(1, 6, List.of(1, 2, 3, 4, 5, 6)),
                arguments(5, 2, List.of(5, 6, 7, 1, 2))
        );
    }

    private static Stream<Arguments> invalidFirstOrLastDaysProvider() {
        return Stream.of(
                arguments(0, 4),
                arguments(3, 8),
                arguments(-3, 14),
                arguments(6, 0),
                arguments(12, 2),
                arguments(10, -4)
        );
    }
}
