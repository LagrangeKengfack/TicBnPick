package com.polytechnique.ticbnpick.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Security configuration for WebFlux.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${admin.email:admin@ticbnpick.com}")
    private String adminEmail;

    @Value("${admin.password:admin123}")
    private String adminPassword;

    /**
     * Configures the security filter chain.
     *
     * @param http the ServerHttpSecurity to configure
     * @return the configured SecurityWebFilterChain
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Public endpoints
                        .pathMatchers("/api/delivery-persons/register").permitAll()
                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        // Admin endpoints require authentication
                        .pathMatchers("/api/admin/**").authenticated()
                        // All other endpoints require authentication
                        .anyExchange().authenticated()
                )
                .httpBasic(httpBasic -> {})
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        
        return http.build();
    }

    /**
     * Creates an in-memory user details service with admin user.
     *
     * @return the MapReactiveUserDetailsService bean
     */
    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username(adminEmail)
                .password(passwordEncoder().encode(adminPassword))
                .roles("ADMIN")
                .build();
        return new MapReactiveUserDetailsService(admin);
    }

    /**
     * Creates the password encoder bean.
     *
     * @return the BCryptPasswordEncoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
