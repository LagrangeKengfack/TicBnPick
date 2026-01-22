CREATE TABLE announcements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    client_id UUID NOT NULL,
    packet_id UUID,
    pickup_address_id UUID NOT NULL,
    delivery_address_id UUID NOT NULL,
    title VARCHAR NOT NULL,
    description VARCHAR,
    status VARCHAR NOT NULL,
    price DOUBLE PRECISION,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    recipient_name VARCHAR,
    recipient_number VARCHAR,
    amount REAL,
    CONSTRAINT fk_announcement_client FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    CONSTRAINT fk_announcement_pickup_address FOREIGN KEY (pickup_address_id) REFERENCES addresses(id),
    CONSTRAINT fk_announcement_delivery_address FOREIGN KEY (delivery_address_id) REFERENCES addresses(id)
);