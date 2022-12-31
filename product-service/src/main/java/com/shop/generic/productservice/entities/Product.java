package com.shop.generic.productservice.entities;

import com.shop.generic.productservice.enums.StockStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer productId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Column(name = "STOCK_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;

    @Column(name = "STOCK_COUNT", nullable = false)
    private Integer stockCount;
}
