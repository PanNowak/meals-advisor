package com.github.pannowak.mealsadvisor.web.utils.framework;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;

@Component
public final class TestClient {

    private final WebTestClient delegateClient;

    TestClient(WebTestClient delegateClient) {
        this.delegateClient = delegateClient;
    }

    public TestResponse get(String uri) {
        WebTestClient.ResponseSpec responseSpec = delegateClient.get().uri(uri).exchange();
        return new TestResponse(responseSpec);
    }

    public TestResponse post(String uri, String body) {
        WebTestClient.ResponseSpec responseSpec = delegateClient.post().uri(uri).bodyValue(body)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).exchange();
        return new TestResponse(responseSpec);
    }

    public TestResponse put(String uri, String body) {
        WebTestClient.ResponseSpec responseSpec = delegateClient.put().uri(uri).bodyValue(body)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).exchange();
        return new TestResponse(responseSpec);
    }

    public TestResponse delete(String uri) {
        WebTestClient.ResponseSpec responseSpec = delegateClient.delete().uri(uri).exchange();
        return new TestResponse(responseSpec);
    }

    public static final class TestResponse {

        private final WebTestClient.ResponseSpec responseSpec;

        private TestResponse(WebTestClient.ResponseSpec responseSpec) {
            this.responseSpec = responseSpec;
        }

        public void assertStatus(HttpStatus status) {
            responseSpec.expectStatus().isEqualTo(status);
        }

        public void assertBody(String jsonBody) {
            responseSpec.expectBody().json(jsonBody);
        }

        public void assertNoBody() {
            responseSpec.expectBody().isEmpty();
        }
    }
}
