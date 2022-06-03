package com.github.pannowak.mealsadvisor.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiPathConfigurer {

    private final String apiPathPrefix;

    ApiPathConfigurer(@Value("${api.path.prefix:api}") String apiPathPrefix) {
        this.apiPathPrefix = apiPathPrefix;
    }

    public String getApiPathPrefix() {
        return apiPathPrefix;
    }

    public boolean isApiPath(String pathToCheck) {
        return pathToCheck.equals(apiPathPrefix) || pathToCheck.startsWith(apiPathPrefix + "/");
    }
}
