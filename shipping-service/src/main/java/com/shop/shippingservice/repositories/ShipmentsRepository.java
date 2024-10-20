package com.shop.shippingservice.repositories;

import com.shop.shippingservice.entities.Shipment;
import com.shop.shippingservice.enums.ShipmentStatus;
import java.util.List;
import org.springframework.data.repository.ListCrudRepository;

public interface ShipmentsRepository extends ListCrudRepository<Shipment, Integer> {

    List<Shipment> findAllByCityAndStatusIs(final String city, ShipmentStatus status);

    List<Shipment> findShipmentByStatus(ShipmentStatus status);
}
