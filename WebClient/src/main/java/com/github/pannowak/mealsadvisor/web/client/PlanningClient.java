package com.github.pannowak.mealsadvisor.web.client;

import com.github.pannowak.mealsadvisor.web.api.model.DayPlanDTO;
import com.github.pannowak.mealsadvisor.web.api.model.GroceryItemDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PlanningClient {

    private static final String BASE_PATH = "/planning";
    private static final String DRAW_PATH = BASE_PATH + "/draw";
    private static final String GENERATE_PATH = BASE_PATH + "/shopping-list";

    private final WebClient webClient;

    PlanningClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<DayPlanDTO> draw(int firstDay, int lastDay) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(DRAW_PATH)
                        .queryParam("first-day", firstDay)
                        .queryParam("last-day", lastDay)
                        .build())
                .retrieve()
                .bodyToFlux(DayPlanDTO.class);
    }

    public Flux<GroceryItemDTO> generateShoppingList(Collection<Long> mealIds) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(GENERATE_PATH)
                        .queryParam("meal-id", concatIds(mealIds))
                        .build())
                .retrieve()
                .bodyToFlux(GroceryItemDTO.class);
    }

    private String concatIds(Collection<Long> mealIds) {
        return mealIds.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(","));
    }
}
