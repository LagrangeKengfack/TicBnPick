package com.polytechnique.ticbnpick;

import com.polytechnique.ticbnpick.dtos.packet.PacketDTO;
import com.polytechnique.ticbnpick.elasticsearch.models.AnnouncementDocument;
import com.polytechnique.ticbnpick.elasticsearch.repositories.AnnouncementSearchRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.UUID;

@SpringBootTest
public class ElasticsearchIntegrationTest {

        @Autowired
        private AnnouncementSearchRepository announcementSearchRepository;

        @Test
        public void testSaveAndFindAnnouncement() {
                UUID announcementId = UUID.randomUUID();
                AnnouncementDocument doc = AnnouncementDocument.builder()
                                .id(announcementId)
                                .clientId(UUID.randomUUID())
                                .pickupLocation(new GeoPoint(48.8566, 2.3522)) // Paris
                                .deliveryLocation(new GeoPoint(45.7640, 4.8357)) // Lyon
                                .packet(new PacketDTO())
                                .createdAt(Instant.now())
                                .amount(100.0f)
                                .build();

                Mono<AnnouncementDocument> saveAndFind = announcementSearchRepository.save(doc)
                                .then(announcementSearchRepository.findById(announcementId));

                StepVerifier.create(saveAndFind)
                                .expectNextMatches(foundDoc -> foundDoc.getId().equals(announcementId) &&
                                                foundDoc.getPickupLocation().getLat() == 48.8566)
                                .verifyComplete();
        }
}
