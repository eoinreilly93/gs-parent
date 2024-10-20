package com.shop.shippingservice.services;

import com.shop.generic.common.kmos.OrderKMO;
import com.shop.shippingservice.entities.Shipment;
import com.shop.shippingservice.enums.ShipmentStatus;
import com.shop.shippingservice.repositories.ShipmentsRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingService {

    private final ShipmentsRepository shipmentsRepository;

    public void createOrUpdatePendingShipmentManifest(final OrderKMO orderKMO) {
        //Check if there already is a shipment pending for this city
        final List<Shipment> shipments = this.shipmentsRepository.findAllByCityAndStatusIs(
                orderKMO.city(), ShipmentStatus.PENDING_DELIVERY);

        //Create a shipment if one doesn't exist
        if (shipments.isEmpty()) {
            final Shipment shipment = new Shipment(List.of(orderKMO.orderId()), orderKMO.city(),
                    ShipmentStatus.PENDING_DELIVERY, LocalDateTime.now(), LocalDateTime.now());
            this.shipmentsRepository.save(shipment);
            log.info("Created shipment {}", shipment);
        } else {
            //TODO: Add some smarter logic instead of just updating the first one. Something to do later
            final Shipment shipment = shipments.getFirst();
            shipment.getOrderIds().add(orderKMO.orderId());
            shipment.setLastUpdated(LocalDateTime.now());
            this.shipmentsRepository.save(shipment);
            log.info("Updated shipment {} with order id {}", shipment, orderKMO.orderId());
        }
    }

    public void updatePendingShipments() {
        final List<Shipment> shipmentsPendingDelivery = this.shipmentsRepository.findShipmentByStatus(
                ShipmentStatus.PENDING_DELIVERY);

        final var filteredList = shipmentsPendingDelivery.stream()
                .filter(shipment -> shipment.getOrderIds().size() >= 5)
                .peek(shipment -> shipment.setStatus(ShipmentStatus.DELIVERY_IN_PROGRESS))
                .toList();

        if (!filteredList.isEmpty()) {
            this.shipmentsRepository.saveAll(filteredList);
            log.info("Updated shipment status for {} deliveries to {}", filteredList.size(),
                    ShipmentStatus.DELIVERY_IN_PROGRESS);
        } else {
            log.info("No pending deliveries meet the criteria for updating their status");
        }
    }

    public void updateInProgressShipments() {
        //TODO: Implement a type of audit trail for tracking the status updates, similar to rtgs
        final List<Shipment> shipmentsPendingDelivery = this.shipmentsRepository.findShipmentByStatus(
                ShipmentStatus.DELIVERY_IN_PROGRESS);

        //If a shipment has been in DELIVERY_IN_PROGRESS for more than x minutes, set it to delivered
        final var filteredList = shipmentsPendingDelivery.stream()
                .filter(shipment -> {
                    final Instant lastUpdatedInstant = shipment.getLastUpdated()
                            .atZone(ZoneId.systemDefault()).toInstant();
                    final Instant currentInstant = LocalDateTime.now()
                            .atZone(ZoneId.systemDefault()).toInstant();
                    final Duration duration = Duration.between(lastUpdatedInstant, currentInstant);
                    return duration.toMinutes() > 2;
                })
                .peek(shipment -> shipment.setStatus(ShipmentStatus.DELIVERY_COMPLETED))
                .toList();

        if (!filteredList.isEmpty()) {
            this.shipmentsRepository.saveAll(filteredList);
            log.info("Updated shipment status for {} deliveries to {}", filteredList.size(),
                    ShipmentStatus.DELIVERY_COMPLETED);
        } else {
            log.info("No in progress deliveries meet the criteria for updating their status");
        }
    }
}
