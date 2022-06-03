package com.github.pannowak.mealsadvisor.web.controller;

import com.github.pannowak.mealsadvisor.api.products.model.Product;
import com.github.pannowak.mealsadvisor.api.products.service.ProductService;
import com.github.pannowak.mealsadvisor.web.api.model.ProductDTO;
import com.github.pannowak.mealsadvisor.web.api.model.ProductSummaryDTO;
import com.github.pannowak.mealsadvisor.web.mapper.ModelTransformer;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/products")
public class ProductController {

    private final ProductService productService;
    private final ModelTransformer modelTransformer;

    ProductController(ProductService productService, ModelTransformer modelTransformer) {
        this.productService = productService;
        this.modelTransformer = modelTransformer;
    }

    @ApiOperation("Returns short representations of all the products")
    @ApiResponse(responseCode = "200", description = "Products queried successfully")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<ProductSummaryDTO> getAllProductSummaries() {
        return productService.getAll()
                .map(modelTransformer.to(ProductSummaryDTO.class));
    }

    @ApiOperation("Returns detailed information about a single product")
    @ApiResponse(responseCode = "200", description = "Product queried successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductDTO> getById(@PathVariable("id") Long productId) {
        return productService.getById(productId)
                .map(modelTransformer.to(ProductDTO.class));
    }

    @ApiOperation("Creates a new product")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductDTO> save(@RequestBody Mono<ProductDTO> productDTO) {
        return productDTO
                .map(modelTransformer.to(Product.class))
                .doOnNext(product -> product.setId(null))
                .flatMap(productService::save)
                .map(modelTransformer.to(ProductDTO.class));
    }

    @ApiOperation("Updates/replaces existing product")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductDTO> update(@PathVariable("id") Long productId,
                                   @RequestBody Mono<ProductDTO> productDTO) {
        return productDTO
                .map(modelTransformer.to(Product.class))
                .doOnNext(product -> product.setId(productId))
                .flatMap(productService::save)
                .map(modelTransformer.to(ProductDTO.class));
    }

    @ApiOperation("Deletes a meal")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable("id") Long productId) {
        return productService.deleteById(productId);
    }
}
