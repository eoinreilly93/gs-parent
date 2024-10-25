package com.shop.generic.orderservice.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.shop.generic.common.dtos.OrderCreationDTO;
import com.shop.generic.common.dtos.OrderStatusDTO;
import com.shop.generic.common.dtos.PurchaseProductDTO;
import com.shop.generic.common.entities.Order;
import com.shop.generic.common.enums.OrderStatus;
import com.shop.generic.orderservice.repositories.OrderRepository;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ShippingService shippingService;

    //Should really use constructor injection but showing this as an alternative example for injecting the mocks
    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("Given a valid order creation request, when creating a shipping order, then should save the order and create a shipping request")
    void testCreateShippingOrder() {
        // Given
        final PurchaseProductDTO product1 = new PurchaseProductDTO(1, 5, new BigDecimal("10.00"));
        final PurchaseProductDTO product2 = new PurchaseProductDTO(2, 2, new BigDecimal("14.99"));
        final OrderCreationDTO orderCreationDTO = new OrderCreationDTO(List.of(product1, product2),
                "London");

        // When
        final OrderStatusDTO orderStatusDTO = orderService.createShippingOrder(
                orderCreationDTO);

        // Then
        assertNotNull(orderStatusDTO);
        assertEquals(OrderStatus.CREATED, orderStatusDTO.status());

        // Capture the saved order
        final ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(orderCaptor.capture());
        final Order savedOrder = orderCaptor.getValue();

        assertNotNull(savedOrder.getOrderId());
        assertEquals(new BigDecimal("79.98"), savedOrder.getPrice());
        assertEquals(OrderStatus.CREATED, savedOrder.getStatus());
        assertEquals("1,2", savedOrder.getProductIds());

        verify(orderRepository).save(any(Order.class));
        verify(shippingService).createOrderShippingRequest(any(Order.class));
    }

    @Test
    @DisplayName("Given a null order creation request, when creating a shipping order, then should throw a NullPointerException")
    void testCreateShippingOrder_NullOrderCreationRequest() {
        // Given
        final OrderCreationDTO orderCreationDTO = null;

        //Then
        assertThrows(
                NullPointerException.class,
                () -> orderService.createShippingOrder(orderCreationDTO));
    }

    @Test
    @DisplayName("Given an order creation request with no products, when creating a shipping order, then should throw a RuntimeException")
    void testCreateShippingOrder_EmptyPurchaseProducts() {
        // Given
        final OrderCreationDTO orderCreationDTO = new OrderCreationDTO(Collections.emptyList(),
                "London");

        // When
        final RuntimeException exception = assertThrows(RuntimeException.class,
                () ->
                        orderService.createShippingOrder(orderCreationDTO));

        // Then
        assertEquals("An order cannot be created with no products",
                exception.getMessage());
    }

    @Test
    @DisplayName("Verify a shipping request is not sent if the order fails to persist to the database")
    void testCreateShippingOrder_FailureInRepository() {
        // Arrange
        final PurchaseProductDTO product1 = new PurchaseProductDTO(1, 2, new BigDecimal("10.00"));
        final OrderCreationDTO orderCreationDTO = new OrderCreationDTO(List.of(product1), "London");

        doThrow(new RuntimeException("Database error")).when(orderRepository)
                .save(any(Order.class));

        final OrderService orderService = new OrderService(orderRepository, shippingService);

        // Act & Assert
        final RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.createShippingOrder(orderCreationDTO);
        });

        assertEquals("Database error", exception.getMessage());

        // Verify that shipping request was not made due to the exception
        verify(shippingService, never()).createOrderShippingRequest(any(Order.class));
    }
}