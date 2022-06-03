package com.github.pannowak.mealsadvisor.gui.views.products.gateway;

import com.github.pannowak.mealsadvisor.api.products.model.Product;
import com.github.pannowak.mealsadvisor.api.products.model.ProductSummary;
import com.github.pannowak.mealsadvisor.api.products.service.ProductService;
import com.github.pannowak.mealsadvisor.gui.CrudGateway;
import com.github.pannowak.mealsadvisor.gui.Gateway;

@Gateway
public class ProductGateway extends CrudGateway<ProductSummary, Product> {

    ProductGateway(ProductService productService) {
        super(productService);
    }
}
