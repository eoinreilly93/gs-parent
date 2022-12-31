package com.shop.generic.productservice.valueobjects;

import com.shop.generic.productservice.entities.Product;
import com.shop.generic.productservice.enums.StockStatus;
import java.math.BigDecimal;

public record ProductVO(String name, BigDecimal price, StockStatus stockStatus, int stockCount) {

    public ProductVO(Product product) {
        this(product.getName(), product.getPrice(), product.getStockStatus(), product.getStockCount());
    }
}
