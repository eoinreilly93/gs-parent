package com.shop.generic.orderservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.shop.generic.common.dtos.OrderCreationDTO;
import com.shop.generic.common.dtos.OrderResponseDTO;
import com.shop.generic.common.dtos.PurchaseProductDTO;
import com.shop.generic.common.enums.OrderStatus;
import com.shop.generic.orderservice.entities.Order;
import com.shop.generic.orderservice.repositories.OrderRepository;
import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ShippingService shippingService;

    @Test
    @DisplayName("Given a valid order creation request, when creating a shipping order, then should save the order and create a shipping request")
    void testCreateShippingOrder() {
        // Given
        final OrderCreationDTO orderCreationDTO = new OrderCreationDTO(
                Collections.singletonList(new PurchaseProductDTO(1, 25, BigDecimal.TEN)));

        // When
        final OrderService orderService = new OrderService(orderRepository, shippingService);
        final OrderResponseDTO orderResponseDTO = orderService.createShippingOrder(
                orderCreationDTO);

        // Then
        verify(orderRepository).save(any(Order.class));
        verify(shippingService).createShippingRequest(any(Order.class));
        assertEquals(OrderStatus.CREATED, orderResponseDTO.status());
    }

    @Test
    @DisplayName("Given a null order creation request, when creating a shipping order, then should throw a NullPointerException")
    void testCreateShippingOrder_NullOrderCreationRequest() {
        // Given
        final OrderCreationDTO orderCreationDTO = null;

        // When
        final OrderService orderService = new OrderService(orderRepository, shippingService);

        //Then
        assertThrows(
                NullPointerException.class,
                () -> orderService.createShippingOrder(orderCreationDTO));
    }

    @Test
    @DisplayName("Given an order creation request with no products, when creating a shipping order, then should throw a RuntimeException")
    void testCreateShippingOrder_EmptyPurchaseProducts() {
        // Given
        final OrderCreationDTO orderCreationDTO = new OrderCreationDTO(Collections.emptyList());

        // When
        final OrderService orderService = new OrderService(orderRepository, shippingService);
        final RuntimeException exception = assertThrows(RuntimeException.class,
                () ->
                        orderService.createShippingOrder(orderCreationDTO));

        // Then
        assertEquals("An order cannot be created with no products",
                exception.getMessage());
    }
}