package com.github.pannowak.mealsadvisor.web.client;

import com.github.pannowak.mealsadvisor.web.api.model.ProductDTO;
import com.github.pannowak.mealsadvisor.web.api.model.ProductSummaryDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.github.pannowak.mealsadvisor.web.client.MockResponseFactory.createResponseWithBody;
import static com.github.pannowak.mealsadvisor.web.client.MockWebServerFacade.startServer;
import static com.github.pannowak.mealsadvisor.web.client.ProductBuilder.createProduct;
import static com.github.pannowak.mealsadvisor.web.client.RequestFilter.createFilterThatMatchesRequestsTo;
import static com.github.pannowak.mealsadvisor.web.client.TestStringUtils.toSingleLine;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ProductClientTest {

    private static MockWebServerFacade mockWebServer;

    private ProductClient productClient;

    @BeforeAll
    static void beforeAll() {
        mockWebServer = startServer();
    }

    @BeforeEach
    void setUp() {
        var baseUri = mockWebServer.getBaseUri();
        productClient = new ProductClient(WebClient.create(baseUri));
    }

    @DisplayName("GET requests test")
    @Nested
    class GetRequests {

        @DisplayName("Should return all products provided by external service")
        @ParameterizedTest
        @ArgumentsSource(AllProductsGetRequestArgsProvider.class)
        void shouldReturnAllProductSummariesProvidedByExternalService(String responseBody,
                                                                      List<ProductSummaryDTO> expectedProducts) {
            mockWebServer.stubHttpExchange(
                    createFilterThatMatchesRequestsTo("/products")
                            .withRequestMethod(HttpMethod.GET)
                            .withoutRequestBody(),
                    createResponseWithBody(responseBody)
            );

            var actualProducts = productClient.getAll().toIterable();

            assertThat(actualProducts)
                    .usingFieldByFieldElementComparator()
                    .containsExactlyElementsOf(expectedProducts);
        }

        @DisplayName("Should return product requested from external service")
        @ParameterizedTest
        @ArgumentsSource(SingleProductGetRequestArgsProvider.class)
        void shouldReturnProductRequestedFromExternalService(Long productId, String responseBody,
                                                             ProductDTO expectedProduct) {
            mockWebServer.stubHttpExchange(
                    createFilterThatMatchesRequestsTo("/products/" + productId)
                            .withRequestMethod(HttpMethod.GET)
                            .withoutRequestBody(),
                    createResponseWithBody(responseBody)
            );

            var actualProduct = productClient.getById(productId).block();

            assertThat(actualProduct)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedProduct);
        }
    }

    @DisplayName("POST requests tests")
    @Nested
    class PostRequests {

        @DisplayName("Should POST product to external service")
        @Test
        void shouldPostProductToExternalService() {
            mockWebServer.stubHttpExchange(
                    createFilterThatMatchesRequestsTo("/products")
                            .withRequestMethod(HttpMethod.POST)
                            .withRequestBody("""
                                    {
                                     "name": "Cereal", 
                                     "primaryUnit": {"id": 42, "name": "Packages"},
                                     "secondaryUnits": []
                                    }
                                    """),
                    createResponseWithBody("""
                                    {
                                     "id": 13,
                                     "name": "Cereal", 
                                     "primaryUnit": {"id": 42, "name": "Packages"},
                                     "secondaryUnits": []
                                    }
                                    """)
            );

            var actualProduct = productClient.create(Mono.just(
                    createProduct("Cereal")
                            .withPrimaryUnit(42L, "Packages")
                            .build()
            )).block();

            assertThat(actualProduct)
                    .usingRecursiveComparison()
                    .isEqualTo(
                            createProduct(13L, "Cereal")
                                    .withPrimaryUnit(42L, "Packages")
                                    .build()
                    );
        }
    }

    @AfterAll
    static void afterAll() {
        mockWebServer.stopServer();
    }

    private static final class AllProductsGetRequestArgsProvider implements ArgumentsProvider {

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    arguments("[]", Collections.emptyList()),
                    singleProductArgs(),
                    multipleUnitsArgs()
            );
        }

        private Arguments singleProductArgs() {
            var responseBody = """
                    [{"id": 52, "name": "Pasta"}]""";
            var expectedProduct = Collections.singletonList(
                    createProductSummary(52L, "Pasta")
            );
            return arguments(responseBody, expectedProduct);
        }

        private Arguments multipleUnitsArgs() {
            var responseBody = toSingleLine("""
                    [
                        {"id": 4, "name": "Bread"},
                        {"id": 5, "name": "Eggs"},
                        {"id": 6, "name": "Butter"}
                    ]""");
            var expectedProducts = List.of(
                    createProductSummary(4L, "Bread"),
                    createProductSummary(5L, "Eggs"),
                    createProductSummary(6L, "Butter")
            );
            return arguments(responseBody, expectedProducts);
        }

        private ProductSummaryDTO createProductSummary(Long id, String name) {
            var product = new ProductSummaryDTO();
            product.setId(id);
            product.setName(name);
            return product;
        }
    }

    private static final class SingleProductGetRequestArgsProvider implements ArgumentsProvider {

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    productWithoutSecondaryUnitsArgs(),
                    productWithSecondaryUnitsArgs()
            );
        }

        private Arguments productWithoutSecondaryUnitsArgs() {
            var responseBody = toSingleLine("""
                    {
                        "id": 13,
                        "name": "Cereal",
                        "primaryUnit": {
                            "id": 42,
                            "name": "Packages"
                        },
                        "secondaryUnits": [] 
                    }""");
            var product = createProduct(13L, "Cereal")
                    .withPrimaryUnit(42L, "Packages")
                    .build();
            return arguments(13L, responseBody, product);
        }

        private Arguments productWithSecondaryUnitsArgs() {
            var responseBody = toSingleLine("""
                    {
                        "id": 5,
                        "name": "Potato",
                        "primaryUnit": {
                            "id": 11,
                            "name": "Grams"
                        },
                        "secondaryUnits": [
                         {
                            "id": 7,
                            "toPrimaryUnitRatio": 13.431,
                            "unit": {
                                "id": 7,
                                "name": "Cups"
                            }
                         },
                         {
                            "id": 2,
                            "toPrimaryUnitRatio": 0.231,
                            "unit": {
                                "id": 2,
                                "name": "Spoons"
                            }
                         }
                        ]
                    }""");
            var product = createProduct(5L, "Potato")
                    .withPrimaryUnit(11L, "Grams")
                    .withSecondaryUnit(7L, "13.431", "Cups")
                    .withSecondaryUnit(2L, "0.231", "Spoons")
                    .build();
            return arguments(5L, responseBody, product);
        }
    }
}
