package com.polytechnique.ticbnpick.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka configuration.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    /**
     * Creates the Kafka producer factory.
     *
     * @return the ProducerFactory bean
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Creates the KafkaTemplate for sending messages.
     *
     * @return the KafkaTemplate bean
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Creates the delivery-person-created topic.
     *
     * @return the NewTopic bean
     */
    @Bean
    public NewTopic deliveryPersonCreatedTopic() {
        return TopicBuilder.name("delivery-person-created")
                .partitions(1)
                .replicas(1)
                .build();
    }

    /**
     * Creates the delivery-person-validated topic.
     *
     * @return the NewTopic bean
     */
    @Bean
    public NewTopic deliveryPersonValidatedTopic() {
        return TopicBuilder.name("delivery-person-validated")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
