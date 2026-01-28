package com.polytechnique.ticbnpick.services.support;

import com.polytechnique.ticbnpick.events.DeliveryPersonCreatedEvent;
import com.polytechnique.ticbnpick.events.DeliveryPersonValidatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for publishing Kafka events.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private static final String TOPIC_DELIVERY_PERSON_CREATED = "delivery-person-created";
    private static final String TOPIC_DELIVERY_PERSON_VALIDATED = "delivery-person-validated";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Publishes a DeliveryPersonCreatedEvent to Kafka.
     *
     * @param event the event to publish
     */
    public void publishDeliveryPersonCreated(DeliveryPersonCreatedEvent event) {
        log.info("Publishing DeliveryPersonCreatedEvent for ID: {}", event.getDeliveryPersonId());
        kafkaTemplate.send(TOPIC_DELIVERY_PERSON_CREATED, event.getDeliveryPersonId().toString(), event);
    }

    /**
     * Publishes a DeliveryPersonValidatedEvent to Kafka.
     *
     * @param event the event to publish
     */
    public void publishDeliveryPersonValidated(DeliveryPersonValidatedEvent event) {
        log.info("Publishing DeliveryPersonValidatedEvent for ID: {}, approved: {}", 
                event.getDeliveryPersonId(), event.isApproved());
        kafkaTemplate.send(TOPIC_DELIVERY_PERSON_VALIDATED, event.getDeliveryPersonId().toString(), event);
    }
}
