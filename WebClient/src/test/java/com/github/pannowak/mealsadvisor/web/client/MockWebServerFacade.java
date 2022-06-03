package com.github.pannowak.mealsadvisor.web.client;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import java.io.IOException;
import java.io.UncheckedIOException;

public final class MockWebServerFacade {

    public static MockWebServerFacade startServer() {
        try {
            return new MockWebServerFacade();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private final MockWebServer mockWebServer;

    private MockWebServerFacade() throws IOException {
        this.mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    public String getBaseUri() {
        return String.format("http://%s:%s",
                mockWebServer.getHostName(), mockWebServer.getPort());
    }

    public void stubHttpExchange(RequestFilter requestFilter, MockResponse serverResponse) {
        var dispatcher = new StubbedDispatcher(requestFilter, serverResponse);
        mockWebServer.setDispatcher(dispatcher);
    }

    public void stopServer() {
        try {
            mockWebServer.shutdown();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static final class StubbedDispatcher extends Dispatcher {

        private static final MockResponse NOT_FOUND_RESPONSE = new MockResponse().setResponseCode(404);

        private final RequestFilter requestFilter;
        private final MockResponse mockResponse;

        public StubbedDispatcher(RequestFilter requestFilter, MockResponse mockResponse) {
            this.requestFilter = requestFilter;
            this.mockResponse = mockResponse;
        }

        @Override
        public MockResponse dispatch(RecordedRequest request) {
            if (requestFilter.test(request)) {
                return mockResponse;
            }

            return NOT_FOUND_RESPONSE;
        }
    }
}
