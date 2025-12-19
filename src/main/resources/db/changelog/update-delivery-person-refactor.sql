/* ============================================================
   1. ADD type COLUMN TO addresses
   ============================================================ */

ALTER TABLE addresses
    ADD COLUMN type VARCHAR NOT NULL DEFAULT 'PRIMARY';


/* ============================================================
   2. RENAME couriers TABLE TO delivery_persons
   ============================================================ */

ALTER TABLE couriers
    RENAME TO delivery_persons;


/* ============================================================
   3. DROP location COLUMN FROM delivery_persons
   ============================================================ */

ALTER TABLE delivery_persons
DROP COLUMN location;


/* ============================================================
   4. UPDATE deliveries TABLE
   ============================================================ */

ALTER TABLE deliveries
DROP CONSTRAINT IF EXISTS fk_delivery_courier;

ALTER TABLE deliveries
    RENAME COLUMN courier_id TO delivery_person_id;

ALTER TABLE deliveries
    ADD CONSTRAINT fk_delivery_person
        FOREIGN KEY (delivery_person_id)
            REFERENCES delivery_persons(id)
            ON DELETE CASCADE;


/* ============================================================
   5. UPDATE responses TABLE
   ============================================================ */

ALTER TABLE responses
DROP CONSTRAINT IF EXISTS fk_response_courier;

ALTER TABLE responses
    RENAME COLUMN courier_id TO delivery_person_id;

ALTER TABLE responses
    ADD CONSTRAINT fk_response_delivery_person
        FOREIGN KEY (delivery_person_id)
            REFERENCES delivery_persons(id)
            ON DELETE CASCADE;


/* ============================================================
   6. UPDATE logistics TABLE
   ============================================================ */

ALTER TABLE logistics
DROP CONSTRAINT IF EXISTS fk_logistics_courier;

ALTER TABLE logistics
    RENAME COLUMN courier_id TO delivery_person_id;

ALTER TABLE logistics
    ADD CONSTRAINT fk_logistics_delivery_person
        FOREIGN KEY (delivery_person_id)
            REFERENCES delivery_persons(id)
            ON DELETE CASCADE;