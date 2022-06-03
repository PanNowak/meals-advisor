//package com.github.pannowak.mealsadvisor.web.controller;
//
//import com.github.pannowak.mealsadvisor.api.planning.model.DayPlan;
//import com.github.pannowak.mealsadvisor.api.planning.model.GroceryItem;
//import com.github.pannowak.mealsadvisor.api.planning.service.PlanningService;
//import com.github.pannowak.mealsadvisor.api.products.model.ProductSummary;
//import com.github.pannowak.mealsadvisor.api.units.model.Unit;
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
//import java.math.BigDecimal;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static com.github.pannowak.mealsadvisor.web.utils.TestStringUtils.toSingleLine;
//import static com.github.pannowak.mealsadvisor.web.utils.model.DayPlanBuilder.TestMealType.*;
//import static com.github.pannowak.mealsadvisor.web.utils.model.DayPlanBuilder.createDayPlan;
//import static org.junit.jupiter.params.provider.Arguments.arguments;
//import static org.mockito.BDDMockito.given;
//
//@ControllerTest
//class PlanningControllerTest {
//
//    @MockBean
//    private PlanningService planningService;
//
//    @Autowired
//    private TestClient testClient;
//
//    @DisplayName("GET endpoints tests")
//    @Nested
//    class GetEndpoints {
//
//        @DisplayName("Should return JSON array of day plans drawn from service")
//        @ParameterizedTest
//        @ArgumentsSource(DrawnDayPlansSuccessArgsProvider.class)
//        void shouldReturnDayPlansDrawnFromService(int firstDay, int lastDay, List<DayPlan> dayPlans,
//                                                  String expectedResponseBody) {
//            given(planningService.draw(firstDay, lastDay)).willReturn(Flux.fromIterable(dayPlans));
//
//            var response = testClient
//                    .get("/planning/draw?first-day=%s&last-day=%s".formatted(firstDay, lastDay));
//
//            response.assertStatus(HttpStatus.OK);
//            response.assertBody(expectedResponseBody);
//        }
//
//        @DisplayName("Should return error status code and message if service emits exception when day plans are drawn")
//        @ParameterizedTest
//        @ArgumentsSource(ExceptionArgumentsProvider.AllExceptions.class)
//        void shouldReturnErrorResponseIfServiceEmitsExceptionWhenDrawingDayPlans(Exception exceptionFromService,
//                                                                                 HttpStatus expectedStatus,
//                                                                                 String expectedResponseBodyTemplate) {
//            given(planningService.draw(5, 2)).willReturn(Flux.error(exceptionFromService));
//
//            var response = testClient.get("/planning/draw?first-day=5&last-day=2");
//
//            response.assertStatus(expectedStatus);
//            response.assertBody(expectedResponseBodyTemplate.formatted("/planning/draw"));
//        }
//
//        @DisplayName("Should return JSON array of grocery items fetched from service")
//        @ParameterizedTest
//        @ArgumentsSource(GroceryItemsSuccessArgsProvider.class)
//        void shouldReturnGroceryItemsFetchedFromService(List<Long> mealIds, List<GroceryItem> groceryItems,
//                                                        String expectedResponseBody) {
//            given(planningService.generateShoppingList(mealIds)).willReturn(Flux.fromIterable(groceryItems));
//
//            var response = testClient
//                    .get("/planning/shopping-list?meal-id=" + asCommaSeparatedString(mealIds));
//
//            response.assertStatus(HttpStatus.OK);
//            response.assertBody(expectedResponseBody);
//        }
//
//        //TODO take care of exceptions
//
//        private String asCommaSeparatedString(List<Long> ids) {
//            return ids.stream().map(Object::toString).collect(Collectors.joining(","));
//        }
//
//    }
//
//    private static final class DrawnDayPlansSuccessArgsProvider implements ArgumentsProvider {
//
//        @Override
//        public Stream<Arguments> provideArguments(ExtensionContext context) {
//            return Stream.of(
//                    singleDayPlanArgs(),
//                    multipleDayPlansArgs()
//            );
//        }
//
//        private Arguments singleDayPlanArgs() {
//            var dayPlan = Collections.singletonList(
//                    createDayPlan(4)
//                            .withMealInfo(BREAKFAST, "Scrambled eggs")
//                            .withMealInfo(LUNCH, "Spaghetti")
//                            .build()
//            );
//            var expectedResponseBody = toSingleLine("""
//                    [
//                     {
//                        "dayOfWeek": 4,
//                        "mealsByType": {
//                            "1. Breakfast": {
//                                "id": 1,
//                                "name": "Scrambled eggs"
//                            },
//                            "2. Lunch": {
//                                "id": 2,
//                                "name": "Spaghetti"
//                            }
//                        }
//                     }
//                    ]
//                    """);
//            return arguments(4, 4, dayPlan, expectedResponseBody);
//        }
//
//        private Arguments multipleDayPlansArgs() {
//            var dayPlans = List.of(
//                    createDayPlan(1)
//                            .withMealInfo(BREAKFAST, "Cereals")
//                            .withMealInfo(LUNCH, "Hamburger")
//                            .build(),
//                    createDayPlan(2)
//                            .withMealInfo(BREAKFAST, "Pancakes")
//                            .build(),
//                    createDayPlan(3)
//                            .withMealInfo(BREAKFAST, "Sandwiches")
//                            .withMealInfo(LUNCH, "Salad")
//                            .withMealInfo(DINNER, "Duck")
//                            .build()
//            );
//            var expectedResponseBody = toSingleLine("""
//                    [
//                     {
//                        "dayOfWeek": 1,
//                        "mealsByType": {
//                            "1. Breakfast": {
//                                "id": 1,
//                                "name": "Cereals"
//                            },
//                            "2. Lunch": {
//                                "id": 2,
//                                "name": "Hamburger"
//                            }
//                        }
//                     },
//                     {
//                        "dayOfWeek": 2,
//                        "mealsByType": {
//                            "1. Breakfast": {
//                                "id": 1,
//                                "name": "Pancakes"
//                            }
//                        }
//                     },
//                     {
//                        "dayOfWeek": 3,
//                        "mealsByType": {
//                            "1. Breakfast": {
//                                "id": 1,
//                                "name": "Sandwiches"
//                            },
//                            "2. Lunch": {
//                                "id": 2,
//                                "name": "Salad"
//                            },
//                            "3. Dinner": {
//                                "id": 3,
//                                "name": "Duck"
//                            }
//                        }
//                     }
//                    ]
//                    """);
//            return arguments(1, 3, dayPlans, expectedResponseBody);
//        }
//    }
//
//    private static final class GroceryItemsSuccessArgsProvider implements ArgumentsProvider {
//
//        @Override
//        public Stream<Arguments> provideArguments(ExtensionContext context) {
//            return Stream.of(
//                    singleGroceryItemArgs(),
//                    multipleGroceryItemsArgs()
//            );
//        }
//
//        private Arguments singleGroceryItemArgs() {
//            var mealIds = List.of(13L, 42L);
//            var groceryItems = List.of(
//                    createGroceryItem("Flour", "1.323", "Cups")
//            );
//            var expectedResponseBody = toSingleLine("""
//                    [
//                     {
//                         "productSummary": {
//                             "id": 1,
//                             "name": "Flour"
//                         },
//                         "numberOfUnits": 1.323,
//                         "unit": {
//                             "id": 3,
//                             "name": "Cups"
//                         }
//                     }
//                    ]
//                    """);
//            return arguments(mealIds, groceryItems, expectedResponseBody);
//        }
//
//        private Arguments multipleGroceryItemsArgs() {
//            var mealIds = List.of(53L, 12L, 64L);
//            var groceryItems = List.of(
//                    createGroceryItem("Egg", "4.000", "Units"),
//                    createGroceryItem("Sugar", "1.323", "Spoons"),
//                    createGroceryItem("Flour", "154.522", "Grams")
//            );
//            var expectedResponseBody = toSingleLine("""
//                    [
//                     {
//                         "productSummary": {
//                             "id": 1,
//                             "name": "Egg"
//                         },
//                         "numberOfUnits": 4.000,
//                         "unit": {
//                             "id": 3,
//                             "name": "Units"
//                         }
//                     },
//                     {
//                         "productSummary": {
//                             "id": 1,
//                             "name": "Sugar"
//                         },
//                         "numberOfUnits": 1.323,
//                         "unit": {
//                             "id": 3,
//                             "name": "Spoons"
//                         }
//                     },
//                     {
//                         "productSummary": {
//                             "id": 1,
//                             "name": "Flour"
//                         },
//                         "numberOfUnits": 154.522,
//                         "unit": {
//                             "id": 3,
//                             "name": "Grams"
//                         }
//                     }
//                    ]
//                    """);
//            return arguments(mealIds, groceryItems, expectedResponseBody);
//        }
//
//        private GroceryItem createGroceryItem(String productName, String numberOfUnits, String unitName) {
//            var unit = new Unit(unitName);
//            unit.setId(3L);
//            return new GroceryItem(new ProductSummary(1L, productName), new BigDecimal(numberOfUnits), unit);
//        }
//    }
//}
//