package com.shop.generic.orderservice.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.shop.generic.common.enums.OrderStatus;
import com.shop.generic.orderservice.entities.Order;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Repository should save an order")
    public void should_saveAnOrder() {
        //Given
        final Order order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setPrice(BigDecimal.valueOf(2030.55));
        order.setProductIds("1,2,3");
        order.setStatus(OrderStatus.CREATED);

        //When
        assertThat(order.getId()).isNull();
        this.orderRepository.save(order);

        //Then
        assertThat(order.getId()).isNotNull();
    }

    @Test
    @DisplayName("Repository should retrieve an order by it's order id")
    public void should_retrieveAnOrder_byOrderId() {
        //Given
        final UUID orderId = UUID.randomUUID();
        final Order order = new Order();
        order.setOrderId(orderId);
        order.setPrice(BigDecimal.valueOf(2030.55));
        order.setProductIds("1,2,3");
        order.setStatus(OrderStatus.CREATED);
        this.testEntityManager.persist(order);

        //When
        final Order persistedOrder = this.orderRepository.findByOrderId(orderId);

        //Then
        assertThat(persistedOrder).isEqualTo(order);
        assertEquals(persistedOrder.getProductIds(), "1,2,3");
    }
}