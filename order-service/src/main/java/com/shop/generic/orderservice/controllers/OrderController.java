package com.shop.generic.orderservice.controllers;

import com.shop.generic.common.dtos.OrderCreationDTO;
import com.shop.generic.common.dtos.OrderResponseDTO;
import com.shop.generic.common.rest.response.RestApiResponse;
import com.shop.generic.common.rest.response.RestApiResponseFactory;
import com.shop.generic.orderservice.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private final RestApiResponseFactory restApiResponseFactory;
    private final OrderService orderService;

    public OrderController(final RestApiResponseFactory restApiResponseFactory,
            final OrderService orderService) {
        this.restApiResponseFactory = restApiResponseFactory;
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<RestApiResponse<OrderResponseDTO>> createOrder(
            @RequestBody final OrderCreationDTO orderCreationDTO) {
        log.info("Creating order");
        final OrderResponseDTO responseDTO = this.orderService.createShippingOrder(
                orderCreationDTO);

        log.info("Created order with order id: {}", responseDTO.orderId());
        return ResponseEntity.ok(this.restApiResponseFactory.createSuccessResponse(responseDTO));
    }
}
