package com.polytechnique.ticbnpick.repositories;

import com.polytechnique.ticbnpick.models.Announcement;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * Reactive repository for Announcement entity.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
public interface AnnouncementRepository extends ReactiveCrudRepository<Announcement, UUID> {

    /**
     * Finds all announcements created by a client.
     *
     * @param client_id client identifier
     * @return list of announcements
     */
//    Flux<Announcement> findAllByClientId(UUID client_id);
}