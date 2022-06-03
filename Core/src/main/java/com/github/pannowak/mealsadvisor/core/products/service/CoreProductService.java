package com.github.pannowak.mealsadvisor.core.products.service;

import com.github.pannowak.mealsadvisor.api.products.model.Product;
import com.github.pannowak.mealsadvisor.api.products.model.ProductSummary;
import com.github.pannowak.mealsadvisor.api.products.service.ProductService;
import com.github.pannowak.mealsadvisor.core.mapper.ModelTransformer;
import com.github.pannowak.mealsadvisor.core.products.model.ProductEntity;
import com.github.pannowak.mealsadvisor.core.products.processor.ProductProcessor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.function.Function;

@Service
public class CoreProductService implements ProductService {

    private final ProductProcessor productProcessor;
    private final ModelTransformer modelTransformer;
    private final Scheduler databaseScheduler;

    CoreProductService(ProductProcessor productProcessor, ModelTransformer modelTransformer,
                       Scheduler databaseScheduler) {
        this.productProcessor = productProcessor;
        this.modelTransformer = modelTransformer;
        this.databaseScheduler = databaseScheduler;
    }

    @Override
    public Flux<ProductSummary> getAll() {
        return Mono.fromCallable(productProcessor::getAll)
                .subscribeOn(databaseScheduler)
                .flatMapIterable(Function.identity());
    }

    @Override
    public Mono<Product> getById(Long productId) {
        return Mono.fromCallable(() -> productProcessor.getById(productId))
                .subscribeOn(databaseScheduler)
                .map(modelTransformer.to(Product.class));
    }

    @Override
    public Mono<Product> save(Product productToSave) {
        return Mono.just(productToSave)
                .map(modelTransformer.to(ProductEntity.class))
                .publishOn(databaseScheduler)
                .map(productProcessor::save)
                .map(modelTransformer.to(Product.class));
    }

    @Override
    public Mono<Void> deleteById(Long productId) {
        return Mono.<Void>fromRunnable(() -> productProcessor.removeById(productId))
                .subscribeOn(databaseScheduler);
    }
}
