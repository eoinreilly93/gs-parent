package com.shop.shippingservice;

import com.shop.generic.common.CommonKafkaProducerAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableKafka
@EnableScheduling
@SpringBootApplication(exclude = {CommonKafkaProducerAutoConfiguration.class})
public class ShippingServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ShippingServiceApplication.class, args);
    }
}
