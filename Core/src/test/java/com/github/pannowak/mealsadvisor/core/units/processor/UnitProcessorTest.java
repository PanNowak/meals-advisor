//package com.github.pannowak.mealsadvisor.core.units.processor;
//
//import com.github.pannowak.mealsadvisor.api.exception.ServiceException;
//import com.github.pannowak.mealsadvisor.api.units.model.Unit;
//import com.github.pannowak.mealsadvisor.core.units.repository.UnitRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import reactor.util.context.Context;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//class UnitProcessorTest {
//
//    private static final List<Unit> TEST_UNITS = List.of(
//            new Unit("TestUnit1"), new Unit("TestUnit2"), new Unit("TestUnit3")
//    );
//
//    @Mock
//    private UnitRepository unitRepository;
//
//    private UnitProcessor unitProcessor;
//
//    @BeforeEach
//    void setUp() {
//        unitProcessor = new UnitProcessor(unitRepository, exceptionFactory);
//    }
//
//    @Test
//    void shouldReturnAllUnitsIfRepositoryThrowsNoException() {
//        given(unitRepository.findAll()).willReturn(TEST_UNITS);
//
//        List<Unit> actualUnits = unitProcessor.getAll(Context.empty());
//
//        assertEquals(TEST_UNITS, actualUnits);
//    }
//
//    @Test
//    void shouldThrowServiceExceptionIfRepositoryThrowsWhenAskedForAllUnits() {
//        given(unitRepository.findAll()).willThrow(RuntimeException.class);
//
//        assertThrows(ServiceException.class, () -> unitProcessor.getAll(Context.empty()));
//    }
//}
