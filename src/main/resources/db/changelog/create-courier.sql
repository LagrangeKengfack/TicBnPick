CREATE TABLE couriers (
                          id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                          person_id UUID NOT NULL UNIQUE,
                          location VARCHAR NOT NULL,
                          commercial_register VARCHAR NOT NULL,
                          commercial_name VARCHAR NOT NULL,
                          taxpayer_number VARCHAR NOT NULL,
                          status VARCHAR NOT NULL,
                          is_active BOOLEAN NOT NULL,
                          created_at TIMESTAMP,
                          updated_at TIMESTAMP,
                          commission_rate DOUBLE PRECISION,
                          siret DOUBLE PRECISION,
                          CONSTRAINT fk_courier_person
                              FOREIGN KEY (person_id)
                                  REFERENCES persons(id)
                                  ON DELETE CASCADE
);