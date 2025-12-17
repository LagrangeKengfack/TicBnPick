CREATE TABLE logistics (
                           id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           courier_id UUID NOT NULL,
                           plate_number VARCHAR NOT NULL,
                           logistics_photo TEXT NOT NULL,
                           logistics_type VARCHAR NOT NULL,
                           logistics_class VARCHAR NOT NULL,
                           rating DOUBLE PRECISION,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP,
                           tank_capacity DOUBLE PRECISION,
                           luggage_max_capacity DOUBLE PRECISION,
                           total_seat_number INTEGER,
                           CONSTRAINT fk_logistics_courier
                               FOREIGN KEY (courier_id)
                                   REFERENCES couriers(id)
                                   ON DELETE CASCADE
);