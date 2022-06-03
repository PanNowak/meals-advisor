package com.github.pannowak.mealsadvisor.core.products.processor;

import com.github.pannowak.mealsadvisor.api.meals.model.Meal;
import com.github.pannowak.mealsadvisor.api.products.model.Product;
import com.github.pannowak.mealsadvisor.api.products.model.ProductSummary;
import com.github.pannowak.mealsadvisor.core.exception.ExceptionFactory;
import com.github.pannowak.mealsadvisor.core.products.model.ProductEntity;
import com.github.pannowak.mealsadvisor.core.products.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@Slf4j
@Validated
@Component
public class ProductProcessor {

    private final ProductRepository productRepository;
    private final ExceptionFactory exceptionFactory;

    ProductProcessor(ProductRepository productRepository, ExceptionFactory exceptionFactory) {
        this.productRepository = productRepository;
        this.exceptionFactory = exceptionFactory;
    }

    public List<ProductSummary> getAll() {
        try {
            return productRepository.findAllProjectedBy();
        } catch (RuntimeException e) {
            throw exceptionFactory.multiFetchException(ProductSummary.class, e);
        }
    }

    public ProductEntity getById(@Positive Long productId) {
        return getByIdInternal(productId).orElseThrow(() ->
                exceptionFactory.entityNotFoundException(Product.class, productId));
    }

    public ProductEntity save(@Valid ProductEntity productToSave) {
        try {
            return productRepository.save(productToSave);
        } catch (RuntimeException e) {
            throw exceptionFactory.savingException(productToSave, e);
        }
    }

    public void removeById(@Positive Long productId) { //TODO weryfikować usunięcie
        try {
            productRepository.deleteById(productId);
        } catch (RuntimeException e) {
            handleRemovalException(productId, e);
        }
    }

    private Optional<ProductEntity> getByIdInternal(Long productId) {
        try {
            return productRepository.findById(productId);
        } catch (RuntimeException e) {
            throw exceptionFactory.singleFetchException(Product.class, productId, e);
        }
    }

    private void handleRemovalException(Long productId, RuntimeException e) {
        if (e instanceof EmptyResultDataAccessException) { //TODO obsłużyć na poziomie exceptionFactory
            log.info("Attempt to delete non-existent entity of type {} with id {}", Meal.class.getSimpleName(), productId);
        } else {
            throw exceptionFactory.removalException(Product.class, productId, e);
        }
    }
}
