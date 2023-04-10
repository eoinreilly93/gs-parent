package com.shop.generic.orderservice.services;

import com.shop.generic.common.dtos.OrderCreationDTO;
import com.shop.generic.common.dtos.OrderResponseDTO;
import com.shop.generic.common.dtos.PurchaseProductDTO;
import com.shop.generic.common.enums.OrderStatus;
import com.shop.generic.orderservice.entities.Order;
import com.shop.generic.orderservice.repositories.OrderRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponseDTO createOrder(final OrderCreationDTO orderCreationDTO) {
        final UUID orderId = UUID.randomUUID();
        final BigDecimal orderCost = orderCreationDTO.purchaseProductDTOS().stream().map(
                        PurchaseProductDTO::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final Order order = new Order();
        order.setPrice(orderCost.setScale(2, RoundingMode.HALF_UP));
        order.setStatus(OrderStatus.CREATED);

        final List<Integer> productIds = orderCreationDTO.purchaseProductDTOS().stream()
                .map(PurchaseProductDTO::productId)
                .toList();

        final String productIdsAsString = productIds.stream().map(String::valueOf)
                .collect(Collectors.joining(","));
        order.setProductIds(productIdsAsString);
        order.setOrderId(orderId);

        saveOrder(order);
        return new OrderResponseDTO(orderId, OrderStatus.CREATED);
    }

    private void saveOrder(final Order order) {
        this.orderRepository.save(order);
        log.info("Persisted order {} to the database", order.getOrderId());
    }
}
