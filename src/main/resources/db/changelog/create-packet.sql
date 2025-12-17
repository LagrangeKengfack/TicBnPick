CREATE TABLE packets (
                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         announcement_id UUID NOT NULL,
                         weight DOUBLE PRECISION NOT NULL,
                         height DOUBLE PRECISION NOT NULL,
                         width DOUBLE PRECISION NOT NULL,
                         length DOUBLE PRECISION NOT NULL,
                         photo_packet TEXT NOT NULL,
                         fragile BOOLEAN NOT NULL,
                         perishable BOOLEAN NOT NULL,
                         description TEXT,
                         CONSTRAINT fk_packet_announcement
                             FOREIGN KEY (announcement_id)
                                 REFERENCES announcements(id)
                                 ON DELETE CASCADE
);