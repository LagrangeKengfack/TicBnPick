package com.polytechnique.ticbnpick.repositories;

import com.polytechnique.ticbnpick.models.PasswordToken;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Reactive repository for PasswordToken entity.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
public interface PasswordTokenRepository extends ReactiveCrudRepository<PasswordToken, UUID> {
    
    /**
     * Finds a token by its value.
     * 
     * @param token the token string
     * @return matching PasswordToken
     */
    Mono<PasswordToken> findByToken(String token);
}
