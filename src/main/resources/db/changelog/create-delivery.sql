CREATE TABLE deliveries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    announcement_id UUID NOT NULL,
    delivery_person_id UUID NOT NULL,
    status VARCHAR NOT NULL,
    tarif INTEGER,
    note_livreur REAL,
    pickup_min_time TIMESTAMP NOT NULL,
    pickup_max_time TIMESTAMP NOT NULL,
    urgency VARCHAR NOT NULL,
    delivery_min_time TIMESTAMP NOT NULL,
    delivery_max_time TIMESTAMP NOT NULL,
    estimated_delivery TIMESTAMP,
    delivery_note DOUBLE PRECISION,
    distance_km DOUBLE PRECISION,
    CONSTRAINT fk_delivery_announcement FOREIGN KEY (announcement_id) REFERENCES announcements(id) ON DELETE CASCADE,
    CONSTRAINT fk_delivery_person FOREIGN KEY (delivery_person_id) REFERENCES delivery_persons(id) ON DELETE CASCADE
);