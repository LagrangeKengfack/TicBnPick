DROP TABLE IF EXISTS password_tokens;
DROP TABLE IF EXISTS document;
DROP TABLE IF EXISTS documents;

-- Alter delivery_persons table: change commercial_register from UUID to VARCHAR
-- First drop constraint if it exists (assuming it was a FK)
-- Note: Constraint name might vary, but standard naming is often used.
-- If we don't know the exact constraint name, we might need to query it or ignore if not exists.
-- For Postgres/H2:
ALTER TABLE delivery_persons DROP CONSTRAINT IF EXISTS fk_delivery_person_commercial_register; 
-- If the constraint name was different, this might fail or do nothing.
-- Assuming standard naming convention or no constraint.
ALTER TABLE delivery_persons ALTER COLUMN commercial_register TYPE VARCHAR;

-- Add logistic_image to logistics table
ALTER TABLE logistics ADD COLUMN IF NOT EXISTS logistic_image VARCHAR;
