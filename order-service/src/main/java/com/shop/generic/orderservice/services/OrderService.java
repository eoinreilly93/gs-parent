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
    private final ShippingService shippingService;

    public OrderService(final OrderRepository orderRepository,
            final ShippingService shippingService) {
        this.orderRepository = orderRepository;
        this.shippingService = shippingService;
    }

    public OrderResponseDTO createShippingOrder(final OrderCreationDTO orderCreationDTO) {
        final UUID orderId = UUID.randomUUID();
        final Order order = createOrder(orderId, orderCreationDTO);
        saveOrder(order);
        createAndSendShippingRequest(order);
        return new OrderResponseDTO(orderId, OrderStatus.CREATED);
    }

    private Order createOrder(final UUID orderId, final OrderCreationDTO orderCreationDTO) {
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
        return order;
    }

    private void saveOrder(final Order order) {
        this.orderRepository.save(order);
        log.info("Persisted order {} to the database", order.getOrderId());
    }


    private void createAndSendShippingRequest(final Order order) {
        log.info("Creating shipping request");
    }
}
