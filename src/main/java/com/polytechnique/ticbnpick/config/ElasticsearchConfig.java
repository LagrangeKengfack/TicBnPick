package com.polytechnique.ticbnpick.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;
import org.springframework.lang.NonNull;

/**
 * Configuration class for Elasticsearch connection.
 * Configures the reactive client and repository settings.
 *
 * @author François-Charles ATANGA
 * @date 05/02/2026
 */
@Configuration
@EnableReactiveElasticsearchRepositories(basePackages = "com.polytechnique.ticbnpick.elasticsearch.repositories")
public class ElasticsearchConfig extends ReactiveElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris:localhost:9200}")
    private String elasticsearchUri;

    @Override
    @NonNull
    public ClientConfiguration clientConfiguration() {
        // Remove "http://" or "https://" prefix if present, as ClientConfiguration
        // expects host:port
        String connectionString = elasticsearchUri.replace("http://", "").replace("https://", "");

        return ClientConfiguration.builder()
                .connectedTo(connectionString)
                .build();
    }
}
