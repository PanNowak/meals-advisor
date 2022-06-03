package com.github.pannowak.mealsadvisor.web.service;

import com.github.pannowak.mealsadvisor.api.products.model.Product;
import com.github.pannowak.mealsadvisor.api.products.model.ProductSummary;
import com.github.pannowak.mealsadvisor.api.products.service.ProductService;
import com.github.pannowak.mealsadvisor.web.api.model.ProductDTO;
import com.github.pannowak.mealsadvisor.web.client.ProductClient;
import com.github.pannowak.mealsadvisor.web.mapper.ModelTransformer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RestProductService implements ProductService {

    private final ProductClient productClient;
    private final ModelTransformer modelTransformer;

    RestProductService(ProductClient productClient, ModelTransformer modelTransformer) {
        this.productClient = productClient;
        this.modelTransformer = modelTransformer;
    }

    @Override
    public Flux<ProductSummary> getAll() {
        return productClient.getAll()
                .map(modelTransformer.to(ProductSummary.class));
    }

    @Override
    public Mono<Product> getById(Long id) {
        return productClient.getById(id)
                .map(modelTransformer.to(Product.class));
    }

    @Override
    public Mono<Product> save(Product product) {
        return Mono.just(product)
                .map(modelTransformer.to(ProductDTO.class))
                .transform(productDto -> saveInternal(product.getId(), productDto))
                .map(modelTransformer.to(Product.class));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return productClient.deleteById(id);
    }

    private Mono<ProductDTO> saveInternal(Long id, Mono<ProductDTO> productDto) {
        if (id == null) {
            return productClient.create(productDto);
        } else {
            return productClient.update(id, productDto);
        }
    }
}
