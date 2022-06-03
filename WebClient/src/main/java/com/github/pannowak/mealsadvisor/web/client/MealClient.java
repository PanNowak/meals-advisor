package com.github.pannowak.mealsadvisor.web.client;

import com.github.pannowak.mealsadvisor.web.api.model.MealDTO;
import com.github.pannowak.mealsadvisor.web.api.model.MealSummaryDTO;
import com.github.pannowak.mealsadvisor.web.api.model.MealTypeDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MealClient {

    private static final String BASE_PATH = "/meals";
    private static final String ID_PATH = BASE_PATH + "/{id}";
    private static final String TYPES_PATH = BASE_PATH + "/types";

    private final WebClient webClient;

    MealClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<MealSummaryDTO> getAll() {
        return webClient.get()
                .uri(BASE_PATH)
                .retrieve()
                .bodyToFlux(MealSummaryDTO.class);
    }

    public Flux<MealTypeDTO> getAllTypes() {
        return webClient.get()
                .uri(TYPES_PATH)
                .retrieve()
                .bodyToFlux(MealTypeDTO.class);
    }

    public Mono<MealDTO> getById(Long id) {
        return webClient.get()
                .uri(ID_PATH, id)
                .retrieve()
                .bodyToMono(MealDTO.class);
    }

    public Mono<MealDTO> create(Mono<MealDTO> meal) {
        return webClient.post()
                .uri(BASE_PATH)
                .body(meal, MealDTO.class)
                .retrieve()
                .bodyToMono(MealDTO.class);
    }

    public Mono<MealDTO> update(Long id, Mono<MealDTO> meal) {
        return webClient.put()
                .uri(ID_PATH, id)
                .body(meal, MealDTO.class)
                .retrieve()
                .bodyToMono(MealDTO.class);
    }

    public Mono<Void> deleteById(Long id) {
        return webClient.delete()
                .uri(ID_PATH, id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
