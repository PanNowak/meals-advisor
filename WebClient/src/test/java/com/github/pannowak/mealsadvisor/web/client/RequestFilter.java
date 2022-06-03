package com.github.pannowak.mealsadvisor.web.client;

import okhttp3.mockwebserver.RecordedRequest;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpMethod;

import java.util.function.Predicate;

public abstract class RequestFilter implements Predicate<RecordedRequest> {

    public static NeedsMethod createFilterThatMatchesRequestsTo(String path) {
        return new RequestFilterBuilder(path);
    }

    private static final class RequestFilterBuilder implements NeedsMethod, CanAddBody {

        private final String path;
        private HttpMethod method;

        private RequestFilterBuilder(String path) {
            this.path = path;
        }

        @Override
        public CanAddBody withRequestMethod(HttpMethod method) {
            this.method = method;
            return this;
        }

        @Override
        public RequestFilter withRequestBody(String body) {
            return new RequestFilter() {
                @Override
                public boolean test(RecordedRequest recordedRequest) {
                    return path.equals(recordedRequest.getPath()) &&
                            method.matches(recordedRequest.getMethod()) &&
                            isEqualRequestBody(body, recordedRequest.getBody().readUtf8());
                }
            };
        }

        @Override
        public RequestFilter withoutRequestBody() {
            return new RequestFilter() {
                @Override
                public boolean test(RecordedRequest recordedRequest) {
                    return path.equals(recordedRequest.getPath()) &&
                            method.matches(recordedRequest.getMethod());
                }
            };
        }

        private boolean isEqualRequestBody(String expectedBody, String actualBody) {
            try {
                return JSONCompare.compareJSON(expectedBody, actualBody, JSONCompareMode.LENIENT).passed();
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public interface NeedsMethod {

        CanAddBody withRequestMethod(HttpMethod method);
    }

    public interface CanAddBody {

        RequestFilter withRequestBody(String body);

        RequestFilter withoutRequestBody();
    }

    private RequestFilter() {}
}
