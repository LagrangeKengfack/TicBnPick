-- Update Logistics table
ALTER TABLE logistics ADD COLUMN back_photo VARCHAR(255);
ALTER TABLE logistics ADD COLUMN front_photo VARCHAR(255);
ALTER TABLE logistics DROP COLUMN logistic_image;

-- Update Packets table
ALTER TABLE packets ADD COLUMN designation VARCHAR(255);

-- Update Announcements table
ALTER TABLE announcements ADD COLUMN shipper_name VARCHAR(255);
ALTER TABLE announcements ADD COLUMN shipper_email VARCHAR(255);
ALTER TABLE announcements ADD COLUMN shipper_phone VARCHAR(255);
ALTER TABLE announcements ADD COLUMN recipient_email VARCHAR(255);
ALTER TABLE announcements ADD COLUMN recipient_phone VARCHAR(255);
