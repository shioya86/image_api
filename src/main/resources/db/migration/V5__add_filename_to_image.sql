ALTER TABLE image
ADD COLUMN filename VARCHAR(255);

UPDATE image
SET filename = regexp_replace(path, '^.*/', '');

ALTER TABLE image
ALTER COLUMN filename SET NOT NULL;

ALTER TABLE image
ADD CONSTRAINT uq_image_filename UNIQUE (filename);

ALTER TABLE image
ADD COLUMN media VARCHAR(64);

UPDATE image
SET media = 'image/jpeg'
WHERE media IS NULL;
