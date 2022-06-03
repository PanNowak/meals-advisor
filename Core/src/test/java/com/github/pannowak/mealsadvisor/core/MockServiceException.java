package com.github.pannowak.mealsadvisor.core;

import com.github.pannowak.mealsadvisor.api.exception.ServiceException;

public final class MockServiceException extends ServiceException {

    @Override
    public String getMessage() {
        return "It's mock ServiceException";
    }
}
