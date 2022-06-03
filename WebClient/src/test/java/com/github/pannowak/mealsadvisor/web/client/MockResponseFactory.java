package com.github.pannowak.mealsadvisor.web.client;

import okhttp3.mockwebserver.MockResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public final class MockResponseFactory {

    public static MockResponse createResponseWithBody(String body) {
        return new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(body);
    }

    private MockResponseFactory() {}
}
