package com.github.pannowak.mealsadvisor.web.filter.request.log;

import lombok.Value;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.util.Map;

@Value
class RequestInfo {

    String prefix;
    HttpMethod method;
    URI url;
    Map<String, String> headers;
    String body;
}
