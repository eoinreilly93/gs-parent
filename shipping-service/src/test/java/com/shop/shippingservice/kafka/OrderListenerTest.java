package com.shop.shippingservice.kafka;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.shop.generic.common.entities.Order;
import com.shop.generic.common.enums.OrderStatus;
import com.shop.generic.common.kmos.OrderKMO;
import com.shop.shippingservice.entities.Shipment;
import com.shop.shippingservice.repositories.ShipmentsRepository;
import com.shop.shippingservice.scheduler.ShipmentStatusScheduler;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

@SpringBootTest
//@Import(CommonKafkaConsumerAutoConfiguration.class)
//@EmbeddedKafka(partitions = 1, topics = {"orders"})
@Slf4j
class OrderListenerTest {

    private KafkaTemplate<String, OrderKMO> kafkaTemplate;

    @MockBean
    private ShipmentStatusScheduler shipmentStatusScheduler;
//
//    @Autowired
//    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private ShipmentsRepository shipmentsRepository;

    @BeforeEach
    void setup() {

//        final Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        final Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        final DefaultKafkaProducerFactory<String, OrderKMO> producerFactory = new DefaultKafkaProducerFactory<>(
                producerProps);

        kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }

    @Test
    void should_ProcessOrder() throws InterruptedException {
        // Given
        final Order order = new Order(UUID.randomUUID(), new BigDecimal("100.00"), "123,456",
                OrderStatus.CREATED, "London", LocalDateTime.now());
        final OrderKMO orderKMO = new OrderKMO(order);

        // When
        kafkaTemplate.send("orders", orderKMO);
        kafkaTemplate.flush();
        log.info("Sent order to topic...");

//        TimeUnit.SECONDS.sleep(5);

        await()
                .pollInterval(Duration.ofSeconds(1))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    final Optional<Shipment> optionalProduct = shipmentsRepository.findShipmentByShipmentId(
                            1);
                    assertThat(optionalProduct).isPresent();
                    final Shipment shipment = optionalProduct.get();
                    assertThat(shipment.getOrderIds().getFirst()).isEqualTo(order.getOrderId());
                    assertThat(shipment.getCity()).isEqualTo(order.getCity());
                    assertThat(shipment.getStatus()).isEqualTo(OrderStatus.PENDING_DELIVERY);
                    assertThat(shipment.getLastUpdated()).isNotNull();
                });
    }
}