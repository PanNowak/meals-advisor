//package com.github.pannowak.mealsadvisor.core.base;
//
//import com.github.pannowak.mealsadvisor.api.exception.ServiceException;
//import com.github.pannowak.mealsadvisor.core.MockServiceException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.reactivestreams.Publisher;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.core.scheduler.Schedulers;
//import reactor.test.StepVerifier;
//
//import java.util.List;
//
//import static org.mockito.BDDMockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class CoreCrudServiceTest {
//
//    private static final List<MockSummary> TEST_SUMMARIES = List.of(
//            new MockSummary(1L), new MockSummary(2L), new MockSummary(3L)
//    );
//
//    private static final MockEntity FETCHED_ENTITY = new MockEntity(10L);
//    private static final MockEntity ENTITY_TO_SAVE = new MockEntity(20L);
//    private static final MockEntity SAVED_ENTITY = new MockEntity(30L);
//
//    @Mock
//    private CrudProcessor<MockSummary, MockEntity> crudProcessor;
//
//    private CoreCrudService<MockSummary, MockEntity> crudService;
//
//    @BeforeEach
//    void setUp() {
//        crudService = new CoreCrudService<>(crudProcessor, Schedulers.immediate());
//    }
//
//    @Test
//    void shouldReturnAllSummariesIfProcessorReturnsAllSummaries() {
//        given(crudProcessor.getAll()).willReturn(TEST_SUMMARIES);
//
//        Flux<MockSummary> allSummaries = crudService.getAll();
//
//        assertEmitsValues(allSummaries, TEST_SUMMARIES.toArray(MockSummary[]::new));
//    }
//
//    @Test
//    void shouldEmitExceptionIfProcessorThrowsWhenAskedForAllSummaries() {
//        given(crudProcessor.getAll()).willThrow(MockServiceException.class);
//
//        Flux<MockSummary> allSummaries = crudService.getAll();
//
//        assertEmitsServiceException(allSummaries);
//    }
//
//    @Test
//    void shouldReturnEntityByIdIfProcessorReturnsEntity() {
//        given(crudProcessor.getById(10L)).willReturn(FETCHED_ENTITY);
//
//        Mono<MockEntity> fetchedEntity = crudService.getById(10L);
//
//        assertEmitsValues(fetchedEntity, FETCHED_ENTITY);
//    }
//
//    @Test
//    void shouldEmitExceptionIfProcessorThrowsWhenAskedForEntityById() {
//        given(crudProcessor.getById(123L)).willThrow(MockServiceException.class);
//
//        Mono<MockEntity> fetchedEntity = crudService.getById(123L);
//
//        assertEmitsServiceException(fetchedEntity);
//    }
//
//    @Test
//    void shouldReturnSavedEntityIfProcessorReturnsEntity() {
//        given(crudProcessor.save(ENTITY_TO_SAVE)).willReturn(SAVED_ENTITY);
//
//        Mono<MockEntity> savedEntity = crudService.save(ENTITY_TO_SAVE);
//
//        assertEmitsValues(savedEntity, SAVED_ENTITY);
//    }
//
//    @Test
//    void shouldEmitExceptionIfProcessorThrowsWhenSavingEntity() {
//        given(crudProcessor.save(ENTITY_TO_SAVE)).willThrow(MockServiceException.class);
//
//        Mono<MockEntity> savedEntity = crudService.save(ENTITY_TO_SAVE);
//
//        assertEmitsServiceException(savedEntity);
//    }
//
//    @Test
//    void shouldCallProcessorWhenDeletingEntity() {
//        crudService.deleteById(3L).block();
//
//        then(crudProcessor).should().removeById(3L);
//    }
//
//    @Test
//    void shouldEmitExceptionIfProcessorThrowsWhenDeletingEntity() {
//        willThrow(MockServiceException.class).given(crudProcessor).removeById(456L);
//
//        Mono<Void> completeSignal = crudService.deleteById(456L);
//
//        assertEmitsServiceException(completeSignal);
//    }
//
//    @SafeVarargs
//    private <T> void assertEmitsValues(Publisher<T> actualValuesPublisher, T... expectedValues) {
//        StepVerifier.create(actualValuesPublisher)
//                .expectNext(expectedValues)
//                .expectComplete()
//                .verify();
//    }
//
//    private void assertEmitsServiceException(Publisher<?> valuePublisher) {
//        StepVerifier.create(valuePublisher)
//                .expectError(ServiceException.class)
//                .verify();
//    }
//}
