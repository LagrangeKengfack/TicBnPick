CREATE TABLE responses (
                           id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           announcement_id UUID NOT NULL,
                           courier_id UUID NOT NULL,
                           arrival_time TIMESTAMP NOT NULL,
                           courier_amount DOUBLE PRECISION NOT NULL,
                           status VARCHAR NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_response_announcement
                               FOREIGN KEY (announcement_id)
                                   REFERENCES announcements(id)
                                   ON DELETE CASCADE,
                           CONSTRAINT fk_response_courier
                               FOREIGN KEY (courier_id)
                                   REFERENCES couriers(id)
                                   ON DELETE CASCADE,
                           CONSTRAINT uq_response_unique
                               UNIQUE (announcement_id, courier_id)
);