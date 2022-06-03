package com.github.pannowak.mealsadvisor.core.planning.service;

import com.github.pannowak.mealsadvisor.core.exception.ExceptionFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.stream.IntStream;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.util.stream.IntStream.rangeClosed;

@Component
class ConsecutiveWeekDaysGenerator {

    private final ExceptionFactory exceptionFactory;

    ConsecutiveWeekDaysGenerator(ExceptionFactory exceptionFactory) {
        this.exceptionFactory = exceptionFactory;
    }

    public Flux<Integer> generate(int firstDay, int lastDay) {
        return Mono.fromCallable(() -> generateInternal(firstDay, lastDay))
                .flatMapIterable(Function.identity());
    }

    private Iterable<Integer> generateInternal(int firstDay, int lastDay) {
        validate(firstDay, lastDay);
        return () -> createDayStream(firstDay, lastDay).iterator();
    }

    private void validate(int firstDay, int lastDay) {
        validate(firstDay);
        validate(lastDay);
    }

    private void validate(int dayOfWeek) {
        if (dayOfWeek < MONDAY.getValue() || dayOfWeek > SUNDAY.getValue()) {
            throw exceptionFactory.illegalWeekDayException(dayOfWeek);
        }
    }

    private IntStream createDayStream(int firstDay, int lastDay) {
        if (firstDay <= lastDay) {
            return rangeClosed(firstDay, lastDay);
        } else {
            return IntStream.concat(
                    rangeClosed(firstDay, SUNDAY.getValue()),
                    rangeClosed(MONDAY.getValue(), lastDay));
        }
    }
}
