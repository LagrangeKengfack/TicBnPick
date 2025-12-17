CREATE TABLE deliveries (
                            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            announcement_id UUID NOT NULL,
                            packet_id UUID NOT NULL,
                            courier_id UUID NOT NULL,
                            sender_phone VARCHAR NOT NULL,
                            receiver_phone VARCHAR NOT NULL,
                            pickup_time_min TIMESTAMP NOT NULL,
                            pickup_time_max TIMESTAMP NOT NULL,
                            delivery_time_min TIMESTAMP NOT NULL,
                            delivery_time_max TIMESTAMP NOT NULL,
                            estimated_delivery TIMESTAMP,
                            distance_km DOUBLE PRECISION,
                            urgency BOOLEAN NOT NULL,
                            delivery_note TEXT,
                            status VARCHAR NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP,
                            CONSTRAINT fk_delivery_announcement
                                FOREIGN KEY (announcement_id)
                                    REFERENCES announcements(id),
                            CONSTRAINT fk_delivery_packet
                                FOREIGN KEY (packet_id)
                                    REFERENCES packets(id),
                            CONSTRAINT fk_delivery_courier
                                FOREIGN KEY (courier_id)
                                    REFERENCES couriers(id)
);