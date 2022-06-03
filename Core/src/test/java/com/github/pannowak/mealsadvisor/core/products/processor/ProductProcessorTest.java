package com.github.pannowak.mealsadvisor.core.products.processor;

import com.github.pannowak.mealsadvisor.api.exception.ClientException;
import com.github.pannowak.mealsadvisor.api.exception.ServiceException;
import com.github.pannowak.mealsadvisor.api.products.model.ProductSummary;
import com.github.pannowak.mealsadvisor.core.MockClientException;
import com.github.pannowak.mealsadvisor.core.MockServiceException;
import com.github.pannowak.mealsadvisor.core.exception.ExceptionFactory;
import com.github.pannowak.mealsadvisor.core.products.model.ProductEntity;
import com.github.pannowak.mealsadvisor.core.products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ProductProcessorTest {

    private static final List<ProductSummary> TEST_PRODUCT_SUMMARIES = List.of(
            new ProductSummary(1L, "TestProductSummary1"),
            new ProductSummary(2L, "TestProductSummary2"),
            new ProductSummary(3L, "TestProductSummary3")
    );

    private static final ProductEntity TEST_QUERIED_PRODUCT = createTestProduct("queriedProduct");
    private static final ProductEntity TEST_PRODUCT_TO_SAVE = createTestProduct("productToSave");
    private static final ProductEntity SAVED_TEST_PRODUCT = createTestProduct("savedProduct");

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ExceptionFactory exceptionFactory;

    private ProductProcessor productProcessor;

    @BeforeEach
    void setUp() {
        productProcessor = new ProductProcessor(productRepository, exceptionFactory);
    }

    @Test
    void shouldReturnAllProductsIfRepositoryThrowsNoException() {
        given(productRepository.findAllProjectedBy()).willReturn(TEST_PRODUCT_SUMMARIES);

        List<ProductSummary> actualProductSummaries = productProcessor.getAll();

        assertEquals(TEST_PRODUCT_SUMMARIES, actualProductSummaries);
    }

    @Test
    void shouldThrowServiceExceptionIfRepositoryThrowsWhenAskedForAllProductSummaries() {
        given(productRepository.findAllProjectedBy()).willThrow(RuntimeException.class);
        given(exceptionFactory.multiFetchException(any(), any())).willThrow(MockServiceException.class);

        assertThrows(ServiceException.class, () -> productProcessor.getAll());
    }

    @Test
    void shouldReturnRequestedProductIfExistsInRepository() {
        given(productRepository.findById(1L))
                .willReturn(Optional.of(TEST_QUERIED_PRODUCT));

        ProductEntity actualProduct = productProcessor.getById(1L);

        assertEquals(TEST_QUERIED_PRODUCT, actualProduct);
    }

    @Test
    void shouldThrowClientExceptionIfRequestProductDoesNotExistInRepository() {
        given(productRepository.findById(2L)).willReturn(Optional.empty());
        given(exceptionFactory.entityNotFoundException(any(), any()))
                .willThrow(MockClientException.class);

        assertThrows(ClientException.class, () -> productProcessor.getById(2L));
    }

    @Test
    void shouldThrowServiceExceptionIfRepositoryThrowsWhenAskedForProduct() {
        given(productRepository.findById(anyLong())).willThrow(RuntimeException.class);
        given(exceptionFactory.singleFetchException(any(), any(), any()))
                .willThrow(MockServiceException.class);

        assertThrows(ServiceException.class, () -> productProcessor.getById(1L));
    }

    @Test
    void shouldReturnProductFromRepositoryAfterSaving() {
        given(productRepository.save(TEST_PRODUCT_TO_SAVE)).willReturn(SAVED_TEST_PRODUCT);

        ProductEntity actualSavedProduct = productProcessor.save(TEST_PRODUCT_TO_SAVE);

        assertEquals(SAVED_TEST_PRODUCT, actualSavedProduct);
    }

    @Test
    void shouldThrowServiceExceptionIfRepositoryThrowsWhileSaving() {
        given(productRepository.save(any(ProductEntity.class))).willThrow(RuntimeException.class);
        given(exceptionFactory.savingException(any(), any())).willThrow(MockServiceException.class);

        assertThrows(ServiceException.class, () -> productProcessor.save(new ProductEntity()));
    }

    @Test
    void shouldCallRepositoryWhenDeletingProduct() {
        productProcessor.removeById(3L);

        then(productRepository).should().deleteById(3L);
    }

    @Test
    void shouldThrowServiceExceptionIfRepositoryThrowsWhileDeleting() {
        willThrow(RuntimeException.class).given(productRepository).deleteById(anyLong());
        given(exceptionFactory.removalException(any(), any(), any()))
                .willThrow(MockServiceException.class);

        assertThrows(ServiceException.class, () -> productProcessor.removeById(314L));
    }

    private static ProductEntity createTestProduct(String name) {
        var product = new ProductEntity();
        product.setName(name);
        return product;
    }
}
