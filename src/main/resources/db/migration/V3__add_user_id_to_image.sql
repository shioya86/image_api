ALTER TABLE image
ADD COLUMN user_id BIGINT;

ALTER TABLE image
ADD CONSTRAINT fk_image_user
FOREIGN KEY (user_id)
REFERENCES "user"(id);

