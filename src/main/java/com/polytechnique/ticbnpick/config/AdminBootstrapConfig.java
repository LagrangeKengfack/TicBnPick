package com.polytechnique.ticbnpick.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bootstrap configuration to create default admin.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Slf4j
@Configuration
public class AdminBootstrapConfig {

    @Value("${admin.email:admin@ticbnpick.com}")
    private String adminEmail;

    @Value("${admin.password:admin123}")
    private String adminPassword;

    /**
     * Creates the admin user on application startup.
     *
     * Admin user is created via in-memory UserDetailsService in SecurityConfig.
     * This method logs the admin credentials for development purposes.
     *
     * @return CommandLineRunner that logs admin info
     */
    @Bean
    public CommandLineRunner createAdminUser() {
        return args -> {
            log.info("=".repeat(60));
            log.info("Admin user configured:");
            log.info("  Email: {}", adminEmail);
            log.info("  Password: [configured via environment variable]");
            log.info("  Use HTTP Basic Auth to access /api/admin/** endpoints");
            log.info("=".repeat(60));
        };
    }
}
