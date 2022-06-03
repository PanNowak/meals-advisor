//package com.github.pannowak.mealsadvisor.web.controller;
//
//import com.github.pannowak.mealsadvisor.api.units.model.Unit;
//import com.github.pannowak.mealsadvisor.api.units.service.UnitService;
//import com.github.pannowak.mealsadvisor.web.utils.exception.ExceptionArgumentsProvider;
//import com.github.pannowak.mealsadvisor.web.utils.framework.ControllerTest;
//import com.github.pannowak.mealsadvisor.web.utils.framework.TestClient;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.extension.ExtensionContext;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.ArgumentsProvider;
//import org.junit.jupiter.params.provider.ArgumentsSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import reactor.core.publisher.Flux;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.params.provider.Arguments.arguments;
//import static org.mockito.BDDMockito.given;
//
//@ControllerTest
//class UnitControllerTest {
//
//    @MockBean
//    private UnitService unitService;
//
//    @Autowired
//    private TestClient testClient;
//
//    @DisplayName("GET endpoints tests")
//    @Nested
//    class GetEndpoints {
//
//        @DisplayName("Should return JSON array of all units fetched from service")
//        @ParameterizedTest
//        @ArgumentsSource(AllUnitsGetEndpointSuccessTestArgsProvider.class)
//        void shouldReturnAllUnits(List<Unit> units, String expectedResponseBody) {
//            given(unitService.getAll()).willReturn(Flux.fromIterable(units));
//
//            var response = testClient.get("/units");
//
//            response.assertStatus(HttpStatus.OK);
//            response.assertBody(expectedResponseBody);
//        }
//
//        @DisplayName("Should return 5XX status code and message if service emits exception when asked for units")
//        @ParameterizedTest
//        @ArgumentsSource(ExceptionArgumentsProvider.ServerExceptions.class)
//        void shouldReturnErrorResponseIfServiceEmitsExceptionWhenAskedForUnit(Exception exceptionFromService,
//                                                                              HttpStatus expectedStatus,
//                                                                              String expectedResponseBodyTemplate) {
//            given(unitService.getAll()).willReturn(Flux.error(exceptionFromService));
//
//            var response = testClient.get("/units");
//
//            response.assertStatus(expectedStatus);
//            response.assertBody(expectedResponseBodyTemplate.formatted("/units"));
//        }
//    }
//
//    private static final class AllUnitsGetEndpointSuccessTestArgsProvider implements ArgumentsProvider {
//
//        @Override
//        public Stream<Arguments> provideArguments(ExtensionContext context) {
//            return Stream.of(
//                    arguments(Collections.emptyList(), "[]"),
//                    singleUnitArgs(),
//                    multipleUnitsArgs()
//            );
//        }
//
//        private Arguments singleUnitArgs() {
//            var unit = Collections.singletonList(createUnit(1L, "Grams"));
//            var expectedResponseBody = """
//                    [{"id": 1, "name": "Grams"}]
//                    """;
//            return arguments(unit, expectedResponseBody);
//        }
//
//        private Arguments multipleUnitsArgs() {
//            var units = List.of(
//                    createUnit(13L, "Packages"),
//                    createUnit(42L, "Spoons"),
//                    createUnit(69L, "Cups")
//            );
//            var expectedResponseBody = """
//                    [{"id": 13, "name": "Packages"},{"id": 42, "name": "Spoons"},{"id": 69, "name": "Cups"}]
//                    """;
//            return arguments(units, expectedResponseBody);
//        }
//
//        private Unit createUnit(Long id, String name) {
//            var unit = new Unit();
//            unit.setId(id);
//            unit.setName(name);
//            return unit;
//        }
//    }
//}
//