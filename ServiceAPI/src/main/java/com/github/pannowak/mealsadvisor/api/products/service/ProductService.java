package com.github.pannowak.mealsadvisor.api.products.service;

import com.github.pannowak.mealsadvisor.api.products.model.Product;
import com.github.pannowak.mealsadvisor.api.products.model.ProductSummary;
import com.github.pannowak.mealsadvisor.api.CrudService;

public interface ProductService extends CrudService<ProductSummary, Product> {}
