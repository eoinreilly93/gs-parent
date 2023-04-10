package com.shop.generic.orderservice.services;

import com.shop.generic.common.dtos.OrderCreationDTO;
import com.shop.generic.common.dtos.OrderResponseDTO;
import com.shop.generic.common.enums.OrderStatus;
import com.shop.generic.orderservice.entities.Order;
import com.shop.generic.orderservice.repositories.OrderRepository;
import java.util.UUID;
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
        final Order order = new Order();
//        this.orderRepository.save(order);

        final OrderResponseDTO responseDTO = new OrderResponseDTO(orderId, OrderStatus.CREATED);
        return responseDTO;
    }

}
