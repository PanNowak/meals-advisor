package com.github.pannowak.mealsadvisor.web.client;

import com.github.pannowak.mealsadvisor.web.api.model.UnitDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class UnitClient {

    private static final String BASE_PATH = "/units";
    private static final String ID_PATH = BASE_PATH + "/{id}";

    private final WebClient webClient;

    UnitClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<UnitDTO> getAll() {
        return webClient.get()
                .uri(BASE_PATH)
                .retrieve()
                .bodyToFlux(UnitDTO.class);
    }

    public Mono<UnitDTO> getById(Long id) {
        return webClient.get()
                .uri(ID_PATH, id)
                .retrieve()
                .bodyToMono(UnitDTO.class);
    }

    public Mono<UnitDTO> create(Mono<UnitDTO> unit) {
        return webClient.post()
                .uri(BASE_PATH)
                .body(unit, UnitDTO.class)
                .retrieve()
                .bodyToMono(UnitDTO.class);
    }

    public Mono<UnitDTO> update(Long id, Mono<UnitDTO> unit) {
        return webClient.put()
                .uri(ID_PATH, id)
                .body(unit, UnitDTO.class)
                .retrieve()
                .bodyToMono(UnitDTO.class);
    }

    public Mono<Void> deleteById(Long id) {
        return webClient.delete()
                .uri(ID_PATH, id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
