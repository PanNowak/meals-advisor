package com.github.pannowak.mealsadvisor.web.client;

import com.github.pannowak.mealsadvisor.web.api.model.UnitDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.github.pannowak.mealsadvisor.web.client.MockResponseFactory.createResponseWithBody;
import static com.github.pannowak.mealsadvisor.web.client.MockWebServerFacade.startServer;
import static com.github.pannowak.mealsadvisor.web.client.RequestFilter.createFilterThatMatchesRequestsTo;
import static com.github.pannowak.mealsadvisor.web.client.TestStringUtils.toSingleLine;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class UnitClientTest {

    private static MockWebServerFacade mockWebServer;

    private UnitClient unitClient;

    @BeforeAll
    static void beforeAll() {
        mockWebServer = startServer();
    }

    @BeforeEach
    void setUp() {
        var baseUri = mockWebServer.getBaseUri();
        unitClient = new UnitClient(WebClient.create(baseUri));
    }

    @DisplayName("Should return all units provided by external service")
    @ParameterizedTest
    @ArgumentsSource(AllUnitsArgsProvider.class)
    void shouldReturnAllUnitsProvidedByExternalService(String responseBody, List<UnitDTO> expectedUnits) {
        mockWebServer.stubHttpExchange(
                createFilterThatMatchesRequestsTo("/units")
                        .withRequestMethod(HttpMethod.GET)
                        .withoutRequestBody(),
                createResponseWithBody(responseBody)
        );

        var actualUnits = unitClient.getAll().toIterable();

        assertThat(actualUnits)
                .usingFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedUnits);
    }

    @AfterAll
    static void afterAll() {
        mockWebServer.stopServer();
    }

    private static final class AllUnitsArgsProvider implements ArgumentsProvider {

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    arguments("[]", Collections.emptyList()),
                    singleUnitArgs(),
                    multipleUnitsArgs()
            );
        }

        private Arguments singleUnitArgs() {
            var responseBody = """
                    [{"id": 13, "name": "Grams"}]""";
            var expectedUnit = Collections.singletonList(
                    createUnitDto(13L, "Grams")
            );
            return arguments(responseBody, expectedUnit);
        }

        private Arguments multipleUnitsArgs() {
            var responseBody = toSingleLine("""
                    [
                        {"id": 1, "name": "Cups"},
                        {"id": 2, "name": "Spoons"},
                        {"id": 3, "name": "Packages"}
                    ]""");
            var expectedUnits = List.of(
                    createUnitDto(1L, "Cups"),
                    createUnitDto(2L, "Spoons"),
                    createUnitDto(3L, "Packages")
            );
            return arguments(responseBody, expectedUnits);
        }

        private UnitDTO createUnitDto(Long id, String name) {
            var unit = new UnitDTO();
            unit.setId(id);
            unit.setName(name);
            return unit;
        }
    }
}
