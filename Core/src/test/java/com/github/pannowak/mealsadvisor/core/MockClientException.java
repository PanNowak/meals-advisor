package com.github.pannowak.mealsadvisor.core;

import com.github.pannowak.mealsadvisor.api.exception.ClientException;

public final class MockClientException extends ClientException {

    @Override
    public String getMessage() {
        return "It's mock ClientException";
    }
}
