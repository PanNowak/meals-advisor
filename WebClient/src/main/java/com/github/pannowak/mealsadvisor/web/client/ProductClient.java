package com.github.pannowak.mealsadvisor.web.client;

import com.github.pannowak.mealsadvisor.web.api.model.ProductDTO;
import com.github.pannowak.mealsadvisor.web.api.model.ProductSummaryDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductClient {

    private static final String BASE_PATH = "/products";
    private static final String ID_PATH = BASE_PATH + "/{id}";

    private final WebClient webClient;

    ProductClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<ProductSummaryDTO> getAll() {
        return webClient.get()
                .uri(BASE_PATH)
                .retrieve()
                .bodyToFlux(ProductSummaryDTO.class);
    }

    public Mono<ProductDTO> getById(Long id) {
        return webClient.get()
                .uri(ID_PATH, id)
                .retrieve()
                .bodyToMono(ProductDTO.class);
    }

    public Mono<ProductDTO> create(Mono<ProductDTO> product) {
        return webClient.post()
                .uri(BASE_PATH)
                .body(product, ProductDTO.class)
                .retrieve()
                .bodyToMono(ProductDTO.class);
    }

    public Mono<ProductDTO> update(Long id, Mono<ProductDTO> product) {
        return webClient.put()
                .uri(ID_PATH, id)
                .body(product, ProductDTO.class)
                .retrieve()
                .bodyToMono(ProductDTO.class);
    }

    public Mono<Void> deleteById(Long id) {
        return webClient.delete()
                .uri(ID_PATH, id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
