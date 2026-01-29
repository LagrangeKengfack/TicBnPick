package com.polytechnique.ticbnpick.services.support;

import com.polytechnique.ticbnpick.events.DeliveryPersonCreatedEvent;
import com.polytechnique.ticbnpick.events.DeliveryPersonValidatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka consumer for delivery person events.
 * Logs events for monitoring and debugging purposes.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Slf4j
@Service
public class DeliveryPersonEventConsumer {

    /**
     * Listens for DeliveryPersonCreatedEvent messages.
     *
     * @param event the received event
     */
    @KafkaListener(topics = "delivery-person-created", groupId = "ticbnpick-group")
    public void handleDeliveryPersonCreated(DeliveryPersonCreatedEvent event) {
        log.info("Received DeliveryPersonCreatedEvent: deliveryPersonId={}, email={}",
                event.getDeliveryPersonId(), event.getEmail());
    }

    /**
     * Listens for DeliveryPersonValidatedEvent messages.
     *
     * @param event the received event
     */
    @KafkaListener(topics = "delivery-person-validated", groupId = "ticbnpick-group")
    public void handleDeliveryPersonValidated(DeliveryPersonValidatedEvent event) {
        log.info("Received DeliveryPersonValidatedEvent: deliveryPersonId={}, approved={}",
                event.getDeliveryPersonId(), event.isApproved());
    }
}
