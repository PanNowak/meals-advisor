package com.github.pannowak.mealsadvisor.web.utils.exception;

import com.github.pannowak.mealsadvisor.api.exception.ClientException;
import com.github.pannowak.mealsadvisor.api.exception.EntityNotFoundException;
import com.github.pannowak.mealsadvisor.api.exception.ServiceException;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.http.HttpStatus;

import static com.github.pannowak.mealsadvisor.web.utils.TestStringUtils.toSingleLine;

public abstract class ExceptionArguments implements Arguments {

    public static ExceptionArguments clientExceptionArgs() {
        return new ExceptionArguments() {
            @Override
            public ClientException exceptionFromService() {
                return new MockClientException("Mock client exception message",
                        "Localized mock client exception message");
            }

            @Override
            public HttpStatus expectedStatus() {
                return HttpStatus.BAD_REQUEST;
            }

            @Override
            public String expectedBodyTemplate() {
                return toSingleLine("""
                    {
                        "path": "%s",
                        "status": 400,
                        "error": "Bad Request",
                        "message": "Mock client exception message",
                        "localizedMessage": "Localized mock client exception message"
                    }
                    """);
            }
        };
    }

    public static ExceptionArguments notFoundExceptionArgs() {
        return new ExceptionArguments() {
            @Override
            public EntityNotFoundException exceptionFromService() {
                return new MockNotFoundException("Mock not found exception message",
                        "Localized mock not found exception message");
            }

            @Override
            public HttpStatus expectedStatus() {
                return HttpStatus.NOT_FOUND;
            }

            @Override
            public String expectedBodyTemplate() {
                return toSingleLine("""
                    {
                        "path": "%s",
                        "status": 404,
                        "error": "Not Found",
                        "message": "Mock not found exception message",
                        "localizedMessage": "Localized mock not found exception message"
                    }
                    """);
            }
        };
    }

    public static ExceptionArguments serviceExceptionArgs() {
        return new ExceptionArguments() {
            @Override
            public ServiceException exceptionFromService() {
                return new MockServiceException("Mock service exception message",
                        "Localized mock service exception message");
            }

            @Override
            public HttpStatus expectedStatus() {
                return HttpStatus.SERVICE_UNAVAILABLE;
            }

            @Override
            public String expectedBodyTemplate() {
                return toSingleLine("""
                    {
                        "path": "%s",
                        "status": 503,
                        "error": "Service Unavailable",
                        "message": "Mock service exception message",
                        "localizedMessage": "Localized mock service exception message"
                    }
                    """);
            }
        };
    }

    public static ExceptionArguments unexpectedExceptionArgs() {
        return new ExceptionArguments() {
            @Override
            public Exception exceptionFromService() {
                return new Exception();
            }

            @Override
            public HttpStatus expectedStatus() {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }

            @Override
            public String expectedBodyTemplate() {
                return toSingleLine("""
                    {
                        "path": "%s",
                        "status": 500,
                        "error": "Internal Server Error",
                        "message": "Unexpected exception occurred",
                        "localizedMessage": "Unexpected exception occurred"
                    }
                    """);
            }
        };
    }

    @Override
    public final Object[] get() {
        return new Object[]{exceptionFromService(), expectedStatus(), expectedBodyTemplate()};
    }

    public abstract Exception exceptionFromService();

    public abstract HttpStatus expectedStatus();

    public abstract String expectedBodyTemplate();

    private ExceptionArguments() {}
}
