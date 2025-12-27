package com.polytechnique.ticbnpick.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bootstrap configuration to create default admin.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Configuration
public class AdminBootstrapConfig {

    @Bean
    public CommandLineRunner createAdminUser() {
        return args -> {
            // TODO: Check if admin exists using PersonService/Repository
            // If not, create admin user using env vars ADMIN_EMAIL and ADMIN_PASSWORD
        };
    }
}
