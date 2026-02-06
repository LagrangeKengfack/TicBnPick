package com.polytechnique.ticbnpick.services;

import com.polytechnique.ticbnpick.dtos.announcement.AnnouncementResponseDTO;
import com.polytechnique.ticbnpick.elasticsearch.models.AnnouncementDocument;
import com.polytechnique.ticbnpick.elasticsearch.models.DeliveryPersonDocument;
import com.polytechnique.ticbnpick.elasticsearch.repositories.AnnouncementSearchRepository;
import com.polytechnique.ticbnpick.elasticsearch.repositories.DeliveryPersonSearchRepository;
import com.polytechnique.ticbnpick.events.AnnouncementPublishedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service responsible for matching announcements with eligible delivery
 * persons.
 * Consumes Kafka events and performs spatial filtering.
 *
 * @author François-Charles ATANGA
 * @date 03/02/2026
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingService {

    private final AnnouncementSearchRepository announcementSearchRepository;
    private final DeliveryPersonSearchRepository deliveryPersonSearchRepository;
    private final NotificationService notificationService;

    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double INITIAL_DELTA_KM = 1.5;
    private static final double DELTA_INCREMENT_KM = 0.5;

    /**
     * Consumes the AnnouncementPublishedEvent and triggers the matching process.
     *
     * @param event The published announcement event.
     */
    @KafkaListener(topics = "announcement-published", groupId = "ticbnpick-group")
    public void consumeAnnouncementPublishedEvent(AnnouncementPublishedEvent event) {
        log.info("Received AnnouncementPublishedEvent: {}", event);

        AnnouncementResponseDTO announcementDTO = event.getAnnouncement();

        // 1. Index Announcement in Elasticsearch
        AnnouncementDocument announcementDoc = AnnouncementDocument.builder()
                .id(announcementDTO.getId())
                .clientId(announcementDTO.getClientId())
                .packet(announcementDTO.getPacket())
                .createdAt(announcementDTO.getCreatedAt() != null ? announcementDTO.getCreatedAt() : Instant.now())
                .amount(announcementDTO.getAmount())
                .build();

        if (announcementDTO.getPickupAddress() != null
                && announcementDTO.getPickupAddress().getLatitude() != null
                && announcementDTO.getPickupAddress().getLongitude() != null) {
            announcementDoc.setPickupLocation(new GeoPoint(
                    announcementDTO.getPickupAddress().getLatitude(),
                    announcementDTO.getPickupAddress().getLongitude()));
        } else {
            log.error("Pickup address or coordinates missing for announcement {}", announcementDTO.getId());
            return;
        }

        if (announcementDTO.getDeliveryAddress() != null
                && announcementDTO.getDeliveryAddress().getLatitude() != null
                && announcementDTO.getDeliveryAddress().getLongitude() != null) {
            announcementDoc.setDeliveryLocation(new GeoPoint(
                    announcementDTO.getDeliveryAddress().getLatitude(),
                    announcementDTO.getDeliveryAddress().getLongitude()));
        } else {
            log.error("Delivery address or coordinates missing for announcement {}", announcementDTO.getId());
            return;
        }

        // Execute the full reactive pipeline synchronously to ensure Kafka delivery
        // guarantees
        announcementSearchRepository.save(announcementDoc)
                .doOnSuccess(saved -> log.info("Announcement indexed in Elasticsearch: {}", saved.getId()))
                .flatMap(saved -> performMatching(saved, INITIAL_DELTA_KM))
                .doOnError(e -> log.error("Error processing announcement {}", announcementDTO.getId(), e))
                .block(); // Block to wait for completion or exception
    }

    private Mono<Void> performMatching(AnnouncementDocument announcement, double delta) {
        GeoPoint F1 = announcement.getPickupLocation();
        GeoPoint F2 = announcement.getDeliveryLocation();

        double distF1F2 = calculateHaversineDistance(F1.getLat(), F1.getLon(), F2.getLat(), F2.getLon());
        double dMax = distF1F2 + (2 * delta);

        log.info("Starting matching for Announcement {} with delta={} km, Dmax={} km", announcement.getId(), delta,
                dMax);

        // 3. Initial Search in Elasticsearch with Radius = Dmax
        return deliveryPersonSearchRepository
                .findByIsAvailableTrueAndIsActiveTrueAndLocationNear(F1, new Distance(dMax, Metrics.KILOMETERS))
                .collectList()
                .flatMap(candidates -> {
                    // 4. Spatial Filtering (Spherical Ellipse)
                    List<DeliveryPersonDocument> eligibleCandidates = new ArrayList<>();
                    for (DeliveryPersonDocument candidate : candidates) {
                        double distP_F1 = calculateHaversineDistance(candidate.getLocation().getLat(),
                                candidate.getLocation().getLon(), F1.getLat(), F1.getLon());
                        double distP_F2 = calculateHaversineDistance(candidate.getLocation().getLat(),
                                candidate.getLocation().getLon(), F2.getLat(), F2.getLon());

                        if (distP_F1 + distP_F2 <= dMax) {
                            eligibleCandidates.add(candidate);
                        }
                    }

                    if (eligibleCandidates.isEmpty()) {
                        log.info("No eligible candidates found for delta={}. Expanding search...", delta);
                        // 5. Expand Delta and Retry
                        if (delta < 50.0) { // Safety break
                            return performMatching(announcement, delta + DELTA_INCREMENT_KM);
                        } else {
                            log.warn("Matching failed after max expansion for Announcement {}", announcement.getId());
                            return Mono.empty();
                        }
                    } else {
                        log.info("Found {} eligible candidates: {}", eligibleCandidates.size(),
                                eligibleCandidates.stream().map(DeliveryPersonDocument::getId).toList());

                        // Proceed to Notification Phase
                        return notificationService.notifyEligibleDeliveryPersons(eligibleCandidates, announcement)
                                .then();
                    }
                });
    }

    /**
     * Calculates the Haversine distance between two points in Kilometers.
     */
    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1Rad) * Math.cos(lat2Rad);

        double c = 2 * Math.asin(Math.sqrt(a));

        return EARTH_RADIUS_KM * c;
    }
}
