package com.shop.generic.orderservice.repositories;


import com.shop.generic.common.entities.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    //TODO: Make this an Optional
    Order findByOrderId(UUID orderId);
}
