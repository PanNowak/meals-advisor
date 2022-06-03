package com.github.pannowak.mealsadvisor.core;

import com.github.pannowak.springconfigurationprovider.SpringApplicationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

@SpringBootApplication
@PropertySource("classpath:core-application.properties")
public class MealsAdvisorCoreApplication implements SpringApplicationProvider {

    public static void main(String[] args) {
        SpringApplication.run(MealsAdvisorCoreApplication.class, args);
    }

    @Bean
    public Scheduler databaseScheduler(@Value("${spring.datasource.hikari.maximum-pool-size:10}") int threadPoolCount) {
        var executor = Executors.newFixedThreadPool(threadPoolCount);
        return Schedulers.fromExecutorService(executor);
    }
}
