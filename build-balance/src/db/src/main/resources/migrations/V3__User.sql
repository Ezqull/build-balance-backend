CREATE TABLE IF NOT EXISTS "app_user"
(
    id         SERIAL PRIMARY KEY,
    email      VARCHAR(128) NOT NULL UNIQUE,
    password   VARCHAR(256) NOT NULL,
    first_name VARCHAR(128) NOT NULL,
    last_name  VARCHAR(128) NOT NULL,
    role_id    INTEGER      NOT NULL,
    CONSTRAINT fk_role
        FOREIGN KEY (role_id)
            REFERENCES "role" (id)
            ON DELETE CASCADE
);