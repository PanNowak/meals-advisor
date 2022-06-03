package com.github.pannowak.mealsadvisor.web.filter.response.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorResponse {

    @JsonProperty("path")
    private String urlPath;

    @JsonProperty("status")
    private int statusCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("localizedMessage")
    private String localizedMessage;

    @JsonProperty("requestId")
    private String requestId;
}
