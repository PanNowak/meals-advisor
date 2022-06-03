//package com.github.pannowak.mealsadvisor.web.controller;
//
//import com.github.pannowak.mealsadvisor.api.products.model.Product;
//import com.github.pannowak.mealsadvisor.api.products.model.ProductSummary;
//import com.github.pannowak.mealsadvisor.api.products.service.ProductService;
//import com.github.pannowak.mealsadvisor.web.utils.exception.ExceptionArgumentsProvider;
//import com.github.pannowak.mealsadvisor.web.utils.framework.ControllerTest;
//import com.github.pannowak.mealsadvisor.web.utils.framework.TestClient;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtensionContext;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.ArgumentsProvider;
//import org.junit.jupiter.params.provider.ArgumentsSource;
//import org.mockito.invocation.InvocationOnMock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Stream;
//
//import static com.github.pannowak.mealsadvisor.web.utils.model.ProductBuilder.createProduct;
//import static com.github.pannowak.mealsadvisor.web.utils.TestStringUtils.toSingleLine;
//import static org.junit.jupiter.params.provider.Arguments.arguments;
//import static org.mockito.AdditionalAnswers.answer;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//
//@ControllerTest
//class ProductControllerTest {
//
//    @MockBean
//    private ProductService productService;
//
//    @Autowired
//    private TestClient testClient;
//
//    @DisplayName("GET endpoints tests")
//    @Nested
//    class GetEndpoints {
//
//        @DisplayName("Should return JSON array of all products fetched from service")
//        @ParameterizedTest
//        @ArgumentsSource(AllProductsGetEndpointSuccessTestArgsProvider.class)
//        void shouldReturnAllProductsSummaries(List<ProductSummary> productSummaries,
//                                              String expectedResponseBody) {
//            given(productService.getAll()).willReturn(Flux.fromIterable(productSummaries));
//
//            var response = testClient.get("/products");
//
//            response.assertStatus(HttpStatus.OK);
//            response.assertBody(expectedResponseBody);
//        }
//
//        @DisplayName("Should return 5XX status code and message if service emits exception when asked for products")
//        @ParameterizedTest
//        @ArgumentsSource(ExceptionArgumentsProvider.ServerExceptions.class)
//        void shouldReturnErrorResponseIfServiceEmitsExceptionWhenAskedForProducts(Exception exceptionFromService,
//                                                                                  HttpStatus expectedStatus,
//                                                                                  String expectedResponseBodyTemplate) {
//            given(productService.getAll()).willReturn(Flux.error(exceptionFromService));
//
//            var response = testClient.get("/products");
//
//            response.assertStatus(expectedStatus);
//            response.assertBody(expectedResponseBodyTemplate.formatted("/products"));
//        }
//
//        @DisplayName("Should return JSON representation of single product found in service")
//        @ParameterizedTest
//        @ArgumentsSource(SingleProductGetEndpointSuccessTestArgsProvider.class)
//        void shouldReturnProductById(Long productId, Product product, String expectedResponseBody) {
//            given(productService.getById(productId)).willReturn(Mono.just(product));
//
//            var response = testClient.get("/products/" + productId);
//
//            response.assertStatus(HttpStatus.OK);
//            response.assertBody(expectedResponseBody);
//        }
//
//        @DisplayName("Should return error status code and message if service emits exception when asked for product by id")
//        @ParameterizedTest
//        @ArgumentsSource(ExceptionArgumentsProvider.AllExceptions.class)
//        void shouldReturnErrorResponseIfServiceEmitsExceptionWhenAskedForProductById(Exception exceptionFromService,
//                                                                                     HttpStatus expectedStatus,
//                                                                                     String expectedResponseBodyTemplate) {
//            given(productService.getById(14L)).willReturn(Mono.error(exceptionFromService));
//
//            var response = testClient.get("/products/14");
//
//            response.assertStatus(expectedStatus);
//            response.assertBody(expectedResponseBodyTemplate.formatted("/products/14"));
//        }
//    }
//
//    @DisplayName("POST endpoints tests")
//    @Nested
//    class PostEndpoints {
//
//        @DisplayName("Should return JSON representation of product passed to service without id")
//        @ParameterizedTest
//        @ArgumentsSource(ProductPostEndpointArgsProvider.class)
//        void shouldReturnTheSameProductWithoutId(String requestBody, String expectedResponseBody) {
//            given(productService.save(any(Product.class))).willAnswer(answer(Mono::just));
//
//            var response = testClient.post("/products", requestBody);
//
//            response.assertStatus(HttpStatus.CREATED);
//            response.assertBody(expectedResponseBody);
//        }
//
//        @DisplayName("Should return JSON representation of product passed to service with added id")
//        @ParameterizedTest
//        @ArgumentsSource(ProductPostEndpointArgsProvider2.class)
//        void shouldReturnTheSameProductWithAssignedId(String requestBody, Long assignedProductId,
//                                                      String expectedResponseBody) {
//            given(productService.save(any(Product.class)))
//                    .willAnswer(invocation -> getInputProductWithAssignedId(invocation, assignedProductId));
//
//            var response = testClient.post("/products", requestBody);
//
//            response.assertStatus(HttpStatus.CREATED);
//            response.assertBody(expectedResponseBody);
//        }
//
//        @DisplayName("Should return error status code and message if service emits exception when saving product")
//        @ParameterizedTest
//        @ArgumentsSource(ExceptionArgumentsProvider.AllExceptions.class)
//        void shouldReturnErrorResponseIfServiceEmitsExceptionWhenSavingProduct(Exception exceptionFromService,
//                                                                               HttpStatus expectedStatus,
//                                                                               String expectedResponseBodyTemplate) {
//            given(productService.save(
//                    createProduct(1L, "FailProduct").build())
//            ).willReturn(Mono.error(exceptionFromService));
//
//            var response = testClient.post("/products", """
//                    {"id": 1, "name": "FailProduct"}""");
//
//            response.assertStatus(expectedStatus);
//            response.assertBody(expectedResponseBodyTemplate.formatted("/products"));
//        }
//
//        private Mono<Product> getInputProductWithAssignedId(InvocationOnMock invocation, Long productId) {
//            Product inputProduct = invocation.getArgument(0);
//            inputProduct.setId(productId);
//            return Mono.just(inputProduct);
//        }
//    }
//
//    @DisplayName("PUT endpoints tests")
//    @Nested
//    class PutEndpoints {
//
//        @DisplayName("Should return JSON representation of product passed to service with id equal to the path variable")
//        @ParameterizedTest
//        @ArgumentsSource(ProductPutEndpointArgsProvider.class)
//        void shouldReturnTheSameProductWithPathVariableId(String requestBody, Long pathVariableId,
//                                                          String expectedResponseBody) {
//            given(productService.save(any(Product.class))).willAnswer(answer(Mono::just));
//
//            var response = testClient.put("/products/" + pathVariableId, requestBody);
//
//            response.assertStatus(HttpStatus.OK);
//            response.assertBody(expectedResponseBody);
//        }
//
//        @DisplayName("Should return error status code and message if service emits exception when updating product")
//        @ParameterizedTest
//        @ArgumentsSource(ExceptionArgumentsProvider.AllExceptions.class)
//        void shouldReturnErrorResponseIfServiceEmitsExceptionWhenUpdatingProduct(Exception exceptionFromService,
//                                                                                 HttpStatus expectedStatus,
//                                                                                 String expectedResponseBodyTemplate) {
//            given(productService.save(
//                    createProduct(15L, "AnotherFailProduct").build())
//            ).willReturn(Mono.error(exceptionFromService));
//
//            var response = testClient.put("/products/15", """
//                    {"id": 15, "name": "AnotherFailProduct"}""");
//
//            response.assertStatus(expectedStatus);
//            response.assertBody(expectedResponseBodyTemplate.formatted("/products/15"));
//        }
//    }
//
//    @DisplayName("DELETE endpoints tests")
//    @Nested
//    class DeleteEndpoints {
//
//        @DisplayName("Should pass product id from path variable to the service")
//        @Test
//        void shouldCallServiceWithPathVariableId() {
//            var response = testClient.delete("/products/135");
//
//            then(productService).should().deleteById(135L);
//            response.assertStatus(HttpStatus.NO_CONTENT);
//            response.assertNoBody();
//        }
//
//        @DisplayName("Should return error status code and message if service emits exception when deleting product")
//        @ParameterizedTest
//        @ArgumentsSource(ExceptionArgumentsProvider.AllExceptions.class)
//        void shouldReturnErrorResponseIfServiceEmitsExceptionWhenDeletingProduct(Exception exceptionFromService,
//                                                                                 HttpStatus expectedStatus,
//                                                                                 String expectedResponseBodyTemplate) {
//            given(productService.deleteById(69L)).willReturn(Mono.error(exceptionFromService));
//
//            var response = testClient.delete("/products/69");
//
//            response.assertStatus(expectedStatus);
//            response.assertBody(expectedResponseBodyTemplate.formatted("/products/69"));
//        }
//    }
//
//    private static final class AllProductsGetEndpointSuccessTestArgsProvider implements ArgumentsProvider {
//
//        @Override
//        public Stream<Arguments> provideArguments(ExtensionContext context) {
//            return Stream.of(
//                    arguments(Collections.emptyList(), "[]"),
//                    singleProductSummaryArgs(),
//                    multipleProductSummariesArgs()
//            );
//        }
//
//        private Arguments singleProductSummaryArgs() {
//            var productSummary = Collections.singletonList(
//                    new ProductSummary(314L, "Pasta")
//            );
//            var responseBody = """
//                        [{"id": 314, "name": "Pasta"}]
//                        """;
//            return arguments(productSummary, responseBody);
//        }
//
//        private Arguments multipleProductSummariesArgs() {
//            var productSummaries = List.of(
//                    new ProductSummary(1L, "Milk"),
//                    new ProductSummary(2L, "Bread"),
//                    new ProductSummary(3L, "Cheese")
//            );
//            var responseBody = """
//                        [{"id": 1, "name": "Milk"},{"id": 2, "name": "Bread"},{"id": 3, "name": "Cheese"}]
//                        """;
//            return arguments(productSummaries, responseBody);
//        }
//    }
//
//    private static final class SingleProductGetEndpointSuccessTestArgsProvider implements ArgumentsProvider {
//
//        @Override
//        public Stream<Arguments> provideArguments(ExtensionContext context) {
//            return Stream.of(
//                    productWithoutSecondaryUnitsArgs(),
//                    productWithSecondaryUnitsArgs()
//            );
//        }
//
//        private Arguments productWithoutSecondaryUnitsArgs() {
//            var product = createProduct(13L, "Cereal")
//                    .withPrimaryUnit(42L, "Packages")
//                    .build();
//            var responseBody = toSingleLine("""
//                    {
//                        "id": 13,
//                        "name": "Cereal",
//                        "primaryUnit": {
//                            "id": 42,
//                            "name": "Packages"
//                        },
//                        "secondaryUnits": [] 
//                    }""");
//            return arguments(13L, product, responseBody);
//        }
//
//        private Arguments productWithSecondaryUnitsArgs() {
//            var product = createProduct(5L, "Potato")
//                    .withPrimaryUnit(11L, "Grams")
//                    .withSecondaryUnit(7L, "13.431", "Cups")
//                    .withSecondaryUnit(2L, "0.231", "Spoons")
//                    .build();
//            var responseBody = toSingleLine("""
//                    {
//                        "id": 5,
//                        "name": "Potato",
//                        "primaryUnit": {
//                            "id": 11,
//                            "name": "Grams"
//                        },
//                        "secondaryUnits": [
//                         {
//                            "id": 7,
//                            "toPrimaryUnitRatio": 13.431,
//                            "unit": {
//                                "id": 7,
//                                "name": "Cups"
//                            }
//                         },
//                         {
//                            "id": 2,
//                            "toPrimaryUnitRatio": 0.231,
//                            "unit": {
//                                "id": 2,
//                                "name": "Spoons"
//                            }
//                         }
//                        ]
//                    }""");
//            return arguments(5L, product, responseBody);
//        }
//    }
//
//    private static final class ProductPostEndpointArgsProvider implements ArgumentsProvider {
//
//        @Override
//        public Stream<Arguments> provideArguments(ExtensionContext context) {
//            return Stream.of(
//                    productToSaveWithoutIdArgs(),
//                    productToSaveWithIdArgs()
//            );
//        }
//
//        private Arguments productToSaveWithoutIdArgs() {
//            var requestBody = """
//                    {"name": "Flour", "primaryUnit": {"id": 139, "name": "Grams"}}
//                    """;
//            var expectedResponseBody = """
//                    {"name": "Flour", "primaryUnit": {"id": 139, "name": "Grams"}, "secondaryUnits": []}
//                    """;
//            return arguments(requestBody, expectedResponseBody);
//        }
//
//        private Arguments productToSaveWithIdArgs() {
//            var requestBody = """
//                    {"id": 36, "name": "Cereal", "primaryUnit": {"id": 54, "name": "Grams"}}
//                    """;
//            var expectedResponseBody = """
//                    {"name": "Cereal", "primaryUnit": {"id": 54, "name": "Grams"},"secondaryUnits": []}
//                    """;
//            return arguments(requestBody, expectedResponseBody);
//        }
//    }
//
//    private static final class ProductPostEndpointArgsProvider2 implements ArgumentsProvider {
//
//        @Override
//        public Stream<Arguments> provideArguments(ExtensionContext context) {
//            return Stream.of(
//                    productToSaveWithoutIdArgs(),
//                    productToSaveWithIdArgs()
//            );
//        }
//
//        private Arguments productToSaveWithoutIdArgs() {
//            var requestBody = """
//                    {"name": "Flour", "primaryUnit": {"id": 139, "name": "Grams"}}
//                    """;
//            var assignedId = 42L;
//            var expectedResponseBody = toSingleLine("""
//                    {
//                        "id": 42,
//                        "name": "Flour",
//                        "primaryUnit": {"id": 139, "name": "Grams"},
//                        "secondaryUnits": []
//                    }
//                    """);
//            return arguments(requestBody, assignedId, expectedResponseBody);
//        }
//
//        private Arguments productToSaveWithIdArgs() {
//            var requestBody = """
//                    {"id": 36, "name": "Cereal", "primaryUnit": {"id": 54, "name": "Grams"}}
//                    """;
//            var assignedId = 13L;
//            var expectedResponseBody = toSingleLine("""
//                    {
//                        "id": 13,
//                        "name": "Cereal",
//                        "primaryUnit": {"id": 54, "name": "Grams"},
//                        "secondaryUnits": []
//                    }
//                    """);
//            return arguments(requestBody, assignedId, expectedResponseBody);
//        }
//    }
//
//    private static final class ProductPutEndpointArgsProvider implements ArgumentsProvider {
//
//        @Override
//        public Stream<Arguments> provideArguments(ExtensionContext context) {
//            return Stream.of(
//                    productToUpdateWithoutIdArgs(),
//                    productToUpdateWithIdArgs()
//            );
//        }
//
//        private Arguments productToUpdateWithoutIdArgs() {
//            var requestBody = """
//                    {"name": "Flour", "primaryUnit": {"id": 139, "name": "Grams"}}
//                    """;
//            var pathVariableId = 54L;
//            var expectedResponseBody = toSingleLine("""
//                    {
//                        "id": 54,
//                        "name": "Flour",
//                        "primaryUnit": {"id": 139, "name": "Grams"},
//                        "secondaryUnits": []
//                    }
//                    """);
//            return arguments(requestBody, pathVariableId, expectedResponseBody);
//        }
//
//        private Arguments productToUpdateWithIdArgs() {
//            var requestBody = """
//                    {"id": 93, "name": "Cereal", "primaryUnit": {"id": 54, "name": "Grams"}}
//                    """;
//            var pathVariableId = 67L;
//            var expectedResponseBody = toSingleLine("""
//                    {
//                        "id": 67,
//                        "name": "Cereal",
//                        "primaryUnit": {"id": 54, "name": "Grams"},
//                        "secondaryUnits": []
//                    }
//                    """);
//            return arguments(requestBody, pathVariableId, expectedResponseBody);
//        }
//    }
//}
//