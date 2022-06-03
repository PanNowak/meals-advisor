package com.github.pannowak.mealsadvisor.web.utils.framework;

import com.github.pannowak.mealsadvisor.web.exception.ErrorHandlingConfiguration;
import com.github.pannowak.mealsadvisor.web.mapper.ModelTransformer;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WebFluxTest(properties = "spring.main.lazy-initialization=true")
@Import({ErrorHandlingConfiguration.class, ModelTransformer.class, TestClient.class})
public @interface ControllerTest {}
