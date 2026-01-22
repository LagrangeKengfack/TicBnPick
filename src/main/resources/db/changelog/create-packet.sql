CREATE TABLE packets (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    width DOUBLE PRECISION NOT NULL,
    length DOUBLE PRECISION NOT NULL,
    fragile BOOLEAN NOT NULL,
    description VARCHAR,
    photo_packet VARCHAR NOT NULL,
    is_perishable BOOLEAN NOT NULL
);