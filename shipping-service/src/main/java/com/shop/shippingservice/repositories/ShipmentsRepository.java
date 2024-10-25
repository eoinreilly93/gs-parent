package com.shop.shippingservice.repositories;

import com.shop.generic.common.enums.OrderStatus;
import com.shop.shippingservice.entities.Shipment;
import java.util.List;
import org.springframework.data.repository.ListCrudRepository;

public interface ShipmentsRepository extends ListCrudRepository<Shipment, Integer> {

    List<Shipment> findAllByCityAndStatusIs(final String city, OrderStatus status);

    List<Shipment> findShipmentByStatus(OrderStatus status);
}
