package com.github.pannowak.mealsadvisor.web.filter.log.response;

import lombok.Value;

import java.util.Map;

@Value
public class ResponseInfo {

    String id;
    Integer code;
    Map<String, String> headers;
    String body;
}
