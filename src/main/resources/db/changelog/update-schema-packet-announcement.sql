--liquibase formatted sql

--changeset antigravity:add-thickness-remove-price
ALTER TABLE packets ADD COLUMN thickness DOUBLE PRECISION;
ALTER TABLE announcements DROP COLUMN price;
