CREATE TABLE addresses (
                           id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           street VARCHAR NOT NULL,
                           city VARCHAR NOT NULL,
                           district VARCHAR NOT NULL,
                           country VARCHAR NOT NULL,
                           latitude DOUBLE PRECISION,
                           longitude DOUBLE PRECISION,
                           description TEXT
);