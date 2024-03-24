CREATE TYPE token_type AS ENUM ('BEARER');

CREATE TABLE IF NOT EXISTS "token"
(
    id         SERIAL PRIMARY KEY,
    token      TEXT       NOT NULL UNIQUE,
    token_type token_type NOT NULL,
    expired    BOOLEAN    NOT NULL,
    revoked    BOOLEAN    NOT NULL,
    user_id    INTEGER    NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES "app_user" (id)
            ON DELETE CASCADE
);