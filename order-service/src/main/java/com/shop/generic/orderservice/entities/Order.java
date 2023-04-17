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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * The @RequiredArgsConstructor is used in conjunction with @NonNull to allow you to create on
 * object with only the fields annotation with NonNull. It basically lets you create an object
 * without having to specify the id, which is auto generated on persist anyways
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "ORDER_ID", nullable = false)
    private UUID orderId;

    @NonNull
    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @NonNull
    @Column(name = "PRODUCT_IDS", nullable = false)
    private String productIds;

    @NonNull
    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //TODO: Add additional fields such as name, address etc.

}
