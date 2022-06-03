package com.github.pannowak.mealsadvisor.web.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Configuration
public class ErrorHandlingConfiguration {

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {

            @Override
            public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions ignored) {
                Map<String, Object> errorAttributes =
                        super.getErrorAttributes(request, ErrorAttributeOptions.of(Include.MESSAGE));

                Throwable error = getError(request);
                String localizedMessage = extractLocalizedMessage(error);
                errorAttributes.put("localizedMessage", localizedMessage);

                return errorAttributes;
            }

            private String extractLocalizedMessage(Throwable error) {
                if (error instanceof LocalizedResponseStatusException) {
                    return ((LocalizedResponseStatusException) error).getLocalizedReason();
                } else if (error instanceof ResponseStatusException) {
                    return ((ResponseStatusException) error).getReason();
                } else {
                    return error.getLocalizedMessage();
                }
            }
        };
    }
}
