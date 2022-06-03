package com.github.pannowak.mealsadvisor.web.filter.response.log;

import lombok.Value;

import java.util.Map;

@Value
class ResponseInfo {

    String prefix;
    int code;
    Map<String, String> headers;
    String body;
}
