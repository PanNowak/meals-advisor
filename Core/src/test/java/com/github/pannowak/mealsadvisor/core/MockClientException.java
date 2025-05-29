package com.github.pannowak.mealsadvisor.core;

import com.github.pannowak.mealsadvisor.api.exception.ClientException;

public final class MockClientException extends ClientException {

    public MockClientException() {
        super("Mock client exception message");
    }

    @Override
    public String getMessage() {
        return "It's mock ClientException";
    }
}
