package com.shop.generic.orderservice.entities;

import com.shop.generic.common.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ORDER_ID", nullable = false)
    private UUID orderId;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Column(name = "PRODUCT_IDS", nullable = false)
    private String productIds;

    //TODO: Add additional fields such as name, address etc.

}
