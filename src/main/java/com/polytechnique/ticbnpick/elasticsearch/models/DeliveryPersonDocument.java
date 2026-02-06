package com.polytechnique.ticbnpick.elasticsearch.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import java.util.UUID;

/**
 * Elasticsearch document representing a delivery person.
 * Used for geospatial searches and matching.
 *
 * @author François-Charles ATANGA
 * @date 03/02/2026
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "delivery-persons")
public class DeliveryPersonDocument {

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Id
    private UUID id;

    @GeoPointField
    private GeoPoint location;

    @Field(type = FieldType.Text)
    private String firstName;

    @Field(type = FieldType.Text)
    private String lastName;

    @Field(type = FieldType.Keyword)
    private String email;

    @Field(type = FieldType.Keyword)
    private String phone;

    @Field(type = FieldType.Text)
    private String commercialName;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Boolean)
    private Boolean isActive;

    @Field(type = FieldType.Boolean)
    private Boolean isAvailable;
}
