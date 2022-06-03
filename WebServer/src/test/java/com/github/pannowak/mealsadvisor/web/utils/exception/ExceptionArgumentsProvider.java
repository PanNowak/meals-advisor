package com.github.pannowak.mealsadvisor.web.utils.exception;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static com.github.pannowak.mealsadvisor.web.utils.exception.ExceptionArguments.*;

public abstract class ExceptionArgumentsProvider implements ArgumentsProvider {

    public static final class AllExceptions extends ExceptionArgumentsProvider {

        @Override
        public Stream<ExceptionArguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    clientExceptionArgs(),
                    notFoundExceptionArgs(),
                    serviceExceptionArgs(),
                    unexpectedExceptionArgs()
            );
        }
    }

    public static final class ServerExceptions extends ExceptionArgumentsProvider {

        @Override
        public Stream<ExceptionArguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    serviceExceptionArgs(),
                    unexpectedExceptionArgs()
            );
        }
    }

    public static final class BusinessExceptions extends ExceptionArgumentsProvider {

        @Override
        public Stream<ExceptionArguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    clientExceptionArgs(),
                    notFoundExceptionArgs(),
                    serviceExceptionArgs()
            );
        }
    }

    @Override
    public abstract Stream<ExceptionArguments> provideArguments(ExtensionContext context);

    private ExceptionArgumentsProvider(){}
}
